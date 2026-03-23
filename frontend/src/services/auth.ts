import { request } from './index'
import type { LoginResponse, User, ApiResponse } from '@/types'

/**
 * 微信登录
 */
export const wechatLogin = async () => {
  const loginRes = await uni.login()
  
  return request<LoginResponse>({
    url: '/api/user/login',
    method: 'POST',
    data: { code: loginRes.code }
  })
}

/**
 * 绑定手机号
 */
export const bindPhone = async (phone: string, code: string) => {
  return request<LoginResponse>({
    url: '/api/user/bind-phone',
    method: 'POST',
    data: { phone, code }
  })
}

/**
 * 获取用户信息
 */
export const getUserInfo = async () => {
  return request<User>({
    url: '/api/user/info',
    method: 'GET'
  })
}

/**
 * 更新用户信息
 */
export const updateUserInfo = async (data: Partial<User>) => {
  return request<User>({
    url: '/api/user',
    method: 'PUT',
    data
  })
}

/**
 * 发送验证码
 */
export const sendVerifyCode = async (phone: string) => {
  return request<{ success: boolean }>({
    url: '/api/user/send-code',
    method: 'POST',
    data: { phone }
  })
}
