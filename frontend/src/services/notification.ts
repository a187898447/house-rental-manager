import { request } from './index'
import type { ApiResponse } from '@/types'

export interface Notification {
  id: string
  type: 'rent_remind' | 'pay_remind' | 'appointment' | 'repair' | 'contract' | 'system'
  title: string
  content: string
  isRead: boolean
  createTime: string
  relatedId?: string
  relatedType?: string
}

export interface NotificationListParams {
  page?: number
  pageSize?: number
  type?: string
  isRead?: boolean
}

/**
 * 获取通知列表
 */
export const getNotificationList = async (params?: NotificationListParams) => {
  const res = await request({ url: '/api/notify/list', method: 'GET', data: params }) as any
  const data = res?.data
  return data?.records ?? data?.list ?? []
}

/**
 * 获取未读数量
 */
export const getUnreadCount = async () => {
  return request({ url: '/api/notify/unread-count', method: 'GET' })
}

/**
 * 标记单条通知为已读
 */
export const markAsRead = async (id: string) => {
  return request({ url: `/api/notify/${id}/read`, method: 'PUT' })
}

/**
 * 标记全部通知为已读
 */
export const markAllAsRead = async () => {
  return request({ url: '/api/notify/read-all', method: 'PUT' })
}

/**
 * 删除通知
 */
export const deleteNotification = async (id: string) => {
  return request({ url: `/api/notify/${id}`, method: 'DELETE' })
}

/**
 * 获取通知设置
 */
export const getNotificationSettings = async () => {
  const res = await request({ url: '/api/notify/settings', method: 'GET' }) as any
  return res?.data ?? res
}

/**
 * 更新通知设置
 */
export const updateNotificationSettings = async (data: {
  notifyDays: number
  smsEnabled: boolean
  wechatEnabled: boolean
}) => {
  return request({ url: '/api/notify/settings', method: 'PUT', data })
}
