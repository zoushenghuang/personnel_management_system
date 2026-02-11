// 用户相关类型
export interface User {
  id: number;
  username: string;
  role: string;
  password?: string;
  employeeId?: number;
  status?: number;
}

// 员工信息类型
export interface Employee {
  id?: number;
  empNo: string;
  name: string;
  gender?: number;
  birthDate?: string;
  idCard?: string;
  phone?: string;
  email?: string;
  deptId?: number;
  deptName?: string;
  positionId?: number;
  positionName?: string;
  hireDate?: string;
  leaveDate?: string;
  status?: number;
  statusText?: string;
  education?: string;
  major?: string;
  address?: string;
  emergencyContact?: string;
  emergencyPhone?: string;
  photoUrl?: string;
  remark?: string;
}

// 部门类型
export interface Department {
  id?: number;
  deptName: string;
  deptCode?: string;
  parentId?: number;
  managerId?: number;
  managerName?: string;
  phone?: string;
  email?: string;
  sortOrder?: number;
  status?: number;
  remark?: string;
  children?: Department[];
}

// 岗位类型
export interface Position {
  id?: number;
  positionName: string;
  positionCode?: string;
  deptId?: number;
  level?: string;
  sortOrder?: number;
  status?: number;
  description?: string;
}

// 考勤记录类型
export interface Attendance {
  id?: number;
  employeeId: number;
  employeeName?: string;
  attendanceDate: string;
  checkInTime?: string;
  checkOutTime?: string;
  workHours?: number;
  status?: string;
  remark?: string;
}

// 薪资记录类型
export interface Salary {
  id?: number;
  employeeId: number;
  employeeName?: string;
  salaryMonth: string;
  baseSalary?: number;
  bonus?: number;
  allowance?: number;
  overtimePay?: number;
  deduction?: number;
  socialSecurity?: number;
  tax?: number;
  actualSalary?: number;
  status?: string;
  remark?: string;
}

// 培训项目类型
export interface Training {
  id?: number;
  title: string;
  content?: string;
  trainer?: string;
  location?: string;
  startDate?: string;
  endDate?: string;
  maxParticipants?: number;
  status?: string;
  remark?: string;
}

// 员工培训记录类型
export interface EmployeeTraining {
  id?: number;
  employeeId: number;
  employeeName?: string;
  trainingId: number;
  trainingTitle?: string;
  status?: string;
  score?: number;
  result?: string;
  feedback?: string;
  remark?: string;
}

// API响应类型
export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
  timestamp: number;
}

// 分页信息类型
export interface PaginationInfo {
  page: number;
  pageSize: number;
  total: number;
  totalPages: number;
  hasNext: boolean;
  hasPrev: boolean;
}

// 分页响应类型
export interface PageResponse<T> {
  items: T[];
  pagination: PaginationInfo;
  links: {
    first: string;
    prev: string | null;
    next: string | null;
    last: string;
  };
}
