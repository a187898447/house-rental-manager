import { request } from './index'
import type { Contract, ApiResponse } from '@/types'

/**
 * 获取合同列表
 */
export const getContracts = async (params?: { tenantId?: string; propertyId?: string }) => {
  const res = await request({ url: '/api/contract', method: 'GET', data: params }) as any
  const data = res?.data
  return data?.records ?? data?.list ?? []
}

/**
 * 获取合同详情
 */
export const getContractDetail = async (id: string) => {
  const res = await request({ url: `/api/contract/${id}`, method: 'GET' }) as any
  return res?.data || res
}

/**
 * 创建合同
 */
export const createContract = async (data: {
  tenantId: string
  propertyId: string
  rentAmount: number
  depositAmount: number
  waterFee: number
  electricityFee: number
  leaseStart: string
  leaseEnd: string
}) => {
  const res = await request({ url: '/api/contract', method: 'POST', data }) as any
  return res?.data || res
}

/**
 * 签署合同
 */
export const signContract = async (id: string, signImageUrl: string) => {
  const res = await request({ url: `/api/contract/${id}/sign`, method: 'POST', data: { signImageUrl } }) as any
  return res?.data || res
}

/**
 * 获取合同签署 URL（用于微信小程序签名）
 */
export const getContractSignUrl = async (id: string) => {
  const res = await request({ url: `/api/contract/${id}/sign-url`, method: 'GET' }) as any
  return res?.data || res
}
