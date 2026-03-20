import { request } from './index'
import type { Property, PropertyInput, ApiResponse, PageResponse } from '@/types'

/**
 * 获取房源列表
 */
export const getProperties = async (params?: { status?: string; page?: number; pageSize?: number }) => {
  return request<Property[]>({
    url: '/api/properties',
    method: 'GET',
    data: params
  })
}

/**
 * 获取房源详情
 */
export const getPropertyDetail = async (id: string) => {
  return request<Property>({
    url: `/api/properties/${id}`,
    method: 'GET'
  })
}

/**
 * 新增房源
 */
export const createProperty = async (data: PropertyInput) => {
  return request<Property>({
    url: '/api/properties',
    method: 'POST',
    data
  })
}

/**
 * 编辑房源
 */
export const updateProperty = async (id: string, data: PropertyInput) => {
  return request<Property>({
    url: `/api/properties/${id}`,
    method: 'PUT',
    data
  })
}

/**
 * 删除房源
 */
export const deleteProperty = async (id: string) => {
  return request<{ success: boolean }>({
    url: `/api/properties/${id}`,
    method: 'DELETE'
  })
}

/**
 * 上传房源图片
 */
export const uploadPropertyImages = async (filePaths: string[]) => {
  return new Promise<string[]>((resolve, reject) => {
    const promises = filePaths.map((path) => {
      return new Promise<string>((resolve, reject) => {
        uni.uploadFile({
          url: 'https://api.example.com/api/upload',
          filePath: path,
          name: 'file',
          success: (res) => {
            const data = JSON.parse(res.data) as ApiResponse<{ url: string }>
            resolve(data.data.url)
          },
          fail: reject
        })
      })
    })
    
    Promise.all(promises).then(resolve).catch(reject)
  })
}
