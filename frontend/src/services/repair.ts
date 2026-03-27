import { request } from './index'
import type { Repair, ApiResponse } from '@/types'

/**
 * 获取报修列表（租客）
 */
export const getRepairs = async (params?: {
  tenantId?: string
  status?: string
}) => {
  const res = await request({ url: '/api/repair/tenant', method: 'GET', data: params }) as any
  const data = res?.data
  return data?.records ?? data?.list ?? []
}

/**
 * 获取报修列表（房东）
 */
export const getOwnerRepairs = async (params?: {
  propertyId?: string
  status?: string
}) => {
  const res = await request({ url: '/api/repair/owner', method: 'GET', data: params }) as any
  const data = res?.data
  return data?.records ?? data?.list ?? []
}

/**
 * 获取报修详情
 */
export const getRepairDetail = async (id: string) => {
  const res = await request({ url: `/api/repair/${id}`, method: 'GET' }) as any
  return res?.data || res
}

/**
 * 提交报修申请
 */
export const createRepair = async (data: {
  propertyId: string
  title: string
  description: string
  images?: string[]
  contactPhone?: string
}) => {
  const res = await request({ url: '/api/repair', method: 'POST', data }) as any
  return res?.data || res
}

/**
 * 更新报修状态
 */
export const updateRepairStatus = async (id: string, data: { 
  status: 'processing' | 'completed' | 'cancelled'
  remark?: string
}) => {
  const res = await request({ url: `/api/repair/${id}/status`, method: 'PUT', data }) as any
  return res?.data || res
}

/**
 * 开始处理报修
 */
export const handleRepair = async (id: string) => {
  const res = await request({ url: `/api/repair/${id}/process`, method: 'POST' }) as any
  return res?.data || res
}

/**
 * 完成报修处理
 */
export const completeRepair = async (id: string) => {
  const res = await request({ url: `/api/repair/${id}/complete`, method: 'POST' }) as any
  return res?.data || res
}

/**
 * 开始处理报修（房东）
 */
export const startRepairProcess = async (id: string) => {
  const res = await request({ url: `/api/repair/${id}/process`, method: 'POST' }) as any
  return res?.data || res
}

/**
 * 取消报修
 */
export const cancelRepair = async (id: string) => {
  const res = await request({ url: `/api/repair/${id}/cancel`, method: 'POST' }) as any
  return res?.data || res
}