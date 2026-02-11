package com.example.chartboardbackend.controller;

import com.example.chartboardbackend.dto.ApiResponse;
import com.example.chartboardbackend.entity.Position;
import com.example.chartboardbackend.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/positions")
@Tag(name = "岗位管理", description = "岗位信息管理接口")
public class PositionController {
    
    @Autowired
    private PositionService positionService;
    
    @GetMapping
    @Operation(summary = "获取所有岗位", description = "获取所有岗位列表")
    public ResponseEntity<ApiResponse<List<Position>>> getAllPositions() {
        List<Position> positions = positionService.getAllPositions();
        return ResponseEntity.ok(ApiResponse.success(positions));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取岗位", description = "根据岗位ID获取岗位详细信息")
    public ResponseEntity<ApiResponse<Position>> getPositionById(@PathVariable Long id) {
        Position position = positionService.getPositionById(id);
        return ResponseEntity.ok(ApiResponse.success(position));
    }
    
    @GetMapping("/code/{positionCode}")
    @Operation(summary = "根据编码获取岗位", description = "根据岗位编码获取岗位详细信息")
    public ResponseEntity<ApiResponse<Position>> getPositionByCode(@PathVariable String positionCode) {
        Position position = positionService.getPositionByCode(positionCode);
        return ResponseEntity.ok(ApiResponse.success(position));
    }
    
    @GetMapping("/department/{deptId}")
    @Operation(summary = "根据部门获取岗位", description = "获取指定部门的所有岗位")
    public ResponseEntity<ApiResponse<List<Position>>> getPositionsByDepartment(@PathVariable Long deptId) {
        List<Position> positions = positionService.getPositionsByDepartment(deptId);
        return ResponseEntity.ok(ApiResponse.success(positions));
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取岗位", description = "获取指定状态的所有岗位")
    public ResponseEntity<ApiResponse<List<Position>>> getPositionsByStatus(@PathVariable Integer status) {
        List<Position> positions = positionService.getPositionsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(positions));
    }
    
    @PostMapping
    @Operation(summary = "创建岗位", description = "新增岗位信息")
    public ResponseEntity<ApiResponse<Position>> createPosition(@RequestBody Position position) {
        Position createdPosition = positionService.createPosition(position);
        return ResponseEntity.ok(ApiResponse.success("岗位创建成功", createdPosition));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新岗位", description = "更新岗位信息")
    public ResponseEntity<ApiResponse<Position>> updatePosition(
            @PathVariable Long id, 
            @RequestBody Position position) {
        Position updatedPosition = positionService.updatePosition(id, position);
        return ResponseEntity.ok(ApiResponse.success("岗位更新成功", updatedPosition));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除岗位", description = "删除岗位信息")
    public ResponseEntity<ApiResponse<Void>> deletePosition(@PathVariable Long id) {
        positionService.deletePosition(id);
        return ResponseEntity.ok(ApiResponse.success("岗位删除成功", null));
    }
}
