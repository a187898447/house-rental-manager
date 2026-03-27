// 预约服务 - Appointment
import { request } from './index'

export interface Appointment {
  id: number
  propertyId: number
  userId?: number
  name: string
  phone: string
  appointmentDate: string
  appointmentTime: string
  status: number // 0-待确认, 1-已确认, 2-已完成, 3-已取消, 4-已拒绝
  remark?: string
  rejectReason?: string
  createdAt: string
}

// 获取预约列表（租客端）
export const getMyAppointments = async (params?: {
  status?: number
  page?: number
  size?: number
}) => {
  const res = await request({ url: '/api/tenant/appointment/my', method: 'GET', data: params }) as any
  const data = res?.data
  return data?.records ?? data?.list ?? []
}

// 获取预约列表（房东端）
export const getOwnerAppointments = async (params?: {
  propertyId?: number
  status?: number
  page?: number
  size?: number
}) => {
  const res = await request({ url: '/api/tenant/appointment/owner', method: 'GET', data: params }) as any
  const data = res?.data
  return data?.records ?? data?.list ?? []
}

// 创建预约
export const createAppointment = async (data: {
  propertyId: number
  name: string
  phone: string
  appointmentDate: string
  appointmentTime: string
  remark?: string
}) => {
  return request({ url: '/api/tenant/appointment', method: 'POST', data })
}

// 更新预约状态
export const updateAppointmentStatus = async (
  id: number,
  data: {
    status: number
    remark?: string
    rejectReason?: string
  }
) => {
  return request({ url: `/api/tenant/appointment/${id}/status`, method: 'PUT', data })
}

// 取消预约
export const cancelAppointment = async (id: number, remark?: string) => {
  return request({ url: `/api/tenant/appointment/${id}/cancel`, method: 'POST', data: { remark } })
}