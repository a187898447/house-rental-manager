import { request } from './index'
import type { Tenant, ApiResponse } from '@/types'

/**
 * 获取租客列表
 */
export const getTenants = async (params?: { propertyId?: string; status?: string }) => {
  if (params?.propertyId) {
    return request<Tenant[]>({
      url: `/api/tenant/property/${params.propertyId}`,
      method: 'GET'
    })
  }
  return request<Tenant[]>({
    url: '/api/tenant/owner/list',
    method: 'GET',
    data: params
  })
}

/**
 * 获取租客详情
 */
export const getTenantDetail = async (id: string) => {
  return request<Tenant>({
    url: `/api/tenant/${id}`,
    method: 'GET'
  })
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
  return request<Tenant>({
    url: '/api/tenant/checkin',
    method: 'POST',
    data
  })
}

/**
 * 退租办理
 */
export const checkOut = async (id: string, data?: { remark?: string }) => {
  return request<Tenant>({
    url: `/api/tenant/${id}/checkout`,
    method: 'POST',
    data
  })
}

/**
 * 删除租客
 */
export const deleteTenant = async (id: string) => {
  return request<{ success: boolean }>({
    url: `/api/tenant/${id}`,
    method: 'DELETE'
  })
}
