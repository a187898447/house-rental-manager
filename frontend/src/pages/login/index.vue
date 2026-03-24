<template>
  <view class="login-page">
    <!-- Logo -->
    <view class="logo-section">
      <image class="logo" src="" mode="aspectFit" />
      <text class="app-name">租房管理</text>
    </view>

    <!-- 登录按钮 -->
    <view class="login-section">
      <button 
        class="wechat-btn" 
        :loading="loading"
        @click="handleWechatLogin"
      >
        <text v-if="!loading">微信一键登录</text>
        <text v-else>登录中...</text>
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

    <!-- 手机号绑定弹窗 -->
    <view v-if="showBindPhone" class="bind-popup">
      <view class="mask" @click="showBindPhone = false" />
      <view class="popup-content">
        <text class="popup-title">绑定手机号</text>
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
        <button class="bind-btn" :loading="binding" @click="handleBind">
          绑定并登录
        </button>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
import { wechatLogin, bindPhone, sendVerifyCode } from '@/services/auth'

const userStore = useUserStore()

const loading = ref(false)
const binding = ref(false)
const agreed = ref(false)

// 手机号绑定
const showBindPhone = ref(false)
const phone = ref('')
const code = ref('')
const countdown = ref(0)
let tempToken = ''

// 微信登录
const handleWechatLogin = async () => {
  if (!agreed.value) {
    uni.showToast({ title: '请先同意用户协议', icon: 'none' })
    return
  }

  loading.value = true
  try {
    const res = await wechatLogin()
    
    if (res.needBindPhone) {
      // 需要绑定手机号
      tempToken = res.token
      showBindPhone.value = true
    } else {
      // 登录成功
      userStore.setToken(res.token)
      if (res.userInfo) {
        userStore.setUserInfo(res.userInfo)
      }
      uni.showToast({ title: '登录成功', icon: 'success' })
      setTimeout(() => {
        uni.switchTab({ url: '/pages/landlord/index/index' })
      }, 1500)
    }
  } catch (error: any) {
    uni.showToast({ title: error.message || '登录失败', icon: 'none' })
  } finally {
    loading.value = false
  }
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
    
    // 开始倒计时
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

// 绑定手机号
const handleBind = async () => {
  if (!/^1\d{10}$/.test(phone.value)) {
    uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
    return
  }
  if (!/^\d{4,6}$/.test(code.value)) {
    uni.showToast({ title: '请输入验证码', icon: 'none' })
    return
  }

  binding.value = true
  try {
    const res = await bindPhone(phone.value, code.value)
    userStore.setToken(res.token)
    if (res.userInfo) {
      userStore.setUserInfo(res.userInfo)
    }
    showBindPhone.value = false
    uni.showToast({ title: '绑定成功', icon: 'success' })
    setTimeout(() => {
      uni.switchTab({ url: '/pages/landlord/index/index' })
    }, 1500)
  } catch (error: any) {
    uni.showToast({ title: error.message || '绑定失败', icon: 'none' })
  } finally {
    binding.value = false
  }
}

// 查看协议
const openAgreement = (type: string) => {
  // TODO: 跳转到协议页面
  console.log('open agreement:', type)
}
</script>

<style scoped lang="scss">
.login-page {
  min-height: 100vh;
  background-color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 200rpx 60rpx 0;
}

.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 120rpx;

  .logo {
    width: 160rpx;
    height: 160rpx;
    margin-bottom: 24rpx;
  }

  .app-name {
    font-size: 40rpx;
    font-weight: bold;
    color: #333;
  }
}

.login-section {
  width: 100%;

  .wechat-btn {
    width: 100%;
    height: 96rpx;
    background-color: #07C160;
    border-radius: 48rpx;
    color: #fff;
    font-size: 32rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    border: none;
    
    &::after {
      border: none;
    }
  }

  .agreement {
    display: flex;
    align-items: flex-start;
    margin-top: 40rpx;
    padding: 0 20rpx;

    .agreement-text {
      font-size: 24rpx;
      color: #999;
      line-height: 1.6;
      margin-left: 12rpx;

      .link {
        color: #0087FF;
      }
    }
  }
}

.bind-popup {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 999;

  .mask {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
  }

  .popup-content {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 600rpx;
    background-color: #fff;
    border-radius: 24rpx;
    padding: 60rpx 40rpx;

    .popup-title {
      font-size: 36rpx;
      font-weight: bold;
      color: #333;
      display: block;
      text-align: center;
      margin-bottom: 40rpx;
    }

    .input-group {
      margin-bottom: 24rpx;

      .phone-input,
      .code-input {
        width: 100%;
        height: 88rpx;
        background-color: #f5f5f5;
        border-radius: 12rpx;
        padding: 0 24rpx;
        font-size: 28rpx;
      }
    }

    .code-group {
      display: flex;
      gap: 16rpx;

      .code-input {
        flex: 1;
      }

      .send-code-btn {
        width: 200rpx;
        height: 88rpx;
        background-color: #f5f5f5;
        border-radius: 12rpx;
        font-size: 24rpx;
        color: #0087FF;
        border: none;
        padding: 0;

        &::after {
          border: none;
        }
      }
    }

    .bind-btn {
      width: 100%;
      height: 96rpx;
      background-color: #0087FF;
      border-radius: 48rpx;
      color: #fff;
      font-size: 32rpx;
      margin-top: 40rpx;
      border: none;

      &::after {
        border: none;
      }
    }
  }
}
</style>
