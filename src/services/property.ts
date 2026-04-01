import { post, get, put, del } from './index'
import type { Property, PropertyDTO, PageResult } from '@/types'

/**
 * 添加房源
 */
export function addProperty(data: PropertyDTO, landlordId: number) {
  return post<number>(`/property?landlordId=${landlordId}`, data)
}

/**
 * 编辑房源
 */
export function updateProperty(data: PropertyDTO) {
  return put('/property', data)
}

/**
 * 删除房源
 */
export function deleteProperty(propertyId: number) {
  return del(`/property/${propertyId}`)
}

/**
 * 查询房源列表
 */
export function getPropertyList(
  landlordId: number,
  status?: number,
  pageNum: number = 1,
  pageSize: number = 10
) {
  return get<PageResult<Property>>('/property/list', {
    landlordId,
    status,
    pageNum,
    pageSize,
  })
}

/**
 * 查询房源详情
 */
export function getPropertyDetail(propertyId: number) {
  return get<Property>(`/property/${propertyId}`)
}
