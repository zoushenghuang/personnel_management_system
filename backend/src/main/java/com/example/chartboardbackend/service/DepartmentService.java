package com.example.chartboardbackend.service;

import com.example.chartboardbackend.dto.DepartmentTreeDTO;
import com.example.chartboardbackend.entity.Department;
import com.example.chartboardbackend.entity.Employee;
import com.example.chartboardbackend.repository.DepartmentRepository;
import com.example.chartboardbackend.repository.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    /**
     * 获取所有部门列表
     */
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    
    /**
     * 获取部门树形结构
     */
    public List<DepartmentTreeDTO> getDepartmentTree() {
        List<Department> allDepartments = departmentRepository.findAll();
        return buildTree(allDepartments, 0L);
    }
    
    /**
     * 根据ID获取部门
     */
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("部门不存在，ID: " + id));
    }
    
    /**
     * 根据部门编码获取部门
     */
    public Department getDepartmentByCode(String deptCode) {
        return departmentRepository.findByDeptCode(deptCode)
                .orElseThrow(() -> new RuntimeException("部门不存在，编码: " + deptCode));
    }
    
    /**
     * 获取子部门列表
     */
    public List<Department> getChildDepartments(Long parentId) {
        return departmentRepository.findByParentId(parentId);
    }
    
    /**
     * 创建部门
     */
    @Transactional
    public Department createDepartment(Department department) {
        // 检查部门编码是否已存在
        if (department.getDeptCode() != null 
                && departmentRepository.existsByDeptCode(department.getDeptCode())) {
            throw new RuntimeException("部门编码已存在: " + department.getDeptCode());
        }
        
        // 验证上级部门是否存在
        if (department.getParentId() != null && department.getParentId() != 0) {
            departmentRepository.findById(department.getParentId())
                    .orElseThrow(() -> new RuntimeException("上级部门不存在，ID: " + department.getParentId()));
        }
        
        return departmentRepository.save(department);
    }
    
    /**
     * 更新部门
     */
    @Transactional
    public Department updateDepartment(Long id, Department department) {
        Department existingDept = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("部门不存在，ID: " + id));
        
        // 检查部门编码是否被其他部门使用
        if (department.getDeptCode() != null 
                && !department.getDeptCode().equals(existingDept.getDeptCode())
                && departmentRepository.existsByDeptCode(department.getDeptCode())) {
            throw new RuntimeException("部门编码已存在: " + department.getDeptCode());
        }
        
        // 不能将部门的上级设置为自己或自己的子部门
        if (department.getParentId() != null && department.getParentId().equals(id)) {
            throw new RuntimeException("不能将部门的上级设置为自己");
        }
        
        // 验证上级部门是否存在
        if (department.getParentId() != null && department.getParentId() != 0) {
            departmentRepository.findById(department.getParentId())
                    .orElseThrow(() -> new RuntimeException("上级部门不存在，ID: " + department.getParentId()));
        }
        
        // 复制属性，保留ID和创建时间
        BeanUtils.copyProperties(department, existingDept, "id", "createTime");
        
        return departmentRepository.save(existingDept);
    }
    
    /**
     * 删除部门
     */
    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("部门不存在，ID: " + id));
        
        // 检查是否有子部门
        List<Department> children = departmentRepository.findByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("该部门下有子部门，无法删除");
        }
        
        // 检查是否有员工
        List<Employee> employees = employeeRepository.findByDeptId(id);
        if (!employees.isEmpty()) {
            throw new RuntimeException("该部门下有员工，无法删除");
        }
        
        departmentRepository.deleteById(id);
    }
    
    /**
     * 构建部门树
     */
    private List<DepartmentTreeDTO> buildTree(List<Department> departments, Long parentId) {
        List<DepartmentTreeDTO> tree = new ArrayList<>();
        
        for (Department dept : departments) {
            if (dept.getParentId().equals(parentId)) {
                DepartmentTreeDTO node = convertToTreeDTO(dept);
                node.setChildren(buildTree(departments, dept.getId()));
                tree.add(node);
            }
        }
        
        return tree;
    }
    
    /**
     * 转换为树形DTO
     */
    private DepartmentTreeDTO convertToTreeDTO(Department department) {
        DepartmentTreeDTO dto = new DepartmentTreeDTO();
        BeanUtils.copyProperties(department, dto);
        
        // 设置负责人姓名
        if (department.getManagerId() != null) {
            employeeRepository.findById(department.getManagerId())
                    .ifPresent(emp -> dto.setManagerName(emp.getName()));
        }
        
        return dto;
    }
}
