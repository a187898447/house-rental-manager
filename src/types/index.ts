/**
 * 统一响应结果
 */
export interface Result<T> {
  code: number
  message: string
  data: T
}

/**
 * 用户信息
 */
export interface User {
  id: number
  phone: string
  nickname: string
  avatarUrl: string
  role: 'landlord' | 'tenant'
  token?: string
}

/**
 * 房源信息
 */
export interface Property {
  id: number
  name: string
  address: string
  buildingId?: number
  layout: string
  area: number
  rentAmount: number
  status: 0 | 1 // 0-未出租，1-已出租
  createTime?: string
}

/**
 * 房源 DTO
 */
export interface PropertyDTO {
  id?: number
  name: string
  address: string
  buildingId?: number
  layout: string
  area: number
  rentAmount: number
}

/**
 * 租客信息
 */
export interface Tenant {
  id: number
  userId: number
  propertyId: number
  propertyName?: string
  name: string
  phone: string
  checkInDate: string
  checkOutDate?: string
  status: 0 | 1 // 0-在住，1-已退租
  stayDays?: number
  createTime?: string
}

/**
 * 租客 DTO
 */
export interface TenantDTO {
  id?: number
  userId: number
  propertyId: number
  name: string
  idCard: string
  phone: string
  checkInDate: string
  remark?: string
}

/**
 * 租金记录
 */
export interface RentRecord {
  id: number
  tenantId: number
  tenantName?: string
  propertyId: number
  propertyName?: string
  amount: number
  dueDate: string
  paidDate?: string
  status: 0 | 1 | 2 // 0-未支付，1-已支付，2-逾期
  overdueDays?: number
  createTime?: string
}

/**
 * 租金记录 DTO
 */
export interface RentRecordDTO {
  tenantId: number
  propertyId: number
  amount: number
  dueDate: string
  remark?: string
}

/**
 * 分页结果
 */
export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
}
