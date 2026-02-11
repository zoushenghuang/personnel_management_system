import axios from 'axios';
import type { Employee, ApiResponse } from '../types';

const API_BASE_URL = '/api/employees';

export const employeeApi = {
  // 获取所有员工
  getAllEmployees: () => 
    axios.get<ApiResponse<Employee[]>>(API_BASE_URL),

  // 根据ID获取员工
  getEmployeeById: (id: number) => 
    axios.get<ApiResponse<Employee>>(`${API_BASE_URL}/${id}`),

  // 根据工号获取员工
  getEmployeeByEmpNo: (empNo: string) => 
    axios.get<ApiResponse<Employee>>(`${API_BASE_URL}/empNo/${empNo}`),

  // 搜索员工
  searchEmployees: (params: { name?: string; deptId?: number; status?: number }) => 
    axios.get<ApiResponse<Employee[]>>(`${API_BASE_URL}/search`, { params }),

  // 根据部门获取员工
  getEmployeesByDepartment: (deptId: number) => 
    axios.get<ApiResponse<Employee[]>>(`${API_BASE_URL}/department/${deptId}`),

  // 根据状态获取员工
  getEmployeesByStatus: (status: number) => 
    axios.get<ApiResponse<Employee[]>>(`${API_BASE_URL}/status/${status}`),

  // 获取在职员工数量
  getActiveEmployeeCount: () => 
    axios.get<ApiResponse<number>>(`${API_BASE_URL}/count/active`),

  // 创建员工
  createEmployee: (employee: Employee) => 
    axios.post<ApiResponse<Employee>>(API_BASE_URL, employee),

  // 更新员工
  updateEmployee: (id: number, employee: Employee) => 
    axios.put<ApiResponse<Employee>>(`${API_BASE_URL}/${id}`, employee),

  // 更新员工状态
  updateEmployeeStatus: (id: number, status: number) => 
    axios.patch<ApiResponse<Employee>>(`${API_BASE_URL}/${id}/status`, null, {
      params: { status }
    }),

  // 删除员工
  deleteEmployee: (id: number) => 
    axios.delete<ApiResponse<void>>(`${API_BASE_URL}/${id}`),
};
