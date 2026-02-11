import axios from 'axios';
import type { Department, ApiResponse } from '../types';

const API_BASE_URL = '/api/departments';

export const departmentApi = {
  // 获取所有部门
  getAllDepartments: () => 
    axios.get<ApiResponse<Department[]>>(API_BASE_URL),

  // 获取部门树
  getDepartmentTree: () => 
    axios.get<ApiResponse<Department[]>>(`${API_BASE_URL}/tree`),

  // 根据ID获取部门
  getDepartmentById: (id: number) => 
    axios.get<ApiResponse<Department>>(`${API_BASE_URL}/${id}`),

  // 根据编码获取部门
  getDepartmentByCode: (deptCode: string) => 
    axios.get<ApiResponse<Department>>(`${API_BASE_URL}/code/${deptCode}`),

  // 获取子部门
  getChildDepartments: (parentId: number) => 
    axios.get<ApiResponse<Department[]>>(`${API_BASE_URL}/children/${parentId}`),

  // 创建部门
  createDepartment: (department: Department) => 
    axios.post<ApiResponse<Department>>(API_BASE_URL, department),

  // 更新部门
  updateDepartment: (id: number, department: Department) => 
    axios.put<ApiResponse<Department>>(`${API_BASE_URL}/${id}`, department),

  // 删除部门
  deleteDepartment: (id: number) => 
    axios.delete<ApiResponse<void>>(`${API_BASE_URL}/${id}`),
};
