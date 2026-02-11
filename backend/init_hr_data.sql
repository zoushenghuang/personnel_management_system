-- 企业人事管理系统数据库初始化脚本
-- Database: hr_management

-- 删除已存在的表（按依赖关系倒序删除）
DROP TABLE IF EXISTS hr_employee_training;
DROP TABLE IF EXISTS hr_training;
DROP TABLE IF EXISTS hr_salary;
DROP TABLE IF EXISTS hr_attendance;
DROP TABLE IF EXISTS hr_employee;
DROP TABLE IF EXISTS hr_position;
DROP TABLE IF EXISTS hr_department;
DROP TABLE IF EXISTS sys_user_role;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS sys_user;

-- ============================================
-- 1. 用户与权限管理表
-- ============================================

-- 系统用户表
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '登录用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    employee_id BIGINT COMMENT '关联员工ID',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_employee_id (employee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 角色表
CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    role_desc VARCHAR(200) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 用户角色关联表
CREATE TABLE sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ============================================
-- 2. 组织架构表
-- ============================================

-- 部门表
CREATE TABLE hr_department (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '部门ID',
    dept_name VARCHAR(100) NOT NULL COMMENT '部门名称',
    dept_code VARCHAR(50) UNIQUE COMMENT '部门编码',
    parent_id BIGINT DEFAULT 0 COMMENT '上级部门ID，0表示顶级部门',
    manager_id BIGINT COMMENT '部门负责人ID',
    phone VARCHAR(20) COMMENT '部门电话',
    email VARCHAR(100) COMMENT '部门邮箱',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_parent_id (parent_id),
    INDEX idx_dept_code (dept_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 岗位表
CREATE TABLE hr_position (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '岗位ID',
    position_name VARCHAR(100) NOT NULL COMMENT '岗位名称',
    position_code VARCHAR(50) UNIQUE COMMENT '岗位编码',
    dept_id BIGINT COMMENT '所属部门ID',
    level VARCHAR(20) COMMENT '岗位级别',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    description VARCHAR(500) COMMENT '岗位描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_dept_id (dept_id),
    INDEX idx_position_code (position_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位表';

-- ============================================
-- 3. 员工信息表
-- ============================================

CREATE TABLE hr_employee (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '员工ID',
    emp_no VARCHAR(50) NOT NULL UNIQUE COMMENT '工号',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender TINYINT COMMENT '性别：0-女，1-男',
    birth_date DATE COMMENT '出生日期',
    id_card VARCHAR(18) COMMENT '身份证号',
    phone VARCHAR(20) COMMENT '手机号码',
    email VARCHAR(100) COMMENT '邮箱',
    dept_id BIGINT COMMENT '所属部门ID',
    position_id BIGINT COMMENT '岗位ID',
    hire_date DATE COMMENT '入职日期',
    leave_date DATE COMMENT '离职日期',
    status TINYINT DEFAULT 1 COMMENT '状态：0-离职，1-在职，2-试用期',
    education VARCHAR(20) COMMENT '学历',
    major VARCHAR(100) COMMENT '专业',
    address VARCHAR(200) COMMENT '家庭住址',
    emergency_contact VARCHAR(50) COMMENT '紧急联系人',
    emergency_phone VARCHAR(20) COMMENT '紧急联系电话',
    photo_url VARCHAR(255) COMMENT '照片URL',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_emp_no (emp_no),
    INDEX idx_dept_id (dept_id),
    INDEX idx_position_id (position_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工信息表';

-- ============================================
-- 4. 考勤管理表
-- ============================================

CREATE TABLE hr_attendance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '考勤ID',
    employee_id BIGINT NOT NULL COMMENT '员工ID',
    attendance_date DATE NOT NULL COMMENT '考勤日期',
    check_in_time TIME COMMENT '上班打卡时间',
    check_out_time TIME COMMENT '下班打卡时间',
    work_hours DECIMAL(4,2) COMMENT '工作时长（小时）',
    status VARCHAR(20) DEFAULT 'NORMAL' COMMENT '状态：NORMAL-正常，LATE-迟到，EARLY-早退，ABSENT-缺勤，LEAVE-请假',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_emp_date (employee_id, attendance_date),
    INDEX idx_employee_id (employee_id),
    INDEX idx_attendance_date (attendance_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考勤记录表';

-- ============================================
-- 5. 薪资管理表
-- ============================================

CREATE TABLE hr_salary (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '薪资ID',
    employee_id BIGINT NOT NULL COMMENT '员工ID',
    salary_month VARCHAR(7) NOT NULL COMMENT '工资月份（格式：YYYY-MM）',
    base_salary DECIMAL(10,2) DEFAULT 0 COMMENT '基本工资',
    bonus DECIMAL(10,2) DEFAULT 0 COMMENT '奖金',
    allowance DECIMAL(10,2) DEFAULT 0 COMMENT '补贴',
    overtime_pay DECIMAL(10,2) DEFAULT 0 COMMENT '加班费',
    deduction DECIMAL(10,2) DEFAULT 0 COMMENT '扣款',
    social_security DECIMAL(10,2) DEFAULT 0 COMMENT '社保扣除',
    tax DECIMAL(10,2) DEFAULT 0 COMMENT '个人所得税',
    actual_salary DECIMAL(10,2) DEFAULT 0 COMMENT '实发工资',
    status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态：DRAFT-草稿，CONFIRMED-已确认，PAID-已发放',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_emp_month (employee_id, salary_month),
    INDEX idx_employee_id (employee_id),
    INDEX idx_salary_month (salary_month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='薪资记录表';

-- ============================================
-- 6. 培训管理表
-- ============================================

-- 培训项目表
CREATE TABLE hr_training (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '培训ID',
    title VARCHAR(200) NOT NULL COMMENT '培训主题',
    content TEXT COMMENT '培训内容描述',
    trainer VARCHAR(100) COMMENT '培训讲师',
    location VARCHAR(200) COMMENT '培训地点',
    start_date DATETIME COMMENT '开始时间',
    end_date DATETIME COMMENT '结束时间',
    max_participants INT COMMENT '最大参与人数',
    status VARCHAR(20) DEFAULT 'PLANNED' COMMENT '状态：PLANNED-计划中，ONGOING-进行中，COMPLETED-已完成，CANCELLED-已取消',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_start_date (start_date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='培训项目表';

-- 员工培训记录表
CREATE TABLE hr_employee_training (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    employee_id BIGINT NOT NULL COMMENT '员工ID',
    training_id BIGINT NOT NULL COMMENT '培训ID',
    status VARCHAR(20) DEFAULT 'REGISTERED' COMMENT '状态：REGISTERED-已报名，ATTENDED-已参加，ABSENT-缺席，COMPLETED-已完成',
    score DECIMAL(5,2) COMMENT '培训成绩',
    result VARCHAR(20) COMMENT '培训结果：PASS-通过，FAIL-未通过',
    feedback TEXT COMMENT '培训反馈',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_emp_training (employee_id, training_id),
    INDEX idx_employee_id (employee_id),
    INDEX idx_training_id (training_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工培训记录表';

-- ============================================
-- 初始化数据
-- ============================================

-- 插入角色数据
INSERT INTO sys_role (role_name, role_code, role_desc) VALUES
('系统管理员', 'ADMIN', '系统最高权限管理员'),
('人事专员', 'HR', '人力资源部门工作人员'),
('普通员工', 'EMPLOYEE', '普通员工用户');

-- 插入管理员用户（密码: admin123，已BCrypt加密）
INSERT INTO sys_user (username, password, status) VALUES
('admin', '$2a$10$xZilb2QY7aImPU5CuRvfUeuuzLTSnVsEjONY9EMGjdoIXEXCKkHFC', 1);

-- 关联管理员角色
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 插入部门数据
INSERT INTO hr_department (dept_name, dept_code, parent_id, sort_order) VALUES
('总经办', 'DEPT001', 0, 1),
('人力资源部', 'DEPT002', 0, 2),
('技术部', 'DEPT003', 0, 3),
('市场部', 'DEPT004', 0, 4),
('财务部', 'DEPT005', 0, 5),
('前端开发组', 'DEPT006', 3, 1),
('后端开发组', 'DEPT007', 3, 2),
('测试组', 'DEPT008', 3, 3);

-- 插入岗位数据
INSERT INTO hr_position (position_name, position_code, dept_id, level, sort_order) VALUES
('总经理', 'POS001', 1, 'P10', 1),
('人事经理', 'POS002', 2, 'P8', 1),
('人事专员', 'POS003', 2, 'P5', 2),
('技术总监', 'POS004', 3, 'P9', 1),
('前端工程师', 'POS005', 6, 'P6', 1),
('后端工程师', 'POS006', 7, 'P6', 1),
('测试工程师', 'POS007', 8, 'P5', 1),
('市场经理', 'POS008', 4, 'P8', 1),
('财务经理', 'POS009', 5, 'P8', 1);

-- 插入示例员工数据
INSERT INTO hr_employee (emp_no, name, gender, birth_date, phone, email, dept_id, position_id, hire_date, status, education) VALUES
('EMP001', '张三', 1, '1990-05-15', '13800138001', 'zhangsan@company.com', 1, 1, '2020-01-01', 1, '本科'),
('EMP002', '李四', 1, '1992-08-20', '13800138002', 'lisi@company.com', 2, 2, '2020-03-15', 1, '硕士'),
('EMP003', '王五', 0, '1995-03-10', '13800138003', 'wangwu@company.com', 2, 3, '2021-06-01', 1, '本科'),
('EMP004', '赵六', 1, '1988-12-05', '13800138004', 'zhaoliu@company.com', 3, 4, '2019-09-01', 1, '硕士'),
('EMP005', '钱七', 1, '1993-07-25', '13800138005', 'qianqi@company.com', 6, 5, '2021-01-15', 1, '本科'),
('EMP006', '孙八', 0, '1994-11-30', '13800138006', 'sunba@company.com', 7, 6, '2021-03-20', 1, '本科'),
('EMP007', '周九', 1, '1996-02-14', '13800138007', 'zhoujiu@company.com', 8, 7, '2022-05-10', 1, '本科'),
('EMP008', '吴十', 0, '1991-09-08', '13800138008', 'wushi@company.com', 4, 8, '2020-07-01', 1, '本科');

-- 插入示例考勤数据（最近一周）
INSERT INTO hr_attendance (employee_id, attendance_date, check_in_time, check_out_time, work_hours, status) VALUES
(1, CURDATE() - INTERVAL 6 DAY, '09:00:00', '18:00:00', 8.0, 'NORMAL'),
(1, CURDATE() - INTERVAL 5 DAY, '09:05:00', '18:00:00', 7.9, 'LATE'),
(1, CURDATE() - INTERVAL 4 DAY, '09:00:00', '18:00:00', 8.0, 'NORMAL'),
(2, CURDATE() - INTERVAL 6 DAY, '09:00:00', '18:00:00', 8.0, 'NORMAL'),
(2, CURDATE() - INTERVAL 5 DAY, '09:00:00', '18:00:00', 8.0, 'NORMAL'),
(3, CURDATE() - INTERVAL 6 DAY, '09:00:00', '17:50:00', 7.8, 'EARLY');

-- 插入示例薪资数据
INSERT INTO hr_salary (employee_id, salary_month, base_salary, bonus, allowance, overtime_pay, deduction, social_security, tax, actual_salary, status) VALUES
(1, DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y-%m'), 20000.00, 5000.00, 1000.00, 0.00, 0.00, 2000.00, 2500.00, 21500.00, 'PAID'),
(2, DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y-%m'), 15000.00, 3000.00, 800.00, 0.00, 0.00, 1500.00, 1800.00, 15500.00, 'PAID'),
(3, DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y-%m'), 8000.00, 1000.00, 500.00, 0.00, 0.00, 800.00, 500.00, 8200.00, 'PAID');

-- 插入示例培训数据
INSERT INTO hr_training (title, content, trainer, location, start_date, end_date, max_participants, status) VALUES
('新员工入职培训', '公司文化、规章制度、业务流程介绍', '李四', '会议室A', DATE_ADD(CURDATE(), INTERVAL 7 DAY), DATE_ADD(CURDATE(), INTERVAL 7 DAY), 50, 'PLANNED'),
('Java高级编程培训', 'Spring Boot、微服务架构等技术培训', '赵六', '培训室', DATE_ADD(CURDATE(), INTERVAL 14 DAY), DATE_ADD(CURDATE(), INTERVAL 16 DAY), 30, 'PLANNED'),
('团队协作与沟通技巧', '提升团队协作能力和沟通效率', '外部讲师', '会议室B', DATE_ADD(CURDATE(), INTERVAL 21 DAY), DATE_ADD(CURDATE(), INTERVAL 21 DAY), 40, 'PLANNED');

-- 插入员工培训记录
INSERT INTO hr_employee_training (employee_id, training_id, status) VALUES
(5, 2, 'REGISTERED'),
(6, 2, 'REGISTERED'),
(7, 1, 'REGISTERED'),
(8, 3, 'REGISTERED');

-- 完成初始化
SELECT '数据库初始化完成！' AS message;
