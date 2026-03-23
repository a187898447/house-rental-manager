import { request } from './index'
import type { Deposit, ApiResponse } from '@/types'

/**
 * 获取押金列表
 */
export const getDeposits = async (params?: { propertyId?: string; tenantId?: string }) => {
  return request<Deposit[]>({
    url: '/api/deposit/tenant',
    method: 'GET',
    data: params
  })
}

/**
 * 收取押金
 */
export const createDeposit = async (data: {
  propertyId: string
  tenantId: string
  amount: number
}) => {
  return request<Deposit>({
    url: '/api/deposit',
    method: 'POST',
    data
  })
}

/**
 * 退还押金
 */
export const refundDeposit = async (id: string, data: { amount: number; remark?: string }) => {
  return request<Deposit>({
    url: `/api/deposit/${id}/refund`,
    method: 'POST',
    data
  })
}
