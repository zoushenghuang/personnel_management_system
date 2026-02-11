package com.example.chartboardbackend.repository;

import com.example.chartboardbackend.entity.SalesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesDataRepository extends JpaRepository<SalesData, Long> {
}