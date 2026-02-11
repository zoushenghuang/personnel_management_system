import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Input, Button, Card, message } from 'antd';
import { UserOutlined, LockOutlined, BarChartOutlined } from '@ant-design/icons';
import { useAuthStore } from '../../store/authStore';
import { ROUTES, MESSAGES } from '../../constants';
import './index.less';

const LoginPage: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuthStore();

  const onFinish = async (values: { username: string; password: string }) => {
    setLoading(true);
    const result = await login(values.username, values.password);
    setLoading(false);

    if (result.success) {
      message.success(MESSAGES.LOGIN_SUCCESS);
      navigate(ROUTES.HOME);
    } else {
      message.error(result.message || MESSAGES.LOGIN_FAILED);
    }
  };

  return (
    <div className="login-page">
      {/* 背景装饰圆形 */}
      <div className="bg-decoration circle-1"></div>
      <div className="bg-decoration circle-2"></div>
      <div className="bg-decoration circle-3"></div>

      {/* 左侧欢迎信息（大屏显示） */}
      <div className="login-left">
        <h1 className="welcome-title">数据可视化平台</h1>
        <p className="welcome-subtitle">
          强大的数据分析与可视化工具，帮助您更好地理解和展示数据，做出明智的决策。
        </p>
        <ul className="feature-list">
          <li>实时数据监控与分析</li>
          <li>多维度数据可视化</li>
          <li>灵活的用户权限管理</li>
          <li>安全可靠的数据存储</li>
        </ul>
      </div>

      {/* 登录卡片 */}
      <Card className="login-card">
        <div className="login-header">
          <div className="logo-wrapper">
            <BarChartOutlined className="login-icon" />
          </div>
          <h1 className="login-title">欢迎登录</h1>
          <p className="login-subtitle">请输入您的账户信息</p>
        </div>

        <Form name="login" onFinish={onFinish} autoComplete="off" size="large" className="login-form">
          <Form.Item
            name="username"
            rules={[{ required: true, message: '请输入用户名!' }]}
          >
            <Input prefix={<UserOutlined />} placeholder="用户名" />
          </Form.Item>

          <Form.Item
            name="password"
            rules={[{ required: true, message: '请输入密码!' }]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="密码" />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" block loading={loading} className="login-button">
              立即登录
            </Button>
          </Form.Item>
        </Form>

        <div className="login-footer">
          <span className="test-account">测试账号: admin / admin123</span>
        </div>
      </Card>
    </div>
  );
};

export default LoginPage;
