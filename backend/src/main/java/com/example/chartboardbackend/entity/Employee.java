package com.example.chartboardbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "hr_employee")
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "emp_no", nullable = false, unique = true, length = 50)
    private String empNo;
    
    @Column(nullable = false, length = 50)
    private String name;
    
    @Column(columnDefinition = "TINYINT")
    private Integer gender;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Column(name = "id_card", length = 18)
    private String idCard;
    
    @Column(length = 20)
    private String phone;
    
    @Column(length = 100)
    private String email;
    
    @Column(name = "dept_id")
    private Long deptId;
    
    @Column(name = "position_id")
    private Long positionId;
    
    @Column(name = "hire_date")
    private LocalDate hireDate;
    
    @Column(name = "leave_date")
    private LocalDate leaveDate;
    
    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer status = 1;
    
    @Column(length = 20)
    private String education;
    
    @Column(length = 100)
    private String major;
    
    @Column(length = 200)
    private String address;
    
    @Column(name = "emergency_contact", length = 50)
    private String emergencyContact;
    
    @Column(name = "emergency_phone", length = 20)
    private String emergencyPhone;
    
    @Column(name = "photo_url", length = 255)
    private String photoUrl;
    
    @Column(length = 500)
    private String remark;
    
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;
    
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
