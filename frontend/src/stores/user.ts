import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { User } from '@/types'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref<User | null>(null)
  const token = ref<string>('')
  const role = ref<'landlord' | 'tenant'>('landlord')

  // 设置用户信息
  const setUserInfo = (info: User) => {
    userInfo.value = info
    role.value = info.role
    uni.setStorageSync('userInfo', info)
  }

  // 设置 Token
  const setToken = (newToken: string) => {
    token.value = newToken
    uni.setStorageSync('token', newToken)
  }

  // 初始化（从本地存储恢复）
  const init = () => {
    const savedToken = uni.getStorageSync('token')
    if (savedToken) {
      token.value = savedToken
    }
    
    const savedUserInfo = uni.getStorageSync('userInfo')
    if (savedUserInfo) {
      userInfo.value = savedUserInfo
      role.value = savedUserInfo.role
    }
  }

  // 登录
  const login = async () => {
    // TODO: 调用微信登录 API
    // const loginRes = await uni.login()
    // 调用后端登录接口
  }

  // 登出
  const logout = () => {
    userInfo.value = null
    token.value = ''
    role.value = 'landlord'
    uni.removeStorageSync('token')
    uni.removeStorageSync('userInfo')
  }

  return {
    userInfo,
    token,
    role,
    setUserInfo,
    setToken,
    init,
    login,
    logout
  }
})
