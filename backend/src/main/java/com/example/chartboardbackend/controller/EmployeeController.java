package com.example.chartboardbackend.controller;

import com.example.chartboardbackend.dto.ApiResponse;
import com.example.chartboardbackend.dto.EmployeeDTO;
import com.example.chartboardbackend.entity.Employee;
import com.example.chartboardbackend.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "员工管理", description = "员工信息管理接口")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @GetMapping
    @Operation(summary = "获取所有员工", description = "获取所有员工列表")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(ApiResponse.success(employees));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取员工", description = "根据员工ID获取员工详细信息")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeById(@PathVariable Long id) {
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ApiResponse.success(employee));
    }
    
    @GetMapping("/empNo/{empNo}")
    @Operation(summary = "根据工号获取员工", description = "根据员工工号获取员工详细信息")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeByEmpNo(@PathVariable String empNo) {
        EmployeeDTO employee = employeeService.getEmployeeByEmpNo(empNo);
        return ResponseEntity.ok(ApiResponse.success(employee));
    }
    
    @GetMapping("/search")
    @Operation(summary = "搜索员工", description = "根据条件搜索员工")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) Integer status) {
        List<EmployeeDTO> employees = employeeService.searchEmployees(name, deptId, status);
        return ResponseEntity.ok(ApiResponse.success(employees));
    }
    
    @GetMapping("/department/{deptId}")
    @Operation(summary = "根据部门获取员工", description = "获取指定部门的所有员工")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getEmployeesByDepartment(@PathVariable Long deptId) {
        List<EmployeeDTO> employees = employeeService.getEmployeesByDepartment(deptId);
        return ResponseEntity.ok(ApiResponse.success(employees));
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取员工", description = "获取指定状态的所有员工")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getEmployeesByStatus(@PathVariable Integer status) {
        List<EmployeeDTO> employees = employeeService.getEmployeesByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(employees));
    }
    
    @GetMapping("/count/active")
    @Operation(summary = "获取在职员工数量", description = "获取当前在职员工总数")
    public ResponseEntity<ApiResponse<Long>> getActiveEmployeeCount() {
        long count = employeeService.getActiveEmployeeCount();
        return ResponseEntity.ok(ApiResponse.success(count));
    }
    
    @PostMapping
    @Operation(summary = "创建员工", description = "新增员工信息")
    public ResponseEntity<ApiResponse<EmployeeDTO>> createEmployee(@RequestBody Employee employee) {
        EmployeeDTO createdEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.ok(ApiResponse.success("员工创建成功", createdEmployee));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新员工", description = "更新员工信息")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateEmployee(
            @PathVariable Long id, 
            @RequestBody Employee employee) {
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(ApiResponse.success("员工更新成功", updatedEmployee));
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "更新员工状态", description = "更新员工在职状态")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateEmployeeStatus(
            @PathVariable Long id, 
            @RequestParam Integer status) {
        EmployeeDTO updatedEmployee = employeeService.updateEmployeeStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("员工状态更新成功", updatedEmployee));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除员工", description = "删除员工信息")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("员工删除成功", null));
    }
}
