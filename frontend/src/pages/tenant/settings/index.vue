<template>
  <view class="tenant-settings">
    <view class="menu-list">
      <view class="menu-item" @click="goToProfile">
        <text class="menu-label">个人资料</text>
        <text class="arrow">></text>
      </view>
      <view class="menu-item" @click="goToNotification">
        <text class="menu-label">通知设置</text>
        <text class="arrow">></text>
      </view>
      <view class="menu-item" @click="goToPassword">
        <text class="menu-label">修改密码</text>
        <text class="arrow">></text>
      </view>
      <view class="menu-item" @click="logout">
        <text class="menu-label logout">退出登录</text>
        <text class="arrow">></text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const goToProfile = () => {
  uni.navigateTo({ url: '/pages/tenant/settings/profile/index' })
}

const goToNotification = () => {
  uni.navigateTo({ url: '/pages/tenant/settings/notification/index' })
}

const goToPassword = () => {
  uni.showToast({ title: '功能开发中', icon: 'none' })
}

const logout = () => {
  uni.showModal({
    title: '提示',
    content: '确定退出登录？',
    success: (res) => {
      if (res.confirm) {
        userStore.logout()
        uni.reLaunch({ url: '/pages/login/index' })
      }
    }
  })
}
</script>

<style scoped>
.tenant-settings {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 24rpx;
}

.menu-list {
  background: #fff;
  border-radius: 16rpx;
}

.menu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 32rpx 24rpx;
  border-bottom: 1rpx solid #f5f5f5;
}

.menu-label {
  font-size: 28rpx;
  color: #333;
}

.menu-label.logout {
  color: #ff4d4f;
}

.arrow {
  font-size: 28rpx;
  color: #999;
}
</style>