package com.example.chartboardbackend.controller;

import com.example.chartboardbackend.entity.User;
import com.example.chartboardbackend.service.UserService;
import com.example.chartboardbackend.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户认证相关的API接口")
public class AuthController {
    
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        User user = userService.getUserByUsername(request.getUsername())
                .orElse(null);
        
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "用户名或密码错误"));
        }
        
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "登录成功");
        response.put("token", token);
        response.put("user", Map.of(
            "id", user.getId(),
            "username", user.getUsername(),
            "role", user.getRole()
        ));
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/validate")
    @Operation(summary = "验证Token", description = "验证Token是否有效")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);
                String role = jwtUtil.getRoleFromToken(token);
                
                return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "username", username,
                    "role", role
                ));
            }
        } catch (Exception e) {
            // Token无效
        }
        
        return ResponseEntity.status(401).body(Map.of("valid", false, "message", "Token已失效"));
    }
    
    @PostMapping("/change-password")
    @Operation(summary = "修改密码", description = "修改用户密码")
    public ResponseEntity<Map<String, Object>> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ChangePasswordRequest request) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("message", "Token已失效"));
            }
            
            String username = jwtUtil.getUsernameFromToken(token);
            User user = userService.getUserByUsername(username).orElse(null);
            
            if (user == null) {
                return ResponseEntity.status(404).body(Map.of("message", "用户不存在"));
            }
            
            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                return ResponseEntity.status(400).body(Map.of("message", "原密码错误"));
            }
            
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userService.saveUser(user);
            
            return ResponseEntity.ok(Map.of("message", "密码修改成功"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "修改密码失败"));
        }
    }
    
    @GetMapping("/encode-password")
    @Operation(summary = "生成加密密码（仅用于测试）", description = "生成BCrypt加密密码")
    public ResponseEntity<Map<String, Object>> encodePassword(@RequestParam String password) {
        String encoded = passwordEncoder.encode(password);
        boolean matches = passwordEncoder.matches(password, encoded);
        
        // 测试现有的admin密码
        String existingHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        boolean matchesExisting = passwordEncoder.matches(password, existingHash);
        
        return ResponseEntity.ok(Map.of(
            "original", password,
            "encoded", encoded,
            "matches", matches,
            "matchesExistingHash", matchesExisting
        ));
    }
    
    @Data
    static class LoginRequest {
        private String username;
        private String password;
    }
    
    @Data
    static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;
    }
}
