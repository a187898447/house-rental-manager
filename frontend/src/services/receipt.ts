import { request } from './index'
import type { ApiResponse } from '@/types'

/**
 * 获取收据列表
 */
export const getReceipts = async (params?: {
  type?: string
  month?: string
  propertyId?: number
  tenantId?: number
}) => {
  return request<any[]>({
    url: '/api/receipt/list',
    method: 'GET',
    data: params
  })
}

/**
 * 生成收据
 */
export const createReceipt = async (data: {
  tenantId: number
  propertyId: number
  type: string
  amount: number
  billMonth?: string
}) => {
  return request<{ id: number }>({
    url: '/api/receipt',
    method: 'POST',
    data
  })
}

/**
 * 导出收据 PDF
 */
export const exportReceiptPdf = async (id: number) => {
  return request<{ url: string }>({
    url: `/api/receipt/${id}/export`,
    method: 'GET'
  })
}