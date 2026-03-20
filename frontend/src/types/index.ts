/**
 * 用户类型
 */
export interface User {
  id: string
  openid: string
  phone: string
  role: 'landlord' | 'tenant'
  avatar?: string
  nickname?: string
}

/**
 * 房源类型
 */
export interface Property {
  id: string
  buildingId: string
  building: string
  unit: string
  roomNumber: string
  rent: number
  status: 'vacant' | 'rented' | 'rented_unpaid' | 'rented_paid'
  images: string[]
  area?: number
  floor?: string
  orientation?: string
  description?: string
  createTime?: string
  updateTime?: string
}

/**
 * 房源输入类型（新增/编辑用）
 */
export interface PropertyInput {
  buildingId?: string
  building: string
  unit: string
  roomNumber: string
  rent: number
  images?: string[]
  area?: number
  floor?: string
  orientation?: string
  description?: string
}

/**
 * 租客类型
 */
export interface Tenant {
  id: string
  propertyId: string
  name: string
  phone: string
  leaseStart: string
  leaseEnd: string
  status: 'living' | 'checkout_pending' | 'checkout'
  idCard?: string
  emergencyContact?: string
  emergencyPhone?: string
  createTime?: string
}

/**
 * 租金账单类型
 */
export interface RentBill {
  id: string
  propertyId: string
  tenantId: string
  amount: number
  days?: number
  dailyRate?: number
  status: 'unpaid' | 'paid'
  dueDate: string
  payDate?: string
  remindCount: number
  month: string
  year: number
  type: 'rent' | 'deposit' | 'utility' | 'other'
}

/**
 * 账单输入类型
 */
export interface BillInput {
  propertyId: string
  tenantId: string
  amount: number
  days?: number
  dailyRate?: number
  month: string
  year: number
  type: 'rent' | 'deposit' | 'utility' | 'other'
}

/**
 * 押金类型
 */
export interface Deposit {
  id: string
  propertyId: string
  tenantId: string
  amount: number
  status: 'held' | 'refunded' | 'partially_refunded'
  createTime: string
  refundTime?: string
  refundAmount?: number
  remark?: string
}

/**
 * 水电费类型
 */
export interface UtilityFee {
  id: string
  propertyId: string
  tenantId: string
  month: string
  year: number
  waterUsage?: number
  waterFee?: number
  electricityUsage?: number
  electricityFee?: number
  totalFee: number
  status: 'unpaid' | 'paid'
  dueDate: string
  payDate?: string
  readingDate?: string
}

/**
 * 其他费用类型
 */
export interface OtherFee {
  id: string
  propertyId: string
  tenantId: string
  name: string
  amount: number
  dueDate: string
  status: 'unpaid' | 'paid'
  remark?: string
}

/**
 * 合同类型
 */
export interface Contract {
  id: string
  tenantId: string
  propertyId: string
  rentAmount: number
  depositAmount: number
  waterFee: number
  electricityFee: number
  otherFees: OtherFee[]
  signImageUrl: string
  status: 'draft' | 'signed'
  leaseStart: string
  leaseEnd: string
  createTime: string
  signTime?: string
}

/**
 * 报修类型
 */
export interface Repair {
  id: string
  propertyId: string
  tenantId?: string
  title: string
  description: string
  images: string[]
  status: 'pending' | 'processing' | 'completed' | 'cancelled'
  contactPhone?: string
  createTime: string
  completeTime?: string
  remark?: string
}

/**
 * API 响应类型
 */
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

/**
 * 登录响应
 */
export interface LoginResponse {
  token: string
  needBindPhone: boolean
  userInfo?: User
}

/**
 * 分页响应
 */
export interface PageResponse<T> {
  list: T[]
  total: number
  page: number
  pageSize: number
}
