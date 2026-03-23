/**
 * 用户类型
 */
export interface User {
  id: number
  phone: string
  role: 'landlord' | 'tenant'
  avatar?: string
  nickname?: string
  openid?: string
  status?: number
}

/**
 * 房源类型
 */
export interface Property {
  id: number
  ownerId: number
  buildingId?: number
  buildingName?: string
  unit?: string
  roomNumber: string
  rentAmount: number
  depositAmount?: number
  status: number
  statusName?: string
  dailyRate?: number
  area?: number
  remark?: string
  createdAt?: string
  updatedAt?: string
}

/**
 * 房源输入类型（新增/编辑用）
 */
export interface PropertyInput {
  ownerId?: number
  buildingId?: number
  unit?: string
  roomNumber: string
  rentAmount: number
  depositAmount?: number
  dailyRate?: number
  area?: number
  remark?: string
}

/**
 * 租客类型
 */
export interface Tenant {
  id: number
  propertyId: number
  userId: number
  name: string
  phone: string
  leaseStartDate?: string
  leaseEndDate?: string
  status: number
  createdAt?: string
  updatedAt?: string
}

/**
 * 租金账单类型
 */
export interface RentRecord {
  id: number
  tenantId: number
  propertyId: number
  amount: number
  days?: number
  dailyRate?: number
  payMonth: string
  payDate?: string
  status: number
  remindCount: number
  remark?: string
  createdAt?: string
}

/**
 * 账单输入类型
 */
export interface BillInput {
  tenantId: number
  propertyId: number
  amount: number
  days?: number
  dailyRate?: number
  payMonth: string
  remark?: string
}

/**
 * 押金类型
 */
export interface Deposit {
  id: number
  tenantId: number
  propertyId: number
  amount: number
  status: number
  refundDate?: string
  refundAmount?: number
  remark?: string
  createdAt?: string
}

/**
 * 水电费类型
 */
export interface UtilityBill {
  id: number
  tenantId: number
  propertyId: number
  billMonth: string
  waterReading?: number
  waterAmount?: number
  electricityReading?: number
  electricityAmount?: number
  source: number
  status: number
  payDate?: string
  remark?: string
  createdAt?: string
}

/**
 * 其他费用类型
 */
export interface OtherFee {
  id: number
  tenantId: number
  propertyId: number
  feeType: string
  amount: number
  billMonth: string
  status: number
  payDate?: string
  remark?: string
  createdAt?: string
}

/**
 * 合同类型
 */
export interface Contract {
  id: number
  tenantId: number
  propertyId: number
  rentAmount: number
  depositAmount: number
  waterFee?: number
  electricityFee?: number
  otherFees?: string
  startDate: string
  endDate: string
  signUrl?: string
  status: number
  createdAt?: string
}

/**
 * 报修类型
 */
export interface Repair {
  id: number
  tenantId: number
  propertyId: number
  title: string
  description?: string
  images?: string
  status: number
  handlerId?: number
  handleRemark?: string
  createdAt?: string
  updatedAt?: string
}

/**
 * 通知消息类型
 */
export interface NotifyMessage {
  id: number
  userId: number
  senderId?: number
  type: string
  title: string
  content: string
  relatedId?: number
  relatedType?: string
  isRead: boolean
  readAt?: string
  createdAt: string
}

/**
 * 预约类型
 */
export interface Appointment {
  id: number
  propertyId: number
  userId?: number
  name: string
  phone: string
  appointmentDate: string
  appointmentTime: string
  status: number
  remark?: string
  rejectReason?: string
  createdAt: string
}

/**
 * API 响应类型
 */
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
  timestamp?: number
}

/**
 * 登录响应
 */
export interface LoginResponse {
  token: string
  userId: number
  nickname?: string
  avatarUrl?: string
  role?: string
  phone?: string
}

/**
 * 分页响应
 */
export interface PageResponse<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}
