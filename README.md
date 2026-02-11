# 企业人事管理系统 (HR Management System)

基于 Spring Boot + React 的企业人事管理系统，提供员工信息管理、考勤管理、薪资管理、培训管理、权限管理等功能。

## 📋 目录

- [选题意义](#选题意义)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [功能模块](#功能模块)
- [快速开始](#快速开始)
- [数据库设计](#数据库设计)

## 🎯 选题意义

本课题以中小企业人事管理为应用背景，围绕员工信息分散、统计困难、易出错等现实问题，设计并实现一套基于前后端分离架构的人事管理系统。系统对员工信息管理、考勤管理、薪资数据、培训管理、权限管理等核心业务进行集中管理，替代传统依赖纸质档案和 Excel 表格的方式，实现人事数据的标准化、结构化与统一维护，有助于提高数据的准确性与可追溯性，减轻人力资源部门的重复性工作负担，提升企业管理效率。

在技术层面，系统前端采用 React、TypeScript 与 Vite 搭建单页应用，结合 Ant Design 组件库实现友好直观的操作界面和人事数据可视化展示。后端基于 Spring Boot 3 构建，使用 Spring Web 提供 RESTful 接口，结合 Spring Data JPA 与 MySQL 完成人事数据的持久化存储，并通过 Spring Security 与 JWT 实现用户认证和基础权限控制，利用 Springdoc OpenAPI 自动生成接口文档，提升开发与维护效率。

## 🛠 技术栈

### 前端技术栈

- **框架**: React 18 + TypeScript
- **构建工具**: Vite 6
- **UI组件库**: Ant Design 5
- **图表库**: ECharts + Ant Design Charts
- **路由**: React Router 6
- **状态管理**: Zustand (with persist)
- **HTTP客户端**: Axios
- **样式**: Tailwind CSS + Less

### 后端技术栈

- **框架**: Spring Boot 3.2.0
- **语言**: Java 17
- **数据库**: MySQL 8.0+
- **ORM**: Spring Data JPA + Hibernate
- **安全**: Spring Security (BCrypt密码加密)
- **认证**: JWT (JSON Web Token)
- **API文档**: Swagger/OpenAPI (SpringDoc)
- **构建工具**: Maven

## 📁 项目结构

```
hr-management-system/
├── backend/                          # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/hrmanagement/
│   │   │   │   ├── config/          # 配置类
│   │   │   │   ├── controller/      # 控制器层
│   │   │   │   ├── entity/          # 实体类
│   │   │   │   ├── repository/      # 数据访问层
│   │   │   │   ├── service/         # 业务逻辑层
│   │   │   │   ├── dto/             # 数据传输对象
│   │   │   │   └── util/            # 工具类
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
│
├── src/                             # 前端项目
│   ├── pages/                       # 页面组件
│   │   ├── Login/                  # 登录页面
│   │   ├── Dashboard/              # 数据概览
│   │   ├── Employee/               # 员工管理
│   │   ├── Department/             # 部门管理
│   │   ├── Attendance/             # 考勤管理
│   │   ├── Salary/                 # 薪资管理
│   │   ├── Training/               # 培训管理
│   │   └── UserManage/             # 用户权限管理
│   ├── services/                    # API服务
│   ├── store/                       # 状态管理
│   ├── components/                  # 公共组件
│   └── types/                       # TypeScript类型定义
│
└── README.md
```

## ✨ 功能模块

### 1. 用户与权限管理模块
- ✅ JWT Token认证
- ✅ BCrypt密码加密
- ✅ 角色管理（管理员/HR/员工）
- ✅ 权限控制
- ✅ 修改密码功能

### 2. 员工信息管理模块
- 📝 员工档案维护（新增/修改/查询/删除）
- 📝 员工状态管理（在职/离职）
- 📝 员工信息导出
- 📝 员工照片上传

### 3. 部门与岗位管理模块
- 📝 部门信息维护
- 📝 岗位信息维护
- 📝 组织架构树形展示

### 4. 考勤管理模块
- 📝 日常考勤记录
- 📝 考勤统计查询
- 📝 考勤异常处理
- 📝 考勤报表导出

### 5. 薪资管理模块
- 📝 薪资项目维护
- 📝 工资信息生成与查询
- 📝 薪资统计分析
- 📝 工资条导出

### 6. 培训管理模块
- 📝 培训项目维护
- 📝 员工培训记录
- 📝 培训效果评估
- 📝 培训统计分析

### 7. 系统支持模块
- ✅ 接口文档（Swagger）
- ✅ 系统监控
- 📝 操作日志

> ✅ 已完成  📝 开发中

## 🚀 快速开始

### 环境要求

- Node.js >= 20.15.0
- Java 17
- MySQL 8.0+
- Maven 3.x

### 1. 克隆项目

```bash
git clone <repository-url>
cd hr-management-system
```

### 2. 数据库配置

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE hr_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 导入初始化脚本
mysql -u root -p hr_management < backend/init_hr_data.sql
```

### 3. 启动后端

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

### 4. 启动前端

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端服务将在 `http://localhost:5173` 启动

### 5. 访问应用

- 前端地址: http://localhost:5173
- 后端API: http://localhost:8080
- Swagger文档: http://localhost:8080/swagger-ui.html

### 默认账号

- 用户名: `admin`
- 密码: `admin123`

## 📊 数据库设计

### 核心数据表

#### 1. 用户与权限
- `sys_user` - 系统用户表
- `sys_role` - 角色表
- `sys_user_role` - 用户角色关联表

#### 2. 组织架构
- `hr_department` - 部门表
- `hr_position` - 岗位表

#### 3. 员工信息
- `hr_employee` - 员工信息表

#### 4. 考勤管理
- `hr_attendance` - 考勤记录表

#### 5. 薪资管理
- `hr_salary` - 薪资记录表

#### 6. 培训管理
- `hr_training` - 培训项目表
- `hr_employee_training` - 员工培训记录表

详细表结构请参考 `backend/init_hr_data.sql`

## 📚 API文档

启动后端服务后，访问 Swagger 文档：
http://localhost:8080/swagger-ui.html

## ⚙️ 配置说明

### 数据库配置

修改 `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hr_management
spring.datasource.username=root
spring.datasource.password=your_password
```

### JWT Token配置

修改 `backend/src/main/java/com/example/hrmanagement/util/JwtUtil.java`:

```java
private static final long EXPIRATION_TIME = 8 * 60 * 60 * 1000; // 8小时
```

## 📝 开发进度

- [x] 项目基础架构搭建
- [x] 用户认证与权限管理
- [ ] 员工信息管理模块（开发中）
- [ ] 部门与岗位管理模块
- [ ] 考勤管理模块
- [ ] 薪资管理模块
- [ ] 培训管理模块
- [ ] 系统优化与测试

## 📄 许可证

MIT License

## 👥 贡献

欢迎提交 Issue 和 Pull Request！

