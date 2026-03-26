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
import { ref, onMounted } from 'vue'

const userInfo = ref<any>({})

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
  background-color: #f5f5f5;
  padding: 30rpx;
}

.user-card {
  display: flex;
  align-items: center;
  background-color: #fff;
  border-radius: 16rpx;
  padding: 40rpx;
  margin-bottom: 30rpx;
  
  .avatar {
    width: 120rpx;
    height: 120rpx;
    background-color: #e6f7ff;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 30rpx;
  }
  
  .info {
    .name {
      font-size: 36rpx;
      font-weight: bold;
      color: #333;
      display: block;
      margin-bottom: 10rpx;
    }
    
    .phone {
      font-size: 28rpx;
      color: #666;
    }
  }
}

.menu-list {
  background-color: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1px solid #f5f5f5;
  
  &:last-child {
    border-bottom: none;
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
  background-color: #fff;
  border-radius: 44rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #FF3B30;
  font-size: 32rpx;
}
</style>
