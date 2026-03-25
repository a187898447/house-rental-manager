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
  return res?.records || res?.list || []
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
  return res?.records || res?.list || []
}

/**
 * 获取报修详情
 */
export const getRepairDetail = async (id: string) => {
  return request<Repair>({
    url: `/api/repair/${id}`,
    method: 'GET'
  })
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
  return request<Repair>({
    url: '/api/repair',
    method: 'POST',
    data
  })
}

/**
 * 更新报修状态
 */
export const updateRepairStatus = async (id: string, data: { 
  status: 'processing' | 'completed' | 'cancelled'
  remark?: string
}) => {
  return request<Repair>({
    url: `/api/repair/${id}/status`,
    method: 'PUT',
    data
  })
}

/**
 * 开始处理报修
 */
export const handleRepair = async (id: string) => {
  return request<Repair>({
    url: `/api/repair/${id}/process`,
    method: 'POST'
  })
}

/**
 * 完成报修处理
 */
export const completeRepair = async (id: string) => {
  return request<Repair>({
    url: `/api/repair/${id}/complete`,
    method: 'POST'
  })
}