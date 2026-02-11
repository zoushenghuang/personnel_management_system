package com.example.chartboardbackend.repository;

import com.example.chartboardbackend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    Optional<Employee> findByEmpNo(String empNo);
    
    List<Employee> findByDeptId(Long deptId);
    
    List<Employee> findByPositionId(Long positionId);
    
    List<Employee> findByStatus(Integer status);
    
    boolean existsByEmpNo(String empNo);
    
    @Query("SELECT e FROM Employee e WHERE " +
           "(:name IS NULL OR e.name LIKE %:name%) AND " +
           "(:deptId IS NULL OR e.deptId = :deptId) AND " +
           "(:status IS NULL OR e.status = :status)")
    List<Employee> searchEmployees(@Param("name") String name, 
                                   @Param("deptId") Long deptId, 
                                   @Param("status") Integer status);
    
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.status = 1")
    long countActiveEmployees();
}
