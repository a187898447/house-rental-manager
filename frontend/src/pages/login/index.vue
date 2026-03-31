<template>
  <view class="login-page">
    <!-- Logo -->
    <view class="logo-section">
      <image class="logo" src="" mode="aspectFit" />
      <text class="app-name">租房管理</text>
    </view>

    <!-- 角色选择 Tab -->
    <view class="role-tabs">
      <view 
        class="role-tab" 
        :class="{ active: loginRole === 'landlord' }"
        @click="switchRole('landlord')"
      >
        房东登录
      </view>
      <view 
        class="role-tab" 
        :class="{ active: loginRole === 'tenant' }"
        @click="switchRole('tenant')"
      >
        住户登录
      </view>
    </view>

    <!-- 登录表单 -->
    <view class="login-form">
      <view class="input-group">
        <input 
          v-model="phone" 
          type="number" 
          placeholder="请输入手机号" 
          class="phone-input"
        />
      </view>
      <view class="input-group code-group">
        <input 
          v-model="code" 
          type="number" 
          placeholder="请输入验证码" 
          class="code-input"
        />
        <button 
          class="send-code-btn" 
          :disabled="countdown > 0"
          @click="sendCode"
        >
          {{ countdown > 0 ? `${countdown}s` : '发送验证码' }}
        </button>
      </view>
      
      <!-- 登录按钮 -->
      <button 
        class="login-btn" 
        :loading="loading"
        @click="handleLogin"
      >
        <text v-if="!loading">{{ loginRole === 'landlord' ? '房东登录' : '住户登录' }}</text>
        <text v-else>登录中...</text>
      </button>

      <!-- 微信登录按钮 -->
      <button 
        class="wechat-btn" 
        :loading="wechatLoading"
        @click="handleWechatLogin"
      >
        微信一键登录
      </button>

      <!-- 用户协议 -->
      <view class="agreement">
        <checkbox 
          :checked="agreed" 
          @click="agreed = !agreed"
          color="#0087FF"
        />
        <text class="agreement-text">
          我已阅读并同意
          <text class="link" @click="openAgreement('user')">《用户协议》</text>
          和
          <text class="link" @click="openAgreement('privacy')">《隐私政策》</text>
        </text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
import { phoneLogin, sendVerifyCode, wechatLogin } from '@/services/auth'

const userStore = useUserStore()

const loading = ref(false)
const wechatLoading = ref(false)
const agreed = ref(false)

// 角色选择
const loginRole = ref<'landlord' | 'tenant'>('landlord')

// 表单数据
const phone = ref('')
const code = ref('')
const countdown = ref(0)

// 角色切换
const switchRole = (role: 'landlord' | 'tenant') => {
  loginRole.value = role
  phone.value = ''
  code.value = ''
  countdown.value = 0
}

// 发送验证码
const sendCode = async () => {
  if (!/^1\d{10}$/.test(phone.value)) {
    uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
    return
  }

  try {
    await sendVerifyCode(phone.value)
    uni.showToast({ title: '验证码已发送', icon: 'success' })
    
    countdown.value = 60
    const timer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) {
        clearInterval(timer)
      }
    }, 1000)
  } catch (error: any) {
    uni.showToast({ title: error.message || '发送失败', icon: 'none' })
  }
}

// 手机号登录
const handleLogin = async () => {
  if (!agreed.value) {
    uni.showToast({ title: '请先同意用户协议', icon: 'none' })
    return
  }
  
  if (!/^1\d{10}$/.test(phone.value)) {
    uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
    return
  }
  
  if (!/^\d{4,6}$/.test(code.value)) {
    uni.showToast({ title: '请输入验证码', icon: 'none' })
    return
  }

  loading.value = true
  try {
    const role = loginRole.value
    const res = await phoneLogin(phone.value, code.value, role) as any
    
    if (res.code === 200 || res.code === 0) {
      const data = res.data
      
      userStore.setToken(data.token)
      userStore.setUserInfo({
        id: data.userId,
        nickname: data.nickname || '用户',
        role: data.role || role,
        phone: data.phone,
        avatar: data.avatarUrl
      } as any)
      
      uni.showToast({ title: '登录成功', icon: 'success' })
      
      setTimeout(() => {
        if (role === 'landlord') {
          uni.switchTab({ url: '/pages/landlord/index/index' })
        } else {
          uni.switchTab({ url: '/pages/tenant/index/index' })
        }
      }, 1500)
    } else {
      uni.showToast({ title: res.message || '登录失败', icon: 'none' })
    }
  } catch (error: any) {
    uni.showToast({ title: error.message || '登录失败', icon: 'none' })
  } finally {
    loading.value = false
  }
}

