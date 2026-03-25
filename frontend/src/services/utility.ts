import { request } from './index'
import type { UtilityBill, ApiResponse } from '@/types'

/**
 * 获取水电费配置
 */
export const getUtilityConfig = async (propertyId: string) => {
  return request<any>({
    url: `/api/utility/config/${propertyId}`,
    method: 'GET'
  })
}

/**
 * 设置水电费单价
 */
export const setUtilityConfig = async (propertyId: string, data: { 
  waterRate: number
  electricityRate: number
}) => {
  return request<any>({
    url: `/api/utility/config/${propertyId}`,
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
}) => {
  const res = await request<any>({
    url: '/api/utility/owner',
    method: 'GET',
    data: params
  })
  return res?.records || res?.list || []
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