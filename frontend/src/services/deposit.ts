import { request } from './index'
import type { Deposit, ApiResponse } from '@/types'

/**
 * 获取押金列表
 */
export const getDeposits = async (params?: { propertyId?: string; tenantId?: string; status?: string }) => {
  const res = await request({ url: '/api/deposit/owner', method: 'GET', data: params }) as any
  const data = res?.data
  return data?.records ?? data?.list ?? []
}

/**
 * 获取押金详情
 */
export const getDepositDetail = async (id: string) => {
  const res = await request({ url: `/api/deposit/${id}`, method: 'GET' }) as any
  return res?.data || res
}

/**
 * 收取押金
 */
export const createDeposit = async (data: {
  propertyId: string
  tenantId: string
  amount: number
}) => {
  const res = await request({ url: '/api/deposit', method: 'POST', data }) as any
  return res?.data || res
}

/**
 * 退还押金
 */
export const refundDeposit = async (id: string, data: { amount: number; remark?: string }) => {
  const res = await request({ url: `/api/deposit/${id}/refund`, method: 'POST', data }) as any
  return res?.data || res
}

/**
 * 确认收款（缴纳押金）
 */
export const payDeposit = async (id: string, payMethod: string) => {
  const res = await request({ url: `/api/deposit/${id}/pay?payMethod=${payMethod}`, method: 'POST' }) as any
  return res?.data || res
}