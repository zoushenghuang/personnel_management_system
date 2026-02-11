// API基础路径
export const API_BASE_URL = '';

// 路由路径
export const ROUTES = {
  LOGIN: '/login',
  HOME: '/',
  EMPLOYEES: '/employees',
  USER_MANAGE: '/users',
} as const;

// 用户角色
export const USER_ROLES = {
  ADMIN: 'ADMIN',
  USER: 'USER',
} as const;

// 本地存储键名
export const STORAGE_KEYS = {
  AUTH: 'auth-storage',
} as const;

// Token过期时间（毫秒）
export const TOKEN_EXPIRE_TIME = 5 * 60 * 1000; // 5分钟

// 消息提示
export const MESSAGES = {
  LOGIN_SUCCESS: '登录成功',
  LOGIN_FAILED: '登录失败',
  LOGOUT_SUCCESS: '退出成功',
  TOKEN_EXPIRED: '登录已过期，请重新登录',
  OPERATION_SUCCESS: '操作成功',
  OPERATION_FAILED: '操作失败',
} as const;
