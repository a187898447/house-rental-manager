import { request } from './index'
import type { RentBill, BillInput, ApiResponse } from '@/types'

/**
 * 获取租金账单列表
 */
export const getRentBills = async (params?: { 
  propertyId?: string
  tenantId?: string
  status?: string
  month?: string
}) => {
  return request<RentBill[]>({
    url: '/api/rent/bills',
    method: 'GET',
    data: params
  })
}

/**
 * 获取账单详情
 */
export const getRentBillDetail = async (id: string) => {
  return request<RentBill>({
    url: `/api/rent/bills/${id}`,
    method: 'GET'
  })
}

/**
 * 生成账单
 */
export const generateRentBill = async (data: BillInput) => {
  return request<RentBill>({
    url: '/api/rent/bills',
    method: 'POST',
    data
  })
}

/**
 * 标记账单已支付
 */
export const markBillPaid = async (id: string) => {
  return request<RentBill>({
    url: `/api/rent/bills/${id}/pay`,
    method: 'POST'
  })
}

/**
 * 发送催租提醒
 */
export const sendRentReminder = async (id: string) => {
  return request<{ success: boolean }>({
    url: `/api/rent/bills/${id}/remind`,
    method: 'POST'
  })
}
