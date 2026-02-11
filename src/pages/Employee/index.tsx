import React, { useState, useEffect } from 'react';
import { 
  Table, Button, Space, Modal, Form, Input, Select, DatePicker, 
  message, Popconfirm, Tag, Card, Row, Col 
} from 'antd';
import { 
  PlusOutlined, EditOutlined, DeleteOutlined, SearchOutlined,
  UserOutlined, TeamOutlined 
} from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import dayjs from 'dayjs';
import { employeeApi } from '../../services/employee';
import { departmentApi } from '../../services/department';
import { positionApi } from '../../services/position';
import type { Employee, Department, Position } from '../../types';
import './index.less';

const { Option } = Select;

const EmployeePage: React.FC = () => {
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [departments, setDepartments] = useState<Department[]>([]);
  const [positions, setPositions] = useState<Position[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editingEmployee, setEditingEmployee] = useState<Employee | null>(null);
  const [searchParams, setSearchParams] = useState<{
    name?: string;
    deptId?: number;
    status?: number;
  }>({});
  
  const [form] = Form.useForm();

  useEffect(() => {
    fetchEmployees();
    fetchDepartments();
    fetchPositions();
  }, []);

  // 获取员工列表
  const fetchEmployees = async () => {
    setLoading(true);
    try {
      const response = await employeeApi.getAllEmployees();
      setEmployees(response.data.data);
    } catch (error) {
      message.error('获取员工列表失败');
    } finally {
      setLoading(false);
    }
  };

  // 获取部门列表
  const fetchDepartments = async () => {
    try {
      const response = await departmentApi.getAllDepartments();
      setDepartments(response.data.data);
    } catch (error) {
      message.error('获取部门列表失败');
    }
  };

  // 获取岗位列表
  const fetchPositions = async () => {
    try {
      const response = await positionApi.getAllPositions();
      setPositions(response.data.data);
    } catch (error) {
      message.error('获取岗位列表失败');
    }
  };

  // 搜索员工
  const handleSearch = async () => {
    setLoading(true);
    try {
      const response = await employeeApi.searchEmployees(searchParams);
      setEmployees(response.data.data);
    } catch (error) {
      message.error('搜索失败');
    } finally {
      setLoading(false);
    }
  };

  // 重置搜索
  const handleReset = () => {
    setSearchParams({});
    fetchEmployees();
  };

  // 打开新增/编辑弹窗
  const handleOpenModal = (employee?: Employee) => {
    if (employee) {
      setEditingEmployee(employee);
      form.setFieldsValue({
        ...employee,
        birthDate: employee.birthDate ? dayjs(employee.birthDate) : null,
        hireDate: employee.hireDate ? dayjs(employee.hireDate) : null,
        leaveDate: employee.leaveDate ? dayjs(employee.leaveDate) : null,
      });
    } else {
      setEditingEmployee(null);
      form.resetFields();
    }
    setModalVisible(true);
  };

  // 关闭弹窗
  const handleCloseModal = () => {
    setModalVisible(false);
    setEditingEmployee(null);
    form.resetFields();
  };

  // 提交表单
  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      const employeeData = {
        ...values,
        birthDate: values.birthDate ? values.birthDate.format('YYYY-MM-DD') : null,
        hireDate: values.hireDate ? values.hireDate.format('YYYY-MM-DD') : null,
        leaveDate: values.leaveDate ? values.leaveDate.format('YYYY-MM-DD') : null,
      };

      if (editingEmployee) {
        await employeeApi.updateEmployee(editingEmployee.id!, employeeData);
        message.success('员工更新成功');
      } else {
        await employeeApi.createEmployee(employeeData);
        message.success('员工创建成功');
      }

      handleCloseModal();
      fetchEmployees();
    } catch (error: any) {
      message.error(error.response?.data?.message || '操作失败');
    }
  };

  // 删除员工
  const handleDelete = async (id: number) => {
    try {
      await employeeApi.deleteEmployee(id);
      message.success('员工删除成功');
      fetchEmployees();
    } catch (error: any) {
      message.error(error.response?.data?.message || '删除失败');
    }
  };

  // 更新员工状态
  const handleStatusChange = async (id: number, status: number) => {
    try {
      await employeeApi.updateEmployeeStatus(id, status);
      message.success('状态更新成功');
      fetchEmployees();
    } catch (error: any) {
      message.error(error.response?.data?.message || '状态更新失败');
    }
  };

  // 表格列定义
  const columns: ColumnsType<Employee> = [
    {
      title: '工号',
      dataIndex: 'empNo',
      key: 'empNo',
      width: 120,
    },
    {
      title: '姓名',
      dataIndex: 'name',
      key: 'name',
      width: 100,
    },
    {
      title: '性别',
      dataIndex: 'gender',
      key: 'gender',
      width: 60,
      render: (gender: number) => gender === 1 ? '男' : '女',
    },
    {
      title: '部门',
      dataIndex: 'deptName',
      key: 'deptName',
      width: 120,
    },
    {
      title: '岗位',
      dataIndex: 'positionName',
      key: 'positionName',
      width: 120,
    },
    {
      title: '手机号',
      dataIndex: 'phone',
      key: 'phone',
      width: 120,
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      key: 'email',
      width: 180,
    },
    {
      title: '入职日期',
      dataIndex: 'hireDate',
      key: 'hireDate',
      width: 110,
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      width: 100,
      render: (status: number, record: Employee) => {
        const statusConfig = {
          0: { color: 'default', text: '离职' },
          1: { color: 'success', text: '在职' },
          2: { color: 'processing', text: '试用期' },
        };
        const config = statusConfig[status as keyof typeof statusConfig] || { color: 'default', text: '未知' };
        return <Tag color={config.color}>{config.text}</Tag>;
      },
    },
    {
      title: '操作',
      key: 'action',
      width: 200,
      fixed: 'right',
      render: (_, record) => (
        <Space size="small">
          <Button 
            type="link" 
            size="small" 
            icon={<EditOutlined />}
            onClick={() => handleOpenModal(record)}
          >
            编辑
          </Button>
          <Popconfirm
            title="确定要删除这个员工吗？"
            onConfirm={() => handleDelete(record.id!)}
            okText="确定"
            cancelText="取消"
          >
            <Button 
              type="link" 
              size="small" 
              danger
              icon={<DeleteOutlined />}
            >
              删除
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div className="employee-page">
      <Card>
        {/* 搜索栏 */}
        <Row gutter={16} style={{ marginBottom: 16 }}>
          <Col span={6}>
            <Input
              placeholder="员工姓名"
              prefix={<UserOutlined />}
              value={searchParams.name}
              onChange={(e) => setSearchParams({ ...searchParams, name: e.target.value })}
              onPressEnter={handleSearch}
            />
          </Col>
          <Col span={6}>
            <Select
              placeholder="选择部门"
              style={{ width: '100%' }}
              allowClear
              value={searchParams.deptId}
              onChange={(value) => setSearchParams({ ...searchParams, deptId: value })}
            >
              {departments.map(dept => (
                <Option key={dept.id} value={dept.id}>{dept.deptName}</Option>
              ))}
            </Select>
          </Col>
          <Col span={6}>
            <Select
              placeholder="员工状态"
              style={{ width: '100%' }}
              allowClear
              value={searchParams.status}
              onChange={(value) => setSearchParams({ ...searchParams, status: value })}
            >
              <Option value={1}>在职</Option>
              <Option value={2}>试用期</Option>
              <Option value={0}>离职</Option>
            </Select>
          </Col>
          <Col span={6}>
            <Space>
              <Button type="primary" icon={<SearchOutlined />} onClick={handleSearch}>
                搜索
              </Button>
              <Button onClick={handleReset}>重置</Button>
            </Space>
          </Col>
        </Row>

        {/* 操作按钮 */}
        <div style={{ marginBottom: 16 }}>
          <Button 
            type="primary" 
            icon={<PlusOutlined />}
            onClick={() => handleOpenModal()}
          >
            新增员工
          </Button>
        </div>

        {/* 员工表格 */}
        <Table
          columns={columns}
          dataSource={employees}
          rowKey="id"
          loading={loading}
          scroll={{ x: 1500 }}
          pagination={{
            showSizeChanger: true,
            showQuickJumper: true,
            showTotal: (total) => `共 ${total} 条记录`,
          }}
        />
      </Card>

      {/* 新增/编辑弹窗 */}
      <Modal
        title={editingEmployee ? '编辑员工' : '新增员工'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={handleCloseModal}
        width={800}
        destroyOnClose
      >
        <Form
          form={form}
          layout="vertical"
          initialValues={{ status: 2, gender: 1 }}
        >
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item
                label="工号"
                name="empNo"
                rules={[{ required: true, message: '请输入工号' }]}
              >
                <Input placeholder="请输入工号" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="姓名"
                name="name"
                rules={[{ required: true, message: '请输入姓名' }]}
              >
                <Input placeholder="请输入姓名" />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="性别" name="gender">
                <Select>
                  <Option value={1}>男</Option>
                  <Option value={0}>女</Option>
                </Select>
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="出生日期" name="birthDate">
                <DatePicker style={{ width: '100%' }} />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="手机号" name="phone">
                <Input placeholder="请输入手机号" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="邮箱" name="email">
                <Input placeholder="请输入邮箱" />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="部门" name="deptId">
                <Select placeholder="请选择部门" allowClear>
                  {departments.map(dept => (
                    <Option key={dept.id} value={dept.id}>{dept.deptName}</Option>
                  ))}
                </Select>
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="岗位" name="positionId">
                <Select placeholder="请选择岗位" allowClear>
                  {positions.map(pos => (
                    <Option key={pos.id} value={pos.id}>{pos.positionName}</Option>
                  ))}
                </Select>
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="入职日期" name="hireDate">
                <DatePicker style={{ width: '100%' }} />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="状态" name="status">
                <Select>
                  <Option value={1}>在职</Option>
                  <Option value={2}>试用期</Option>
                  <Option value={0}>离职</Option>
                </Select>
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="学历" name="education">
                <Select placeholder="请选择学历" allowClear>
                  <Option value="高中">高中</Option>
                  <Option value="大专">大专</Option>
                  <Option value="本科">本科</Option>
                  <Option value="硕士">硕士</Option>
                  <Option value="博士">博士</Option>
                </Select>
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="专业" name="major">
                <Input placeholder="请输入专业" />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item label="身份证号" name="idCard">
            <Input placeholder="请输入身份证号" />
          </Form.Item>

          <Form.Item label="家庭住址" name="address">
            <Input.TextArea placeholder="请输入家庭住址" rows={2} />
          </Form.Item>

          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="紧急联系人" name="emergencyContact">
                <Input placeholder="请输入紧急联系人" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="紧急联系电话" name="emergencyPhone">
                <Input placeholder="请输入紧急联系电话" />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item label="备注" name="remark">
            <Input.TextArea placeholder="请输入备注" rows={3} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default EmployeePage;
