<template>
  <view class="login-container">
    <view class="login-header">
      <text class="title">租房管家</text>
      <text class="subtitle">让租房更简单</text>
    </view>

    <view class="login-form">
      <!-- 角色选择 -->
      <view class="role-selector">
        <view 
          class="role-item" 
          :class="{ active: role === 'landlord' }"
          @click="role = 'landlord'"
        >
          <text class="role-icon">🏠</text>
          <text class="role-text">我是房东</text>
        </view>
        <view 
          class="role-item" 
          :class="{ active: role === 'tenant' }"
          @click="role = 'tenant'"
        >
          <text class="role-icon">👤</text>
          <text class="role-text">我是租客</text>
        </view>
      </view>

      <!-- 手机号输入 -->
      <view class="form-item">
        <input 
          class="input" 
          type="number" 
          maxlength="11"
          v-model="phone" 
          placeholder="请输入手机号"
        />
      </view>

      <!-- 验证码输入 -->
      <view class="form-item code-input">
        <input 
          class="input" 
          type="number" 
          maxlength="6"
          v-model="code" 
          placeholder="请输入验证码"
        />
        <text class="send-code" @click="sendCode">
          {{ countdown > 0 ? `${countdown}s后重发` : '获取验证码' }}
        </text>
      </view>

      <!-- 登录按钮 -->
      <button class="login-btn" @click="handleLogin">登录</button>

      <!-- 微信登录 -->
      <view class="divider">
        <text class="divider-text">或</text>
      </view>

      <button class="wx-login-btn" @click="wxLogin">
        <text class="wx-icon">💚</text>
        <text class="wx-text">微信一键登录</text>
      </button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { phoneLogin, wxLogin } from '@/services/auth'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const role = ref<'landlord' | 'tenant'>('landlord')
const phone = ref('')
const code = ref('')
const countdown = ref(0)

/**
 * 发送验证码
 */
function sendCode() {
  if (!phone.value || phone.value.length !== 11) {
    uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
    return
  }

  if (countdown.value > 0) return

  // 开发环境：验证码固定为 123456
  uni.showToast({ title: '验证码：123456', icon: 'none', duration: 3000 })
  
  countdown.value = 60
  const timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
    }
  }, 1000)
}

/**
 * 手机号登录
 */
async function handleLogin() {
  if (!phone.value || !code.value) {
    uni.showToast({ title: '请填写完整信息', icon: 'none' })
    return
  }

  try {
    const user = await phoneLogin(phone.value, code.value, role.value)
    userStore.setUserInfo(user, user.token || '')
    uni.showToast({ title: '登录成功', icon: 'success' })
    
    // 跳转到对应首页
    setTimeout(() => {
      const url = role.value === 'landlord' 
        ? '/pages/landlord/index/index' 
        : '/pages/tenant/index/index'
      uni.switchTab({ url })
    }, 1500)
  } catch (error) {
    console.error('登录失败:', error)
  }
}

/**
 * 微信登录
 */
async function wxLogin() {
  // #ifdef MP-WEIXIN
  uni.login({
    provider: 'weixin',
    success: async (loginRes) => {
      try {
        const user = await wxLogin(loginRes.code)
        userStore.setUserInfo(user, user.token || '')
        uni.showToast({ title: '登录成功', icon: 'success' })
        
        setTimeout(() => {
          const url = role.value === 'landlord' 
            ? '/pages/landlord/index/index' 
            : '/pages/tenant/index/index'
          uni.switchTab({ url })
        }, 1500)
      } catch (error) {
        console.error('微信登录失败:', error)
      }
    },
    fail: (err) => {
      console.error('微信登录失败:', err)
      uni.showToast({ title: '登录失败', icon: 'none' })
    }
  })
  // #endif

  // #ifndef MP-WEIXIN
  uni.showToast({ title: '请在微信小程序中使用', icon: 'none' })
  // #endif
}
</script>

<style lang="scss" scoped>
.login-container {
  padding: 40rpx;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-header {
  text-align: center;
  margin-bottom: 80rpx;

  .title {
    display: block;
    font-size: 48rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 16rpx;
  }

  .subtitle {
    display: block;
    font-size: 28rpx;
    color: rgba(255, 255, 255, 0.8);
  }
}

.login-form {
  background: #fff;
  border-radius: 24rpx;
  padding: 40rpx;
}

.role-selector {
  display: flex;
  justify-content: space-around;
  margin-bottom: 40rpx;

  .role-item {
    flex: 1;
    text-align: center;
    padding: 20rpx;
    border: 2rpx solid #eee;
    border-radius: 12rpx;
    margin: 0 10rpx;
    transition: all 0.3s;

    &.active {
      border-color: #667eea;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

      .role-text {
        color: #fff;
      }
    }

    .role-icon {
      display: block;
      font-size: 48rpx;
      margin-bottom: 8rpx;
    }

    .role-text {
      font-size: 24rpx;
      color: #666;
    }
  }
}

.form-item {
  margin-bottom: 24rpx;

  .input {
    width: 100%;
    height: 88rpx;
    padding: 0 24rpx;
    background: #f5f5f5;
    border-radius: 12rpx;
    font-size: 28rpx;
  }

  &.code-input {
    display: flex;
    align-items: center;

    .input {
      flex: 1;
      margin-right: 20rpx;
    }

    .send-code {
      font-size: 24rpx;
      color: #667eea;
      white-space: nowrap;
    }
  }
}

.login-btn {
  width: 100%;
  height: 88rpx;
  line-height: 88rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  font-size: 32rpx;
  border-radius: 12rpx;
  border: none;
  margin-top: 40rpx;
}

.divider {
  display: flex;
  align-items: center;
  margin: 40rpx 0;

  &::before,
  &::after {
    content: '';
    flex: 1;
    height: 1rpx;
    background: #eee;
  }

  .divider-text {
    padding: 0 20rpx;
    font-size: 24rpx;
    color: #999;
  }
}

.wx-login-btn {
  width: 100%;
  height: 88rpx;
  line-height: 88rpx;
  background: #07c160;
  color: #fff;
  font-size: 32rpx;
  border-radius: 12rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;

  .wx-icon {
    margin-right: 12rpx;
    font-size: 36rpx;
  }
}
</style>
