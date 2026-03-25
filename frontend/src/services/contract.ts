import { request } from './index'
import type { Contract, ApiResponse } from '@/types'

/**
 * 获取合同列表
 */
export const getContracts = async (params?: { tenantId?: string; propertyId?: string }) => {
  const res = await request<any>({
    url: '/api/contract',
    method: 'GET',
    data: params
  })
  return res?.records || res?.list || []
}

/**
 * 获取合同详情
 */
export const getContractDetail = async (id: string) => {
  return request<Contract>({
    url: `/api/contract/${id}`,
    method: 'GET'
  })
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
  return request<Contract>({
    url: '/api/contract',
    method: 'POST',
    data
  })
}

/**
 * 签署合同
 */
export const signContract = async (id: string, signImageUrl: string) => {
  return request<Contract>({
    url: `/api/contract/${id}/sign`,
    method: 'POST',
    data: { signImageUrl }
  })
}

/**
 * 获取合同签署 URL（用于微信小程序签名）
 */
export const getContractSignUrl = async (id: string) => {
  return request<{ url: string }>({
    url: `/api/contract/${id}/sign-url`,
    method: 'GET'
  })
}
