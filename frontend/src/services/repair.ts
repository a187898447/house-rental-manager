import { request } from './index'
import type { Repair, ApiResponse } from '@/types'

/**
 * 获取报修列表（租客）
 */
export const getRepairs = async (params?: { 
  propertyId?: string
  tenantId?: string
  status?: string
}) => {
  const res = await request<any>({
    url: '/api/repair',
    method: 'GET',
    data: params
  })
  return res?.data?.records || res?.records || res?.list || []
}

/**
 * 获取报修列表（房东）
 */
export const getOwnerRepairs = async (params?: {
  propertyId?: string
  status?: string
}) => {
  const res = await request<any>({
    url: '/api/repair/owner',
    method: 'GET',
    data: params
  })
  return res?.data?.records || res?.records || res?.list || []
}

/**
 * 获取报修详情
 */
export const getRepairDetail = async (id: string) => {
  const res = await request<any>({
    url: `/api/repair/${id}`,
    method: 'GET'
  })
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
  const res = await request<any>({
    url: '/api/repair',
    method: 'POST',
    data
  })
  return res?.data || res
}

/**
 * 更新报修状态
 */
export const updateRepairStatus = async (id: string, data: { 
  status: 'processing' | 'completed' | 'cancelled'
  remark?: string
}) => {
  const res = await request<any>({
    url: `/api/repair/${id}/status`,
    method: 'PUT',
    data
  })
  return res?.data || res
}

/**
 * 开始处理报修
 */
export const handleRepair = async (id: string) => {
  const res = await request<any>({
    url: `/api/repair/${id}/process`,
    method: 'POST'
  })
  return res?.data || res
}

/**
 * 完成报修处理
 */
export const completeRepair = async (id: string) => {
  const res = await request<any>({
    url: `/api/repair/${id}/complete`,
    method: 'POST'
  })
  return res?.data || res
}

/**
 * 开始处理报修（房东）
 */
export const startRepairProcess = async (id: string) => {
  const res = await request<any>({
    url: `/api/repair/${id}/process`,
    method: 'POST'
  })
  return res?.data || res
}

/**
 * 取消报修
 */
export const cancelRepair = async (id: string) => {
  const res = await request<any>({
    url: `/api/repair/${id}/cancel`,
    method: 'POST'
  })
  return res?.data || res
}