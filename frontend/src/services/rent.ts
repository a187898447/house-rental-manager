import { request } from './index'
import type { RentBill, BillInput, ApiResponse } from '@/types'

/**
 * 获取租金账单列表（房东）
 */
export const getRentBills = async (params?: { 
  propertyId?: string
  tenantId?: string
  status?: string
  month?: string
}) => {
  const res = await request({ url: '/api/rent/bills/owner', method: 'GET', data: params }) as any
  const data = res?.data
  return data?.records ?? data?.list ?? []
}

/**
 * 获取账单详情
 */
export const getRentBillDetail = async (id: string) => {
  const res = await request({ url: `/api/rent/bills/${id}`, method: 'GET' }) as any
  return res?.data || res
}

/**
 * 生成账单
 */
export const generateRentBill = async (data: BillInput) => {
  return request({ url: '/api/rent/bills', method: 'POST', data })
}

/**
 * 标记账单已支付
 */
export const markBillPaid = async (id: string) => {
  return request({ url: `/api/rent/bills/${id}/pay`, method: 'POST' })
}

/**
 * 发送催租提醒
 */
export const sendRentReminder = async (id: string) => {
  return request({ url: `/api/rent/bills/${id}/remind`, method: 'POST' })
}
