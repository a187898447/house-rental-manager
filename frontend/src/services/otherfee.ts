// 其他费用服务 - OtherFee
import request from './index'

export interface OtherFee {
  id: number
  tenantId: number
  propertyId: number
  feeType: string
  amount: number
  month: string
  status: number // 0-待支付, 1-已支付
  payDate?: string
  remark?: string
  createdAt: string
}

// 获取其他费用列表（租客端）
export const getMyOtherFees = async (params?: {
  month?: string
  status?: number
  page?: number
  size?: number
}) => {
  return request<{ list: OtherFee[]; total: number }>({
    url: '/api/fee/tenant',
    method: 'GET',
    data: params
  })
}

// 获取其他费用列表（房东端）
export const getOwnerOtherFees = async (params?: {
  propertyId?: number
  tenantId?: number
  month?: string
  status?: number
  page?: number
  size?: number
}) => {
  return request<{ list: OtherFee[]; total: number }>({
    url: '/api/fee/owner',
    method: 'GET',
    data: params
  })
}

// 获取其他费用详情
export const getOtherFeeDetail = async (id: number) => {
  return request<OtherFee>({
    url: `/api/fee/${id}`,
    method: 'GET'
  })
}

// 创建其他费用
export const createOtherFee = async (data: {
  tenantId: number
  propertyId: number
  feeType: string
  amount: number
  month: string
  remark?: string
}) => {
  return request<{ id: number }>({
    url: '/api/fee',
    method: 'POST',
    data
  })
}

// 支付其他费用
export const payOtherFee = async (id: number) => {
  return request<{ success: boolean }>({
    url: `/api/fee/${id}/pay`,
    method: 'POST'
  })
}

// 删除其他费用
export const deleteOtherFee = async (id: number) => {
  return request<{ success: boolean }>({
    url: `/api/fee/${id}`,
    method: 'DELETE'
  })
}
