package com.example.chartboardbackend.repository;

import com.example.chartboardbackend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    List<Department> findByParentId(Long parentId);
    
    List<Department> findByStatus(Integer status);
    
    Optional<Department> findByDeptCode(String deptCode);
    
    boolean existsByDeptCode(String deptCode);
}
