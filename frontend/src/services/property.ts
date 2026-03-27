import { request } from './index'
import type { Property, PropertyInput, PageResponse, ApiResponse } from '@/types'

/**
 * 获取房源列表
 */
export const getProperties = async (params?: { 
  ownerId?: number
  status?: number
  page?: number
  size?: number 
}) => {
  const res = await request({
    url: '/api/property/list',
    method: 'GET',
    data: params
  }) as any
  console.log('getProperties raw res:', res)
  // 后端返回 Result<Page> 结构: { code, message, data: { records, total, ... } }
  // request 返回的是 res.data，所以 res 已经是 { code, message, data: {...}, timestamp }
  const data = res?.data
  const records = data?.records ?? data?.list ?? []
  console.log('getProperties extracted records:', records)
  return Array.isArray(records) ? records : []
}

/**
 * 获取房源详情
 */
export const getPropertyDetail = async (id: number) => {
  const res = await request({ url: `/api/property/${id}`, method: 'GET' }) as any
  // 后端返回 Result<PropertyVO> 结构: { code, message, data: PropertyVO }
  return res?.data || res
}

/**
 * 添加房源
 */
export const createProperty = async (data: PropertyInput) => {
  return request({ url: '/api/property', method: 'POST', data })
}

/**
 * 更新房源
 */
export const updateProperty = async (id: number, data: Partial<PropertyInput>) => {
  return request({ url: `/api/property/${id}`, method: 'PUT', data })
}

/**
 * 删除房源
 */
export const deleteProperty = async (id: number) => {
  return request({ url: `/api/property/${id}`, method: 'DELETE' })
}