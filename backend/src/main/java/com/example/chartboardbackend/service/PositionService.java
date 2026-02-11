package com.example.chartboardbackend.service;

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

@Service
public class PositionService {
    
    @Autowired
    private PositionRepository positionRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    /**
     * 获取所有岗位列表
     */
    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }
    
    /**
     * 根据ID获取岗位
     */
    public Position getPositionById(Long id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("岗位不存在，ID: " + id));
    }
    
    /**
     * 根据岗位编码获取岗位
     */
    public Position getPositionByCode(String positionCode) {
        return positionRepository.findByPositionCode(positionCode)
                .orElseThrow(() -> new RuntimeException("岗位不存在，编码: " + positionCode));
    }
    
    /**
     * 根据部门获取岗位列表
     */
    public List<Position> getPositionsByDepartment(Long deptId) {
        return positionRepository.findByDeptId(deptId);
    }
    
    /**
     * 根据状态获取岗位列表
     */
    public List<Position> getPositionsByStatus(Integer status) {
        return positionRepository.findByStatus(status);
    }
    
    /**
     * 创建岗位
     */
    @Transactional
    public Position createPosition(Position position) {
        // 检查岗位编码是否已存在
        if (position.getPositionCode() != null 
                && positionRepository.existsByPositionCode(position.getPositionCode())) {
            throw new RuntimeException("岗位编码已存在: " + position.getPositionCode());
        }
        
        // 验证部门是否存在
        if (position.getDeptId() != null) {
            departmentRepository.findById(position.getDeptId())
                    .orElseThrow(() -> new RuntimeException("部门不存在，ID: " + position.getDeptId()));
        }
        
        return positionRepository.save(position);
    }
    
    /**
     * 更新岗位
     */
    @Transactional
    public Position updatePosition(Long id, Position position) {
        Position existingPosition = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("岗位不存在，ID: " + id));
        
        // 检查岗位编码是否被其他岗位使用
        if (position.getPositionCode() != null 
                && !position.getPositionCode().equals(existingPosition.getPositionCode())
                && positionRepository.existsByPositionCode(position.getPositionCode())) {
            throw new RuntimeException("岗位编码已存在: " + position.getPositionCode());
        }
        
        // 验证部门是否存在
        if (position.getDeptId() != null) {
            departmentRepository.findById(position.getDeptId())
                    .orElseThrow(() -> new RuntimeException("部门不存在，ID: " + position.getDeptId()));
        }
        
        // 复制属性，保留ID和创建时间
        BeanUtils.copyProperties(position, existingPosition, "id", "createTime");
        
        return positionRepository.save(existingPosition);
    }
    
    /**
     * 删除岗位
     */
    @Transactional
    public void deletePosition(Long id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("岗位不存在，ID: " + id));
        
        // 检查是否有员工使用该岗位
        List<Employee> employees = employeeRepository.findByPositionId(id);
        if (!employees.isEmpty()) {
            throw new RuntimeException("该岗位下有员工，无法删除");
        }
        
        positionRepository.deleteById(id);
    }
}
