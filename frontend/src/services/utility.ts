import { request } from './index'
import type { UtilityFee, ApiResponse } from '@/types'

/**
 * 获取水电费配置
 */
export const getUtilityConfig = async (propertyId: string) => {
  return request<{ waterRate: number; electricityRate: number }>({
    url: `/api/property/${propertyId}/utility-config`,
    method: 'GET'
  })
}

/**
 * 设置水电费单价
 */
export const setUtilityConfig = async (propertyId: string, data: { waterRate: number; electricityRate: number }) => {
  return request<{ success: boolean }>({
    url: `/api/property/${propertyId}/utility-config`,
    method: 'PUT',
    data
  })
}

/**
 * 获取水电费账单
 */
export const getUtilityBills = async (params?: {
  propertyId?: string
  tenantId?: string
  status?: string
  page?: number
  pageSize?: number
}) => {
  return request<UtilityFee[]>({
    url: '/api/utility/bills',
    method: 'GET',
    data: params
  })
}

/**
 * 录入水电表读数
 */
export const createUtilityBill = async (data: {
  propertyId: string
  tenantId: string
  billMonth: string
  waterReading?: number
  electricityReading?: number
  waterAmount?: number
  electricityAmount?: number
  remark?: string
}) => {
  return request<UtilityFee>({
    url: '/api/utility/bills',
    method: 'POST',
    data
  })
}

/**
 * 标记水电费已支付
 */
export const markUtilityPaid = async (id: string) => {
  return request<UtilityFee>({
    url: `/api/utility/bills/${id}/pay`,
    method: 'POST'
  })
}
