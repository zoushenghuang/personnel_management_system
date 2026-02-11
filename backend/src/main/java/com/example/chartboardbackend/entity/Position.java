package com.example.chartboardbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "hr_position")
public class Position {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "position_name", nullable = false, length = 100)
    private String positionName;
    
    @Column(name = "position_code", unique = true, length = 50)
    private String positionCode;
    
    @Column(name = "dept_id")
    private Long deptId;
    
    @Column(length = 20)
    private String level;
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer status = 1;
    
    @Column(length = 500)
    private String description;
    
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
