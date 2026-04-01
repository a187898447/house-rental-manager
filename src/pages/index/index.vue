<template>
  <view class="container">
    <view class="welcome">
      <text class="title">欢迎使用租房管家</text>
      <text class="subtitle">请选择您的身份</text>
    </view>

    <view class="role-cards">
      <view class="role-card" @click="goToLandlord">
        <view class="role-icon">🏠</view>
        <text class="role-name">房东端</text>
        <text class="role-desc">房源管理、租客管理、租金收取</text>
      </view>

      <view class="role-card" @click="goToTenant">
        <view class="role-icon">👤</view>
        <text class="role-name">租客端</text>
        <text class="role-desc">账单查看、在线缴费、报修申请</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

/**
 * 跳转到房东端
 */
function goToLandlord() {
  if (userStore.userInfo) {
    if (userStore.userInfo.role === 'landlord') {
      uni.switchTab({ url: '/pages/landlord/index/index' })
    } else {
      uni.showToast({ title: '请使用房东账号登录', icon: 'none' })
    }
  } else {
    uni.navigateTo({ url: '/pages/login/index' })
  }
}

/**
 * 跳转到租客端
 */
function goToTenant() {
  if (userStore.userInfo) {
    if (userStore.userInfo.role === 'tenant') {
      uni.switchTab({ url: '/pages/tenant/index/index' })
    } else {
      uni.showToast({ title: '请使用租客账号登录', icon: 'none' })
    }
  } else {
    uni.navigateTo({ url: '/pages/login/index' })
  }
}
</script>

<style lang="scss" scoped>
.container {
  padding: 40rpx;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.welcome {
  text-align: center;
  margin-bottom: 80rpx;
  margin-top: 100rpx;

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

.role-cards {
  .role-card {
    background: #fff;
    border-radius: 24rpx;
    padding: 48rpx 32rpx;
    margin-bottom: 32rpx;
    text-align: center;

    .role-icon {
      font-size: 80rpx;
      margin-bottom: 24rpx;
    }

    .role-name {
      display: block;
      font-size: 36rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 12rpx;
    }

    .role-desc {
      display: block;
      font-size: 24rpx;
      color: #999;
    }
  }
}
</style>
