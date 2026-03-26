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
  const res = await request<any>({
    url: '/api/property/list',
    method: 'GET',
    data: params
  })
  console.log('getProperties res:', res)
  // 后端返回 Result<Page> 结构: { code, message, data: { records, total, ... } }
  const pageData = res?.data
  if (pageData && Array.isArray(pageData.records)) {
    return pageData.records
  }
  if (Array.isArray(res?.records)) {
    return res.records
  }
  if (Array.isArray(res?.list)) {
    return res.list
  }
  return []
}

/**
 * 获取房源详情
 */
export const getPropertyDetail = async (id: number) => {
  const res = await request<any>({
    url: `/api/property/${id}`,
    method: 'GET'
  })
  // 后端返回 Result<PropertyVO> 结构: { code, message, data: PropertyVO }
  return res?.data || res
}

/**
 * 添加房源
 */
export const createProperty = async (data: PropertyInput) => {
  return request<number>({
    url: '/api/property',
    method: 'POST',
    data
  })
}

/**
 * 更新房源
 */
export const updateProperty = async (id: number, data: Partial<PropertyInput>) => {
  return request<boolean>({
    url: `/api/property/${id}`,
    method: 'PUT',
    data
  })
}

/**
 * 删除房源
 */
export const deleteProperty = async (id: number) => {
  return request<boolean>({
    url: `/api/property/${id}`,
    method: 'DELETE'
  })
}