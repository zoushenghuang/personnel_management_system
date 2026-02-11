package com.example.chartboardbackend.controller;

import com.example.chartboardbackend.dto.ApiResponse;
import com.example.chartboardbackend.repository.EmployeeRepository;
import com.example.chartboardbackend.repository.DepartmentRepository;
import com.example.chartboardbackend.repository.PositionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "数据概览", description = "人事管理系统数据概览API")
public class DashboardController {
    
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    
    @GetMapping("/statistics")
    @Operation(summary = "获取统计数据", description = "获取人事管理系统的统计数据")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 员工统计
        long totalEmployees = employeeRepository.count();
        long activeEmployees = employeeRepository.countActiveEmployees();
        long inactiveEmployees = totalEmployees - activeEmployees;
        
        // 部门和岗位统计
        long totalDepartments = departmentRepository.count();
        long totalPositions = positionRepository.count();
        
        statistics.put("totalEmployees", totalEmployees);
        statistics.put("activeEmployees", activeEmployees);
        statistics.put("inactiveEmployees", inactiveEmployees);
        statistics.put("totalDepartments", totalDepartments);
        statistics.put("totalPositions", totalPositions);
        
        return ResponseEntity.ok(ApiResponse.success(statistics));
    }
}
