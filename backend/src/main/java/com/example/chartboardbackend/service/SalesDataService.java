package com.example.chartboardbackend.service;

import com.example.chartboardbackend.entity.SalesData;
import com.example.chartboardbackend.repository.SalesDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesDataService {
    
    private final SalesDataRepository salesDataRepository;
    
    public List<SalesData> getAllSalesData() {
        return salesDataRepository.findAll();
    }
    
    public Optional<SalesData> getSalesDataById(Long id) {
        return salesDataRepository.findById(id);
    }
    
    public SalesData saveSalesData(SalesData salesData) {
        return salesDataRepository.save(salesData);
    }
    
    public void deleteSalesData(Long id) {
        salesDataRepository.deleteById(id);
    }
}