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
  const res = await request({ url: '/api/receipt/owner', method: 'GET', data: params }) as any
  const data = res?.data
  return data?.records ?? data?.list ?? []
}

/**
 * 获取收据详情
 */
export const getReceiptDetail = async (id: string) => {
  const res = await request({ url: `/api/receipt/${id}`, method: 'GET' }) as any
  return res?.data || res
}