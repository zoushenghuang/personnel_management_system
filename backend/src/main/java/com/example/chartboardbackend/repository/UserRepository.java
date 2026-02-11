package com.example.chartboardbackend.repository;

import com.example.chartboardbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
    @Query(value = "SELECT r.role_code FROM sys_user u " +
                   "JOIN sys_user_role ur ON u.id = ur.user_id " +
                   "JOIN sys_role r ON ur.role_id = r.id " +
                   "WHERE u.username = :username LIMIT 1", 
           nativeQuery = true)
    String findRoleCodeByUsername(@Param("username") String username);
}