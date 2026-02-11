# 快速启动指南

## 员工信息管理模块已完成！

### 已完成的功能

✅ 后端开发
- EmployeeService - 员工业务逻辑
- DepartmentService - 部门业务逻辑
- PositionService - 岗位业务逻辑
- EmployeeController - 员工API接口
- DepartmentController - 部门API接口
- PositionController - 岗位API接口

✅ 前端开发
- 员工管理页面（完整的CRUD功能）
- 员工搜索和筛选
- 部门和岗位联动选择
- API服务封装
- 路由和菜单配置

### 启动步骤

#### 1. 初始化数据库

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE hr_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 导入初始化脚本
mysql -u root -p hr_management < backend/init_hr_data.sql
```

#### 2. 启动后端

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端将在 http://localhost:8080 启动

#### 3. 启动前端

```bash
# 如果还没安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端将在 http://localhost:5173 启动

#### 4. 访问系统

- 前端地址: http://localhost:5173
- 登录账号: admin / admin123
- Swagger文档: http://localhost:8080/swagger-ui.html

### 功能演示

#### 员工管理功能

1. **查看员工列表**
   - 访问：人事管理 > 员工管理
   - 显示所有员工信息（工号、姓名、部门、岗位等）

2. **搜索员工**
   - 按姓名搜索
   - 按部门筛选
   - 按状态筛选（在职/试用期/离职）

3. **新增员工**
   - 点击"新增员工"按钮
   - 填写员工基本信息
   - 选择部门和岗位
   - 提交保存

4. **编辑员工**
   - 点击员工列表中的"编辑"按钮
   - 修改员工信息
   - 保存更新

5. **删除员工**
   - 点击员工列表中的"删除"按钮
   - 确认删除操作

### 测试数据

系统已预置以下测试数据：

**部门**
- 总经办
- 人力资源部
- 技术部（含前端开发组、后端开发组、测试组）
- 市场部
- 财务部

**岗位**
- 总经理、人事经理、人事专员
- 技术总监、前端工程师、后端工程师、测试工程师
- 市场经理、财务经理

**员工**
- 8名示例员工，分布在不同部门和岗位

### API接口文档

访问 Swagger 文档查看所有API接口：
http://localhost:8080/swagger-ui.html

主要接口：

**员工管理**
- GET /api/employees - 获取所有员工
- GET /api/employees/{id} - 获取指定员工
- GET /api/employees/search - 搜索员工
- POST /api/employees - 创建员工
- PUT /api/employees/{id} - 更新员工
- DELETE /api/employees/{id} - 删除员工

**部门管理**
- GET /api/departments - 获取所有部门
- GET /api/departments/tree - 获取部门树
- POST /api/departments - 创建部门
- PUT /api/departments/{id} - 更新部门
- DELETE /api/departments/{id} - 删除部门

**岗位管理**
- GET /api/positions - 获取所有岗位
- GET /api/positions/department/{deptId} - 获取部门岗位
- POST /api/positions - 创建岗位
- PUT /api/positions/{id} - 更新岗位
- DELETE /api/positions/{id} - 删除岗位

### 常见问题

**Q: 数据库连接失败？**
A: 检查 `backend/src/main/resources/application.properties` 中的数据库配置

**Q: 前端无法访问后端API？**
A: 确保后端已启动在 8080 端口，前端代理配置正确

**Q: 员工列表为空？**
A: 确认已执行数据库初始化脚本 `init_hr_data.sql`

### 下一步开发

接下来可以开发其他模块：

1. **部门管理页面** - 树形结构展示和管理
2. **岗位管理页面** - 岗位信息维护
3. **考勤管理模块** - 考勤记录和统计
4. **薪资管理模块** - 工资计算和发放
5. **培训管理模块** - 培训项目和记录

### 技术支持

如有问题，请查看：
- README.md - 项目完整文档
- MIGRATION_GUIDE.md - 改造指南
- backend/init_hr_data.sql - 数据库结构

祝使用愉快！🎉
