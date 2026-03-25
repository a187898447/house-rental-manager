import { request } from './index'
import type { UtilityBill, ApiResponse } from '@/types'

/**
 * 获取水电费配置
 */
export const getUtilityConfig = async (propertyId: string) => {
  return request<{ waterRate: number; electricityRate: number }>({
    url: `/api/properties/${propertyId}/utility-config`,
    method: 'GET'
  })
}

/**
 * 设置水电费单价
 */
export const setUtilityConfig = async (propertyId: string, data: { waterRate: number; electricityRate: number }) => {
  return request<{ success: boolean }>({
    url: `/api/properties/${propertyId}/utility-config`,
    method: 'PUT',
    data
  })
}

/**
 * 获取水电费账单
 */
export const getUtilityBills = async (params?: {
  propertyId?: number
  tenantId?: number
  status?: number
  month?: string
  page?: number
  size?: number
}) => {
  return request<{ records: UtilityBill[]; total: number; size: number; current: number }>({
    url: '/api/utility/owner',
    method: 'GET',
    data: params
  })
}

/**
 * 录入水电表读数
 */
export const createUtilityBill = async (data: {
  propertyId: number
  tenantId: number
  billMonth: string
  waterReading?: number
  waterReadingCurrent?: number
  waterAmount?: number
  electricityReading?: number
  electricityReadingCurrent?: number
  electricityAmount?: number
  source?: number
}) => {
  return request<UtilityBill>({
    url: '/api/utility/bills',
    method: 'POST',
    data
  })
}

/**
 * 标记水电费已支付
 */
export const markUtilityPaid = async (id: string) => {
  return request<UtilityBill>({
    url: `/api/utility/${id}/pay`,
    method: 'POST'
  })
}
