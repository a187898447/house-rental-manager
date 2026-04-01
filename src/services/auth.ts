import { post, get } from './index'
import type { Result, User } from '@/types'

/**
 * 微信登录
 */
export function wxLogin(code: string, nickname?: string, avatarUrl?: string) {
  return post<User>('/user/wx-login', {
    openid: code,
    nickname,
    avatarUrl,
  })
}

/**
 * 手机号登录
 */
export function phoneLogin(phone: string, code: string, role: 'landlord' | 'tenant') {
  return post<User>('/user/phone-login', null, {
    params: { phone, code, role }
  })
}

/**
 * 获取用户信息
 */
export function getUserInfo(userId: number) {
  return get<User>(`/user/info?userId=${userId}`)
}
