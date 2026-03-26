// 收据服务
import { request } from './index'

/**
 * 获取收据列表（房东端）
 */
export const getOwnerReceipts = async (params?: {
  type?: string
  month?: string
  page?: number
  size?: number
}) => {
  const res = await request<any>({
    url: '/api/receipt/owner',
    method: 'GET',
    data: params
  })
  return res?.records || res?.list || []
}

/**
 * 获取收据详情
 */
export const getReceiptDetail = async (id: string) => {
  return request<any>({
    url: `/api/receipt/${id}`,
    method: 'GET'
  })
}