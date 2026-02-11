# 项目改造指南

## 改造概述

本文档记录了从"数据可视化平台"到"企业人事管理系统"的改造过程。

## 已完成的改造

### 1. 项目基础配置更新 ✅

- [x] 更新项目名称：`chartboard` → `hr-management-system`
- [x] 更新后端项目名：`chartboard-backend` → `hr-management-backend`
- [x] 更新数据库名：`chartboard` → `hr_management`
- [x] 更新 README.md 文档

### 2. 数据库设计 ✅

创建了完整的人事管理系统数据库结构：

#### 用户与权限管理
- `sys_user` - 系统用户表
- `sys_role` - 角色表（管理员/HR/员工）
- `sys_user_role` - 用户角色关联表

#### 组织架构
- `hr_department` - 部门表（支持树形结构）
- `hr_position` - 岗位表

#### 员工信息
- `hr_employee` - 员工信息表（包含完整的员工档案）

#### 考勤管理
- `hr_attendance` - 考勤记录表

#### 薪资管理
- `hr_salary` - 薪资记录表

#### 培训管理
- `hr_training` - 培训项目表
- `hr_employee_training` - 员工培训记录表

### 3. 后端实体类创建 ✅

- [x] `Department.java` - 部门实体
- [x] `Position.java` - 岗位实体
- [x] `Employee.java` - 员工实体
- [x] `Role.java` - 角色实体
- [x] 更新 `User.java` - 适配新的用户表结构

### 4. Repository 层创建 ✅

- [x] `DepartmentRepository.java`
- [x] `PositionRepository.java`
- [x] `EmployeeRepository.java`
- [x] `RoleRepository.java`

### 5. DTO 类创建 ✅

- [x] `EmployeeDTO.java` - 员工数据传输对象
- [x] `DepartmentTreeDTO.java` - 部门树形结构

### 6. 前端类型定义 ✅

更新 `src/types/index.ts`，添加：
- `Employee` - 员工类型
- `Department` - 部门类型
- `Position` - 岗位类型
- `Attendance` - 考勤类型
- `Salary` - 薪资类型
- `Training` - 培训类型
- `EmployeeTraining` - 员工培训记录类型

## 待完成的改造

### 阶段一：员工信息管理模块（下一步）

#### 后端开发
- [ ] 创建 `EmployeeService.java`
- [ ] 创建 `EmployeeController.java`
- [ ] 创建 `DepartmentService.java`
- [ ] 创建 `DepartmentController.java`
- [ ] 创建 `PositionService.java`
- [ ] 创建 `PositionController.java`

#### 前端开发
- [ ] 创建 `src/pages/Employee/index.tsx` - 员工管理页面
- [ ] 创建 `src/pages/Department/index.tsx` - 部门管理页面
- [ ] 创建 `src/services/employee.ts` - 员工API服务
- [ ] 创建 `src/services/department.ts` - 部门API服务
- [ ] 更新路由配置
- [ ] 更新菜单配置

### 阶段二：考勤管理模块

- [ ] 后端：Attendance 实体、Repository、Service、Controller
- [ ] 前端：考勤管理页面、API服务

### 阶段三：薪资管理模块

- [ ] 后端：Salary 实体、Repository、Service、Controller
- [ ] 前端：薪资管理页面、API服务

### 阶段四：培训管理模块

- [ ] 后端：Training、EmployeeTraining 实体、Repository、Service、Controller
- [ ] 前端：培训管理页面、API服务

### 阶段五：权限管理增强

- [ ] 完善角色权限管理
- [ ] 实现菜单权限控制
- [ ] 实现数据权限控制

### 阶段六：系统优化

- [ ] 添加操作日志
- [ ] 数据导出功能
- [ ] 统计报表功能
- [ ] 性能优化
- [ ] 单元测试

## 数据库初始化

```bash
# 1. 创建数据库
mysql -u root -p -e "CREATE DATABASE hr_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 2. 导入初始化脚本
mysql -u root -p hr_management < backend/init_hr_data.sql

# 3. 验证数据
mysql -u root -p hr_management -e "SHOW TABLES;"
```

## 启动项目

### 后端启动

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### 前端启动

```bash
npm install
npm run dev
```

## 注意事项

1. **数据兼容性**：旧的 `users` 表已更新为 `sys_user`，需要重新初始化数据库
2. **认证系统**：JWT 认证机制保持不变，但用户表结构已更新
3. **渐进式开发**：建议按模块逐步开发，每个模块完成后进行测试
4. **代码复用**：保留了原有的认证、权限等基础功能，可以直接复用

## 下一步行动

现在开始开发第一个模块：**员工信息管理模块**

包括：
1. 员工列表展示（分页、搜索、筛选）
2. 员工信息新增
3. 员工信息编辑
4. 员工信息删除
5. 员工状态管理（在职/离职/试用期）
6. 部门和岗位的联动选择

准备好后，我们将开始创建相关的 Service 和 Controller。
