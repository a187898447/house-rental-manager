import { request } from './index'
import type { Tenant, ApiResponse } from '@/types'

/**
 * 获取租客列表
 */
export const getTenants = async (params?: { propertyId?: string; status?: string }) => {
  if (params?.propertyId) {
    const res = await request<any>({
      url: `/api/tenant/property/${params.propertyId}`,
      method: 'GET'
    })
    return res?.data?.records || res?.records || res?.list || []
  }
  const res = await request<any>({
    url: '/api/tenant/owner/list',
    method: 'GET',
    data: params
  })
  return res?.data?.records || res?.records || res?.list || []
}

/**
 * 获取租客详情
 */
export const getTenantDetail = async (id: string) => {
  const res = await request<any>({
    url: `/api/tenant/${id}`,
    method: 'GET'
  })
  return res?.data || res
}

/**
 * 入住登记
 */
export const checkIn = async (data: {
  propertyId: string
  name: string
  phone: string
  idCard?: string
  leaseStart: string
  leaseEnd: string
  emergencyContact?: string
  emergencyPhone?: string
}) => {
  const res = await request<any>({
    url: '/api/tenant/checkin',
    method: 'POST',
    data
  })
  return res?.data || res
}

/**
 * 退租办理
 */
export const checkOut = async (id: string, data?: { remark?: string }) => {
  const res = await request<any>({
    url: `/api/tenant/${id}/checkout`,
    method: 'POST',
    data
  })
  return res?.data || res
}

/**
 * 删除租客
 */
export const deleteTenant = async (id: string) => {
  const res = await request<any>({
    url: `/api/tenant/${id}`,
    method: 'DELETE'
  })
  return res?.data || res
}