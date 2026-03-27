// 通知服务 - Notify
import { request } from './index'

export interface NotifyMessage {
  id: number
  userId: number
  senderId?: number
  type: string
  title: string
  content: string
  relatedId?: number
  relatedType?: string
  isRead: boolean
  readAt?: string
  createdAt: string
}

// 获取通知列表
export const getNotifyList = async (params?: {
  type?: string
  isRead?: boolean
  page?: number
  size?: number
}) => {
  const res = await request({ url: '/api/notify/list', method: 'GET', data: params }) as any
  const data = res?.data
  return data?.records ?? data?.list ?? []
}

// 获取未读数量
export const getUnreadCount = async () => {
  return request({ url: '/api/notify/unread-count', method: 'GET' })
}

// 标记已读
export const markNotifyRead = async (id: number) => {
  return request({ url: `/api/notify/${id}/read`, method: 'PUT' })
}

// 全部标记已读
export const markAllRead = async () => {
  return request({ url: '/api/notify/read-all', method: 'PUT' })
}

// 删除通知
export const deleteNotify = async (id: number) => {
  return request({ url: `/api/notify/${id}`, method: 'DELETE' })
}
