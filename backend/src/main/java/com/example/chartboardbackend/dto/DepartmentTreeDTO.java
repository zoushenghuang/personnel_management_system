package com.example.chartboardbackend.dto;

import lombok.Data;
import java.util.List;

@Data
public class DepartmentTreeDTO {
    private Long id;
    private String deptName;
    private String deptCode;
    private Long parentId;
    private Long managerId;
    private String managerName;
    private String phone;
    private String email;
    private Integer sortOrder;
    private Integer status;
    private String remark;
    private List<DepartmentTreeDTO> children;
}
