package com.example.chartboardbackend.controller;

import com.example.chartboardbackend.dto.ApiResponse;
import com.example.chartboardbackend.dto.DepartmentTreeDTO;
import com.example.chartboardbackend.entity.Department;
import com.example.chartboardbackend.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@Tag(name = "部门管理", description = "部门信息管理接口")
public class DepartmentController {
    
    @Autowired
    private DepartmentService departmentService;
    
    @GetMapping
    @Operation(summary = "获取所有部门", description = "获取所有部门列表")
    public ResponseEntity<ApiResponse<List<Department>>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success(departments));
    }
    
    @GetMapping("/tree")
    @Operation(summary = "获取部门树", description = "获取部门树形结构")
    public ResponseEntity<ApiResponse<List<DepartmentTreeDTO>>> getDepartmentTree() {
        List<DepartmentTreeDTO> tree = departmentService.getDepartmentTree();
        return ResponseEntity.ok(ApiResponse.success(tree));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取部门", description = "根据部门ID获取部门详细信息")
    public ResponseEntity<ApiResponse<Department>> getDepartmentById(@PathVariable Long id) {
        Department department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success(department));
    }
    
    @GetMapping("/code/{deptCode}")
    @Operation(summary = "根据编码获取部门", description = "根据部门编码获取部门详细信息")
    public ResponseEntity<ApiResponse<Department>> getDepartmentByCode(@PathVariable String deptCode) {
        Department department = departmentService.getDepartmentByCode(deptCode);
        return ResponseEntity.ok(ApiResponse.success(department));
    }
    
    @GetMapping("/children/{parentId}")
    @Operation(summary = "获取子部门", description = "获取指定部门的所有子部门")
    public ResponseEntity<ApiResponse<List<Department>>> getChildDepartments(@PathVariable Long parentId) {
        List<Department> children = departmentService.getChildDepartments(parentId);
        return ResponseEntity.ok(ApiResponse.success(children));
    }
    
    @PostMapping
    @Operation(summary = "创建部门", description = "新增部门信息")
    public ResponseEntity<ApiResponse<Department>> createDepartment(@RequestBody Department department) {
        Department createdDepartment = departmentService.createDepartment(department);
        return ResponseEntity.ok(ApiResponse.success("部门创建成功", createdDepartment));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新部门", description = "更新部门信息")
    public ResponseEntity<ApiResponse<Department>> updateDepartment(
            @PathVariable Long id, 
            @RequestBody Department department) {
        Department updatedDepartment = departmentService.updateDepartment(id, department);
        return ResponseEntity.ok(ApiResponse.success("部门更新成功", updatedDepartment));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除部门", description = "删除部门信息")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("部门删除成功", null));
    }
}
