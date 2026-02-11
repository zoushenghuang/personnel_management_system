import React from 'react';
import { Navigate, type RouteObject } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';
import LoginPage from '../pages/Login';
import MainLayout from '../components/Layout/MainLayout';
import DashboardPage from '../pages/Dashboard';
import UserManagePage from '../pages/UserManage';
import EmployeePage from '../pages/Employee';

// 认证保护组件
const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated } = useAuthStore();
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" replace />;
};

// 路由配置
export const routes: RouteObject[] = [
  {
    path: '/login',
    element: <LoginPage />,
  },
  {
    path: '/',
    element: (
      <ProtectedRoute>
        <MainLayout />
      </ProtectedRoute>
    ),
    children: [
      {
        index: true,
        element: <DashboardPage />,
      },
      {
        path: 'employees',
        element: <EmployeePage />,
      },
      {
        path: 'users',
        element: <UserManagePage />,
      },
    ],
  },
  {
    path: '*',
    element: <Navigate to="/login" replace />,
  },
];
