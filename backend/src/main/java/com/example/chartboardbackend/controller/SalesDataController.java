package com.example.chartboardbackend.controller;

import com.example.chartboardbackend.dto.ApiResponse;
import com.example.chartboardbackend.entity.SalesData;
import com.example.chartboardbackend.service.SalesDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Tag(name = "销售数据管理", description = "销售数据相关的API接口")
public class SalesDataController {
    
    private final SalesDataService salesDataService;
    
    @GetMapping
    @Operation(summary = "获取所有销售数据", description = "获取系统中所有销售数据的列表")
    public ResponseEntity<ApiResponse<List<SalesData>>> getAllSalesData() {
        List<SalesData> data = salesDataService.getAllSalesData();
        return ResponseEntity.ok(ApiResponse.success(data));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取销售数据", description = "通过销售数据ID获取特定销售数据信息")
    public ResponseEntity<ApiResponse<SalesData>> getSalesDataById(
            @Parameter(description = "销售数据ID", required = true) 
            @PathVariable Long id) {
        return salesDataService.getSalesDataById(id)
                .map(data -> ResponseEntity.ok(ApiResponse.success(data)))
                .orElse(ResponseEntity.status(404).body(ApiResponse.error(404, "销售数据不存在")));
    }
    
    @PostMapping
    @Operation(summary = "创建销售数据", description = "在系统中创建新的销售数据")
    public ResponseEntity<ApiResponse<SalesData>> createSalesData(
            @Parameter(description = "销售数据信息", required = true) 
            @RequestBody SalesData salesData) {
        SalesData saved = salesDataService.saveSalesData(salesData);
        return ResponseEntity.ok(ApiResponse.success("创建成功", saved));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新销售数据", description = "更新指定ID的销售数据信息")
    public ResponseEntity<ApiResponse<SalesData>> updateSalesData(
            @Parameter(description = "销售数据ID", required = true) 
            @PathVariable Long id,
            @Parameter(description = "更新的销售数据信息", required = true) 
            @RequestBody SalesData salesData) {
        salesData.setId(id);
        SalesData updated = salesDataService.saveSalesData(salesData);
        return ResponseEntity.ok(ApiResponse.success("更新成功", updated));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除销售数据", description = "根据ID删除销售数据")
    public ResponseEntity<ApiResponse<Void>> deleteSalesData(
            @Parameter(description = "销售数据ID", required = true) 
            @PathVariable Long id) {
        salesDataService.deleteSalesData(id);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }
}