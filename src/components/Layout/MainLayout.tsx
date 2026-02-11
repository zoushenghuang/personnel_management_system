import React, { useState, useEffect } from 'react';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import { Layout, Menu, Avatar, Dropdown, Modal, Form, Input, message } from 'antd';
import {
  UserOutlined,
  LogoutOutlined,
  BarChartOutlined,
  DashboardOutlined,
  TeamOutlined,
  MonitorOutlined,
  IdcardOutlined,
  ApartmentOutlined,
} from '@ant-design/icons';
import { useAuthStore } from '../../store/authStore';
import { changePassword } from '../../services/api';
import { ROUTES, MESSAGES } from '../../constants';
import './MainLayout.less';

const { Header, Content, Sider } = Layout;

const MainLayout: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout, checkAuth } = useAuthStore();
  const [collapsed, setCollapsed] = useState(false);
  const [passwordModalVisible, setPasswordModalVisible] = useState(false);
  const [form] = Form.useForm();

  // 根据路径获取选中的菜单key
  const getSelectedKey = () => {
    if (location.pathname === '/employees') return 'employees';
    if (location.pathname === ROUTES.USER_MANAGE) return 'users';
    return 'dashboard';
  };

  const [selectedKey, setSelectedKey] = useState(getSelectedKey());

  useEffect(() => {
    setSelectedKey(getSelectedKey());
  }, [location.pathname]);

  // 检查认证状态
  useEffect(() => {
    const verify = async () => {
      const isValid = await checkAuth();
      if (!isValid) {
        message.warning(MESSAGES.TOKEN_EXPIRED);
        navigate(ROUTES.LOGIN);
      }
    };
    verify();

    const interval = setInterval(verify, 60000);
    return () => clearInterval(interval);
  }, [checkAuth, navigate]);

  const handleLogout = () => {
    logout();
    message.success(MESSAGES.LOGOUT_SUCCESS);
    navigate(ROUTES.LOGIN);
  };

  const handleChangePassword = async () => {
    try {
      const values = await form.validateFields();
      await changePassword(values.oldPassword, values.newPassword);
      message.success('密码修改成功，请重新登录');
      setPasswordModalVisible(false);
      form.resetFields();
      handleLogout();
    } catch (error: any) {
      if (error.errorFields) return;
      message.error(error.response?.data?.message || '修改密码失败');
    }
  };

  const userMenuItems = [
    {
      key: 'change-password',
      icon: <UserOutlined />,
      label: '修改密码',
      onClick: () => setPasswordModalVisible(true),
    },
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录',
      onClick: handleLogout,
    },
  ];

  const menuItems = [
    {
      key: 'dashboard',
      icon: <DashboardOutlined />,
      label: '数据概览',
      onClick: () => {
        setSelectedKey('dashboard');
        navigate(ROUTES.DASHBOARD);
      },
    },
    {
      key: 'hr',
      icon: <ApartmentOutlined />,
      label: '人事管理',
      children: [
        {
          key: 'employees',
          icon: <IdcardOutlined />,
          label: '员工管理',
          onClick: () => {
            setSelectedKey('employees');
            navigate('/employees');
          },
        },
      ],
    },
    {
      key: 'system',
      icon: <MonitorOutlined />,
      label: '系统管理',
      children: [
        {
          key: 'users',
          icon: <TeamOutlined />,
          label: '用户管理',
          onClick: () => {
            setSelectedKey('users');
            navigate(ROUTES.USER_MANAGE);
          },
        },
      ],
    },
  ];

  return (
    <Layout className="main-layout">
      <Header className="main-header">
        <div className="header-left">
          <BarChartOutlined className="logo-icon" />
          <h1 className="logo-title">企业人事管理系统</h1>
        </div>
        <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
          <div className="user-info">
            <Avatar icon={<UserOutlined />} className="user-avatar" />
            <span>{user?.username}</span>
          </div>
        </Dropdown>
      </Header>

      <Layout className="main-content-layout">
        <Sider
          collapsible
          collapsed={collapsed}
          onCollapse={setCollapsed}
          width={200}
          className="main-sider"
          // trigger={
          //   <div style={{ background: '#ffffff', color: '#000' }}>
          //     {collapsed ? '>' : '<'}
          //   </div>
          // }
        >
          <Menu
            mode="inline"
            selectedKeys={[selectedKey]}
            items={menuItems}
            className="main-menu"
          />
        </Sider>

        <Layout className="content-wrapper" style={{ marginLeft: collapsed ? 80 : 200 }}>
          <Content className="main-content">
            <Outlet />
          </Content>
        </Layout>
      </Layout>

      <Modal
        title="修改密码"
        open={passwordModalVisible}
        onOk={handleChangePassword}
        onCancel={() => {
          setPasswordModalVisible(false);
          form.resetFields();
        }}
        okText="确定"
        cancelText="取消"
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="oldPassword"
            label="原密码"
            rules={[{ required: true, message: '请输入原密码' }]}
          >
            <Input.Password placeholder="请输入原密码" />
          </Form.Item>

          <Form.Item
            name="newPassword"
            label="新密码"
            rules={[
              { required: true, message: '请输入新密码' },
              { min: 6, message: '密码至少6个字符' },
            ]}
          >
            <Input.Password placeholder="请输入新密码" />
          </Form.Item>

          <Form.Item
            name="confirmPassword"
            label="确认密码"
            dependencies={['newPassword']}
            rules={[
              { required: true, message: '请确认新密码' },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue('newPassword') === value) {
                    return Promise.resolve();
                  }
                  return Promise.reject(new Error('两次输入的密码不一致'));
                },
              }),
            ]}
          >
            <Input.Password placeholder="请再次输入新密码" />
          </Form.Item>
        </Form>
      </Modal>
    </Layout>
  );
};

export default MainLayout;
