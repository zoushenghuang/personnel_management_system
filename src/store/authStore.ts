import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { login as apiLogin, validateToken } from '../services/api';

interface AuthState {
  isAuthenticated: boolean;
  user: { username: string; role: string } | null;
  token: string | null;
  login: (username: string, password: string) => Promise<{ success: boolean; message?: string }>;
  logout: () => void;
  checkAuth: () => Promise<boolean>;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      isAuthenticated: false,
      user: null,
      token: null,
      login: async (username, password) => {
        try {
          const response = await apiLogin(username, password);
          if (response.user && response.token) {
            set({ 
              isAuthenticated: true, 
              user: { 
                username: response.user.username, 
                role: response.user.role 
              },
              token: response.token
            });
            return { success: true };
          } else {
            return { success: false, message: response.message || '登录失败' };
          }
        } catch (error: any) {
          console.error('Login error:', error);
          // 优先使用后端返回的错误信息
          const errorMessage = error.response?.data?.message 
            || error.message 
            || '登录失败，请检查网络连接';
          return { 
            success: false, 
            message: errorMessage
          };
        }
      },
      logout: () => {
        set({ isAuthenticated: false, user: null, token: null });
      },
      checkAuth: async () => {
        const { token } = get();
        if (!token) {
          set({ isAuthenticated: false, user: null, token: null });
          return false;
        }
        
        try {
          const response = await validateToken(token);
          if (response.valid) {
            set({ 
              isAuthenticated: true,
              user: {
                username: response.username,
                role: response.role
              }
            });
            return true;
          } else {
            set({ isAuthenticated: false, user: null, token: null });
            return false;
          }
        } catch (error) {
          set({ isAuthenticated: false, user: null, token: null });
          return false;
        }
      },
    }),
    {
      name: 'auth-storage',
    }
  )
);