import { useUserStore } from '@/stores/user'
import type { ApiResponse } from '@/types'

interface RequestOptions {
  url: string
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE'
  data?: any
  header?: Record<string, string>
}

const baseURL = 'https://api.example.com' // TODO: 配置实际后端地址

/**
 * 统一请求封装
 */
export const request = async <T = any>(options: RequestOptions): Promise<T> => {
  const userStore = useUserStore()
  
  const header: Record<string, string> = {
    'Content-Type': 'application/json',
    ...options.header
  }
  
  // 添加认证 Token
  if (userStore.token) {
    header['Authorization'] = `Bearer ${userStore.token}`
  }
  
  return new Promise<T>((resolve, reject) => {
    uni.request({
      url: baseURL + options.url,
      method: options.method || 'GET',
      data: options.data,
      header,
      success: (res) => {
        if (res.statusCode === 200) {
          const response = res.data as ApiResponse<T>
          if (response.code === 200 || response.code === 0) {
            resolve(response.data)
          } else {
            uni.showToast({
              title: response.message || '请求失败',
              icon: 'none'
            })
            reject(response)
          }
        } else if (res.statusCode === 401) {
          // Token 过期，跳转登录
          userStore.logout()
          uni.reLaunch({ url: '/pages/landlord/index/index' })
          reject(new Error('未授权'))
        } else {
          reject(res.data)
        }
      },
      fail: (err) => {
        uni.showToast({
          title: '网络请求失败',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}
