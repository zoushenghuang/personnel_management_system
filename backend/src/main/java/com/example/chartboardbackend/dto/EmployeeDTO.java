package com.example.chartboardbackend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EmployeeDTO {
    private Long id;
    private String empNo;
    private String name;
    private Integer gender;
    private LocalDate birthDate;
    private String idCard;
    private String phone;
    private String email;
    private Long deptId;
    private String deptName;
    private Long positionId;
    private String positionName;
    private LocalDate hireDate;
    private LocalDate leaveDate;
    private Integer status;
    private String statusText;
    private String education;
    private String major;
    private String address;
    private String emergencyContact;
    private String emergencyPhone;
    private String photoUrl;
    private String remark;
}
