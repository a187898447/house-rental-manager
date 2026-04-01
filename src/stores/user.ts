import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { User } from '@/types'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref<User | null>(null)
  const token = ref<string>('')

  /**
   * 设置用户信息
   */
  function setUserInfo(user: User, userToken: string) {
    userInfo.value = user
    token.value = userToken
    uni.setStorageSync('token', userToken)
    uni.setStorageSync('userInfo', JSON.stringify(user))
  }

  /**
   * 退出登录
   */
  function logout() {
    userInfo.value = null
    token.value = ''
    uni.removeStorageSync('token')
    uni.removeStorageSync('userInfo')
  }

  /**
   * 初始化用户信息
   */
  function initUserInfo() {
    const savedToken = uni.getStorageSync('token')
    const savedUserInfo = uni.getStorageSync('userInfo')
    if (savedToken && savedUserInfo) {
      token.value = savedToken
      userInfo.value = JSON.parse(savedUserInfo)
    }
  }

  return {
    userInfo,
    token,
    setUserInfo,
    logout,
    initUserInfo,
  }
})
