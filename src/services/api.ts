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
    // 如果是登录接口的401错误，不要自动跳转，让调用方处理
    const isLoginRequest = error.config?.url?.includes('/api/auth/login');
    
    if (error.response?.status === 401 && !isLoginRequest) {
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

export default api;