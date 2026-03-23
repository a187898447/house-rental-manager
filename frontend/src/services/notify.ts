// 通知服务 - Notify
import request from './index'

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
  return request<{ list: NotifyMessage[]; total: number }>({
    url: '/api/notify/list',
    method: 'GET',
    data: params
  })
}

// 获取未读数量
export const getUnreadCount = async () => {
  return request<{ count: number }>({
    url: '/api/notify/unread-count',
    method: 'GET'
  })
}

// 标记已读
export const markNotifyRead = async (id: number) => {
  return request<{ success: boolean }>({
    url: `/api/notify/${id}/read`,
    method: 'PUT'
  })
}

// 全部标记已读
export const markAllRead = async () => {
  return request<{ success: boolean }>({
    url: '/api/notify/read-all',
    method: 'PUT'
  })
}

// 删除通知
export const deleteNotify = async (id: number) => {
  return request<{ success: boolean }>({
    url: `/api/notify/${id}`,
    method: 'DELETE'
  })
}
