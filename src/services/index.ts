import type { Result, PageResult } from '@/types'

const BASE_URL = '/api'

/**
 * 请求封装
 */
async function request<T>(
  url: string,
  options: RequestInit = {}
): Promise<T> {
  const token = uni.getStorageSync('token')
  
  const response = await fetch(BASE_URL + url, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...options.headers,
    },
  })

  const result: Result<T> = await response.json()
  
  if (result.code !== 200) {
    uni.showToast({
      title: result.message || '请求失败',
      icon: 'none'
    })
    throw new Error(result.message)
  }

  return result.data
}

/**
 * GET 请求
 */
export function get<T>(url: string, params?: Record<string, any>): Promise<T> {
  const queryString = params ? '?' + new URLSearchParams(params).toString() : ''
  return request<T>(url + queryString, { method: 'GET' })
}

/**
 * POST 请求
 */
export function post<T>(url: string, data?: any): Promise<T> {
  return request<T>(url, {
    method: 'POST',
    body: JSON.stringify(data),
  })
}

/**
 * PUT 请求
 */
export function put<T>(url: string, data?: any): Promise<T> {
  return request<T>(url, {
    method: 'PUT',
    body: JSON.stringify(data),
  })
}

/**
 * DELETE 请求
 */
export function del<T>(url: string): Promise<T> {
  return request<T>(url, { method: 'DELETE' })
}
