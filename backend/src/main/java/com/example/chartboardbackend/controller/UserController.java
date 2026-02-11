package com.example.chartboardbackend.controller;

import com.example.chartboardbackend.entity.User;
import com.example.chartboardbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户相关的API接口")
public class UserController {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    @GetMapping
    @Operation(summary = "获取所有用户", description = "获取系统中所有用户的列表")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取用户", description = "通过用户ID获取特定用户信息")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "用户ID", required = true) 
            @PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名获取用户", description = "通过用户名获取特定用户信息")
    public ResponseEntity<User> getUserByUsername(
            @Parameter(description = "用户名", required = true) 
            @PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "创建用户", description = "在系统中创建一个新用户")
    public ResponseEntity<User> createUser(
            @Parameter(description = "用户信息", required = true) 
            @RequestBody User user) {
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(userService.saveUser(user));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新用户", description = "更新指定ID的用户信息")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "用户ID", required = true) 
            @PathVariable Long id,
            @Parameter(description = "更新的用户信息", required = true) 
            @RequestBody User user) {
        user.setId(id);
        // 如果密码字段存在且不为空，则加密（编辑时不修改密码则不传password字段）
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // 保持原密码
            User existingUser = userService.getUserById(id).orElse(null);
            if (existingUser != null) {
                user.setPassword(existingUser.getPassword());
            }
        }
        return ResponseEntity.ok(userService.saveUser(user));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据ID删除用户")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "用户ID", required = true) 
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}