// 试用模式
const handleDemoLogin = () => {
  const role = loginRole.value
  
  userStore.setToken("demo-token-" + role)
  userStore.setUserInfo({
    id: role === 'landlord' ? 1 : 100,
    nickname: role === 'landlord' ? '房东演示' : '住户演示',
    role: role,
    phone: '13800000000'
  })
  
  uni.showToast({ title: '已进入演示模式', icon: 'success' })
  
  setTimeout(() => {
    if (role === 'landlord') {
      uni.switchTab({ url: '/pages/landlord/index/index' })
    } else {
      uni.switchTab({ url: '/pages/tenant/index/index' })
    }
  }, 1000)
}

// 微信登录
const handleWechatLogin = async () => {
  if (!agreed.value) {
    uni.showToast({ title: '请先同意用户协议', icon: 'none' })
    return
  }

  wechatLoading.value = true
  try {
    const res = await wechatLogin() as any
    const data = res?.data || res
    
    if (!data.token) {
      throw new Error('登录失败：未获取到token')
    }
    
    userStore.setToken(data.token)
    userStore.setUserInfo({
      id: data.userId,
      nickname: data.nickname || '用户',
      role: data.role || 'tenant',
      phone: data.phone,
      avatar: data.avatarUrl
    } as any)
    
    uni.showToast({ title: '登录成功', icon: 'success' })
    setTimeout(() => {
      uni.switchTab({ url: '/pages/landlord/index/index' })
    }, 1500)
  } catch (error: any) {
    uni.showToast({ title: error.message || '登录失败', icon: 'none' })
  } finally {
    wechatLoading.value = false
  }
}

// 打开协议
const openAgreement = (type: string) => {
  uni.showToast({ title: '协议页面开发中', icon: 'none' })
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: #fff;
  padding: 48rpx 32rpx;
}

.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 64rpx;
}

.logo {
  width: 160rpx;
  height: 160rpx;
  background: #f5f5f5;
  border-radius: 32rpx;
}

.app-name {
  font-size: 40rpx;
  font-weight: 600;
  color: #333;
  margin-top: 24rpx;
}

.role-tabs {
  display: flex;
  background: #f5f5f5;
  border-radius: 16rpx;
  padding: 8rpx;
  margin-bottom: 48rpx;
}

.role-tab {
  flex: 1;
  text-align: center;
  padding: 24rpx 0;
  font-size: 28rpx;
  color: #666;
  border-radius: 12rpx;
  transition: all 0.3s;
}

.role-tab.active {
  background: #fff;
  color: #007AFF;
  font-weight: 600;
  box-shadow: 0 2rpx 8rpx rgba(0, 122, 255, 0.15);
}

.login-form {
  width: 100%;
}

.input-group {
  margin-bottom: 32rpx;
}

.phone-input,
.code-input {
  width: 100%;
  height: 96rpx;
  background: #f5f5f5;
  border-radius: 16rpx;
  padding: 0 32rpx;
  font-size: 28rpx;
}

.code-group {
  display: flex;
  align-items: center;
}

.code-input {
  flex: 1;
  margin-right: 24rpx;
}

.send-code-btn {
  width: 200rpx;
  height: 80rpx;
  background: #f5f5f5;
  border: none;
  border-radius: 12rpx;
  font-size: 24rpx;
  color: #007AFF;
  padding: 0;
}

.login-btn {
  width: 100%;
  height: 96rpx;
  background: #007AFF;
  color: #fff;
  border-radius: 16rpx;
  font-size: 32rpx;
  font-weight: 500;
  margin-bottom: 24rpx;
}

.demo-btn {
  width: 100%;
  height: 88rpx;
  background: #fff;
  border: 2rpx solid #ddd;
  border-radius: 16rpx;
  font-size: 28rpx;
  color: #666;
  margin-bottom: 24rpx;
}

.wechat-btn {
  width: 100%;
  height: 96rpx;
  background: #07C160;
  color: #fff;
  border-radius: 16rpx;
  font-size: 32rpx;
  font-weight: 500;
  margin-bottom: 48rpx;
}

.agreement {
  display: flex;
  align-items: center;
  justify-content: center;
}

.agreement-text {
  font-size: 24rpx;
  color: #999;
  margin-left: 16rpx;
}

.link {
  color: #007AFF;
}
</style>