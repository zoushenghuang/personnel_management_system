import axios from 'axios';
import { useAuthStore } from '../store/authStore';

// 创建axios实例（使用代理，不需要指定完整URL）
const api = axios.create({
  baseURL: '', // 使用Vite代理
  headers: {
    'Content-Type': 'application/json',
  },
});

// 请求拦截器：添加token
api.interceptors.request.use(
  (config) => {
    const token = useAuthStore.getState().token;
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器：处理统一响应格式和token过期
api.interceptors.response.use(
  (response) => {
    // 如果响应有统一格式，提取data字段
    if (response.data && typeof response.data === 'object' && 'code' in response.data) {
      if (response.data.code === 200) {
        return { ...response, data: response.data.data };
      } else {
        return Promise.reject(new Error(response.data.message || '请求失败'));
      }
    }
    return response;
  },
  (error) => {
    if (error.response?.status === 401) {
      useAuthStore.getState().logout();
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// 登录请求
export const login = async (username: string, password: string) => {
  const response = await api.post('/api/auth/login', { username, password });
  return response.data;
};

// 验证Token
export const validateToken = async (token: string) => {
  const response = await axios.post('/api/auth/validate', {}, {
    headers: { Authorization: `Bearer ${token}` }
  });
  return response.data;
};

// 修改密码
export const changePassword = async (oldPassword: string, newPassword: string) => {
  const response = await api.post('/api/auth/change-password', {
    oldPassword,
    newPassword,
  });
  return response.data;
};

// 获取销售数据
export const getSalesData = async () => {
  const response = await api.get('/api/sales');
  return response.data;
};

// 获取流量来源数据
export const getTrafficSources = async () => {
  const response = await api.get('/api/dashboard/traffic-sources');
  return response.data;
};

// 获取访问数据
export const getVisitData = async () => {
  const response = await api.get('/api/dashboard/visit-data');
  return response.data;
};

// 获取产品销售数据
export const getProductSales = async () => {
  const response = await api.get('/api/dashboard/product-sales');
  return response.data;
};

// 获取流量来源详情
export const getTrafficSourceDetail = async (id: number) => {
  const response = await api.get(`/api/dashboard/traffic-sources/${id}`);
  return response.data;
};

// 大屏数据接口
export const getBigScreenStats = async () => {
  const response = await api.get('/api/bigscreen/stats');
  return response.data;
};

export const getIndustryDistribution = async () => {
  const response = await api.get('/api/bigscreen/industry-distribution');
  return response.data;
};

export const getProvinceDistribution = async () => {
  const response = await api.get('/api/bigscreen/province-distribution');
  return response.data;
};

export const getCityTrend = async () => {
  const response = await api.get('/api/bigscreen/city-trend');
  return response.data;
};

export const getIndustryAnalysis = async () => {
  const response = await api.get('/api/bigscreen/industry-analysis');
  return response.data;
};

// 系统监控接口
export const getSystemInfo = async () => {
  const response = await api.get('/api/system/info');
  return response.data;
};

export const getCpuInfo = async () => {
  const response = await api.get('/api/system/cpu');
  return response.data;
};

export const getMemoryInfo = async () => {
  const response = await api.get('/api/system/memory');
  return response.data;
};

export const getDiskInfo = async () => {
  const response = await api.get('/api/system/disk');
  return response.data;
};

export default api;