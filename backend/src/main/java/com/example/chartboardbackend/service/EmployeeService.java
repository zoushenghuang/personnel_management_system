package com.example.chartboardbackend.service;

import com.example.chartboardbackend.dto.EmployeeDTO;
import com.example.chartboardbackend.entity.Department;
import com.example.chartboardbackend.entity.Employee;
import com.example.chartboardbackend.entity.Position;
import com.example.chartboardbackend.repository.DepartmentRepository;
import com.example.chartboardbackend.repository.EmployeeRepository;
import com.example.chartboardbackend.repository.PositionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private PositionRepository positionRepository;
    
    /**
     * 获取所有员工列表
     */
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据ID获取员工
     */
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("员工不存在，ID: " + id));
        return convertToDTO(employee);
    }
    
    /**
     * 根据工号获取员工
     */
    public EmployeeDTO getEmployeeByEmpNo(String empNo) {
        Employee employee = employeeRepository.findByEmpNo(empNo)
                .orElseThrow(() -> new RuntimeException("员工不存在，工号: " + empNo));
        return convertToDTO(employee);
    }
    
    /**
     * 搜索员工
     */
    public List<EmployeeDTO> searchEmployees(String name, Long deptId, Integer status) {
        List<Employee> employees = employeeRepository.searchEmployees(name, deptId, status);
        return employees.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据部门获取员工
     */
    public List<EmployeeDTO> getEmployeesByDepartment(Long deptId) {
        List<Employee> employees = employeeRepository.findByDeptId(deptId);
        return employees.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据状态获取员工
     */
    public List<EmployeeDTO> getEmployeesByStatus(Integer status) {
        List<Employee> employees = employeeRepository.findByStatus(status);
        return employees.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 创建员工
     */
    @Transactional
    public EmployeeDTO createEmployee(Employee employee) {
        // 检查工号是否已存在
        if (employeeRepository.existsByEmpNo(employee.getEmpNo())) {
            throw new RuntimeException("工号已存在: " + employee.getEmpNo());
        }
        
        // 验证部门是否存在
        if (employee.getDeptId() != null) {
            departmentRepository.findById(employee.getDeptId())
                    .orElseThrow(() -> new RuntimeException("部门不存在，ID: " + employee.getDeptId()));
        }
        
        // 验证岗位是否存在
        if (employee.getPositionId() != null) {
            positionRepository.findById(employee.getPositionId())
                    .orElseThrow(() -> new RuntimeException("岗位不存在，ID: " + employee.getPositionId()));
        }
        
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDTO(savedEmployee);
    }
    
    /**
     * 更新员工
     */
    @Transactional
    public EmployeeDTO updateEmployee(Long id, Employee employee) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("员工不存在，ID: " + id));
        
        // 检查工号是否被其他员工使用
        if (!existingEmployee.getEmpNo().equals(employee.getEmpNo()) 
                && employeeRepository.existsByEmpNo(employee.getEmpNo())) {
            throw new RuntimeException("工号已存在: " + employee.getEmpNo());
        }
        
        // 验证部门是否存在
        if (employee.getDeptId() != null) {
            departmentRepository.findById(employee.getDeptId())
                    .orElseThrow(() -> new RuntimeException("部门不存在，ID: " + employee.getDeptId()));
        }
        
        // 验证岗位是否存在
        if (employee.getPositionId() != null) {
            positionRepository.findById(employee.getPositionId())
                    .orElseThrow(() -> new RuntimeException("岗位不存在，ID: " + employee.getPositionId()));
        }
        
        // 复制属性，保留ID和创建时间
        BeanUtils.copyProperties(employee, existingEmployee, "id", "createTime");
        
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return convertToDTO(updatedEmployee);
    }
    
    /**
     * 删除员工
     */
    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("员工不存在，ID: " + id);
        }
        employeeRepository.deleteById(id);
    }
    
    /**
     * 更新员工状态
     */
    @Transactional
    public EmployeeDTO updateEmployeeStatus(Long id, Integer status) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("员工不存在，ID: " + id));
        
        employee.setStatus(status);
        Employee updatedEmployee = employeeRepository.save(employee);
        return convertToDTO(updatedEmployee);
    }
    
    /**
     * 获取在职员工数量
     */
    public long getActiveEmployeeCount() {
        return employeeRepository.countActiveEmployees();
    }
    
    /**
     * 转换为DTO
     */
    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(employee, dto);
        
        // 设置部门名称
        if (employee.getDeptId() != null) {
            departmentRepository.findById(employee.getDeptId())
                    .ifPresent(dept -> dto.setDeptName(dept.getDeptName()));
        }
        
        // 设置岗位名称
        if (employee.getPositionId() != null) {
            positionRepository.findById(employee.getPositionId())
                    .ifPresent(pos -> dto.setPositionName(pos.getPositionName()));
        }
        
        // 设置状态文本
        dto.setStatusText(getStatusText(employee.getStatus()));
        
        return dto;
    }
    
    /**
     * 获取状态文本
     */
    private String getStatusText(Integer status) {
        if (status == null) return "未知";
        return switch (status) {
            case 0 -> "离职";
            case 1 -> "在职";
            case 2 -> "试用期";
            default -> "未知";
        };
    }
}
