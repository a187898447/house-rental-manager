import { request } from './index'

/**
 * 获取收据列表
 */
export const getReceiptList = async (params?: { 
  tenantId?: string
  propertyId?: string
  page?: number
  size?: number
}) => {
  const res = await request<any>({
    url: '/api/receipt/list',
    method: 'GET',
    data: params
  })
  return res?.records || res?.list || []
}

/**
 * 生成收据
 */
export const createReceipt = async (data: {
  rentRecordId: string
}) => {
  return request<any>({
    url: '/api/receipt',
    method: 'POST',
    data
  })
}

/**
 * 获取收据下载URL
 */
export const getReceiptDownloadUrl = async (id: string) => {
  return request<{ url: string }>({
    url: `/api/receipt/${id}`,
    method: 'GET'
  })
}
