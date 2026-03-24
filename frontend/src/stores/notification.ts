import { defineStore } from 'pinia'
import { ref } from 'vue'
import { 
  getNotificationList, 
  getUnreadCount, 
  markAsRead, 
  markAllAsRead,
  deleteNotification,
  type Notification
} from '@/services/notification'

export const useNotificationStore = defineStore('notification', () => {
  const notifications = ref<Notification[]>([])
  const unreadCount = ref(0)
  const loading = ref(false)

  const fetchNotifications = async (params?: { page?: number; pageSize?: number }) => {
    loading.value = true
    try {
      const res = await getNotificationList(params)
      notifications.value = res
    } finally {
      loading.value = false
    }
  }

  const fetchUnreadCount = async () => {
    try {
      const res = await getUnreadCount()
      unreadCount.value = res.count
    } catch (error) {
      console.error('获取未读数量失败:', error)
    }
  }

  const readNotification = async (id: string) => {
    await markAsRead(id)
    const notification = notifications.value.find(n => n.id === id)
    if (notification && !notification.isRead) {
      notification.isRead = true
      unreadCount.value = Math.max(0, unreadCount.value - 1)
    }
  }

  const readAllNotifications = async () => {
    await markAllAsRead()
    notifications.value.forEach(n => n.isRead = true)
    unreadCount.value = 0
  }

  const removeNotification = async (id: string) => {
    await deleteNotification(id)
    const index = notifications.value.findIndex(n => n.id === id)
    if (index !== -1) {
      if (!notifications.value[index].isRead) {
        unreadCount.value = Math.max(0, unreadCount.value - 1)
      }
      notifications.value.splice(index, 1)
    }
  }

  return {
    notifications,
    unreadCount,
    loading,
    fetchNotifications,
    fetchUnreadCount,
    readNotification,
    readAllNotifications,
    removeNotification
  }
})
