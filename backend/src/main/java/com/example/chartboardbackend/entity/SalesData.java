package com.example.chartboardbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sales_data")
@Data
public class SalesData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "sales_month", nullable = false)
    private String month;
    
    @Column(nullable = false)
    private Double sales;
}