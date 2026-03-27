<template>
  <view class="settings-page">
    <!-- 用户信息 -->
    <view class="user-card">
      <view class="avatar">
        <up-icon name="account-fill" size="60" color="#0087FF"></up-icon>
      </view>
      <view class="info">
        <text class="name">{{ userInfo.name || '房东' }}</text>
        <text class="phone">{{ userInfo.phone || '' }}</text>
      </view>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-list">
      <view class="menu-item" @click="onEditProfile">
        <up-icon name="account" size="40" color="#0087FF"></up-icon>
        <text class="label">个人信息</text>
        <up-icon name="arrow-right" size="32" color="#999"></up-icon>
      </view>
      
      <view class="menu-item" @click="onNotificationSettings">
        <up-icon name="bell" size="40" color="#0087FF"></up-icon>
        <text class="label">通知设置</text>
        <up-icon name="arrow-right" size="32" color="#999"></up-icon>
      </view>
      
      <view class="menu-item" @click="onStatistics">
        <up-icon name="chart" size="40" color="#0087FF"></up-icon>
        <text class="label">数据统计</text>
        <up-icon name="arrow-right" size="32" color="#999"></up-icon>
      </view>
    </view>

    <!-- 退出登录 -->
    <view class="logout-btn" @click="onLogout">
      退出登录
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted, onShow } from 'vue'

const userInfo = ref<any>({})

// 每次页面显示时刷新用户信息
onShow(() => {
  const info = uni.getStorageSync('userInfo')
  if (info) {
    userInfo.value = typeof info === 'string' ? JSON.parse(info) : info
  }
})

onMounted(() => {
  const info = uni.getStorageSync('userInfo')
  if (info) {
    userInfo.value = typeof info === 'string' ? JSON.parse(info) : info
  }
})

const onEditProfile = () => {
  uni.navigateTo({ url: '/pages/landlord/settings/profile/index' })
}

const onNotificationSettings = () => {
  uni.navigateTo({ url: '/pages/landlord/settings/notification/index' })
}

const onStatistics = () => {
  uni.navigateTo({ url: '/pages/landlord/statistics/index' })
}

const onLogout = () => {
  uni.showModal({
    title: '提示',
    content: '确定退出登录？',
    success: (res) => {
      if (res.confirm) {
        uni.removeStorageSync('token')
        uni.removeStorageSync('userInfo')
        uni.reLaunch({ url: '/pages/login/index' })
      }
    }
  })
}
</script>

<style scoped lang="scss">
.settings-page {
  min-height: 100vh;
  background-color: #F5F7FA;
  padding: 30rpx;
}

.user-card {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #667EEA 0%, #764BA2 100%);
  border-radius: 24rpx;
  padding: 50rpx 40rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 8rpx 30rpx rgba(102, 126, 234, 0.3);
  
  .avatar {
    width: 120rpx;
    height: 120rpx;
    background-color: rgba(255, 255, 255, 0.3);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 30rpx;
  }
  
  .info {
    .name {
      font-size: 40rpx;
      font-weight: bold;
      color: #fff;
      display: block;
      margin-bottom: 10rpx;
    }
    
    .phone {
      font-size: 28rpx;
      color: rgba(255, 255, 255, 0.8);
    }
  }
}

.menu-list {
  background-color: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 32rpx 30rpx;
  border-bottom: 1rpx solid #f5f5f5;
  background-color: #fff;
  
  &:first-child {
    border-top-left-radius: 16rpx;
    border-top-right-radius: 16rpx;
  }
  
  &:last-child {
    border-bottom: none;
    border-bottom-left-radius: 16rpx;
    border-bottom-right-radius: 16rpx;
  }
  
  .label {
    flex: 1;
    font-size: 30rpx;
    color: #333;
    margin-left: 20rpx;
  }
}

.logout-btn {
  margin-top: 60rpx;
  height: 88rpx;
  background: linear-gradient(135deg, #667EEA 0%, #764BA2 100%);
  border-radius: 44rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 20rpx rgba(102, 126, 234, 0.4);
  
  text {
    color: #fff;
    font-size: 32rpx;
    font-weight: 600;
  }
}
</style>
