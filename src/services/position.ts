import axios from 'axios';
import type { Position, ApiResponse } from '../types';

const API_BASE_URL = '/api/positions';

export const positionApi = {
  // 获取所有岗位
  getAllPositions: () => 
    axios.get<ApiResponse<Position[]>>(API_BASE_URL),

  // 根据ID获取岗位
  getPositionById: (id: number) => 
    axios.get<ApiResponse<Position>>(`${API_BASE_URL}/${id}`),

  // 根据编码获取岗位
  getPositionByCode: (positionCode: string) => 
    axios.get<ApiResponse<Position>>(`${API_BASE_URL}/code/${positionCode}`),

  // 根据部门获取岗位
  getPositionsByDepartment: (deptId: number) => 
    axios.get<ApiResponse<Position[]>>(`${API_BASE_URL}/department/${deptId}`),

  // 根据状态获取岗位
  getPositionsByStatus: (status: number) => 
    axios.get<ApiResponse<Position[]>>(`${API_BASE_URL}/status/${status}`),

  // 创建岗位
  createPosition: (position: Position) => 
    axios.post<ApiResponse<Position>>(API_BASE_URL, position),

  // 更新岗位
  updatePosition: (id: number, position: Position) => 
    axios.put<ApiResponse<Position>>(`${API_BASE_URL}/${id}`, position),

  // 删除岗位
  deletePosition: (id: number) => 
    axios.delete<ApiResponse<void>>(`${API_BASE_URL}/${id}`),
};
