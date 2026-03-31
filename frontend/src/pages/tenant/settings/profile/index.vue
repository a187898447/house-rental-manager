<template>
  <view class="profile-page">
    <view class="form">
      <view class="form-item">
        <text class="label">头像</text>
        <image class="avatar" :src="userInfo.avatar || '/static/default-avatar.png'" @click="chooseAvatar" />
      </view>
      <view class="form-item">
        <text class="label">昵称</text>
        <input class="input" v-model="userInfo.nickname" placeholder="请输入昵称" />
      </view>
      <view class="form-item">
        <text class="label">手机号</text>
        <text class="value">{{ userInfo.phone || '未绑定' }}</text>
      </view>
    </view>
    <button class="save-btn" @click="saveProfile">保存</button>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const userInfo = ref({ ...userStore.userInfo })

const chooseAvatar = () => {
  uni.showToast({ title: '功能开发中', icon: 'none' })
}

const saveProfile = () => {
  userStore.setUserInfo(userInfo.value)
  uni.showToast({ title: '保存成功', icon: 'success' })
  setTimeout(() => uni.navigateBack(), 1500)
}
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 24rpx;
}

.form {
  background: #fff;
  border-radius: 16rpx;
  padding: 0 24rpx;
}

.form-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 32rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.label {
  font-size: 28rpx;
  color: #333;
}

.input {
  flex: 1;
  text-align: right;
  font-size: 28rpx;
}

.value {
  font-size: 28rpx;
  color: #999;
}

.avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 40rpx;
  background: #eee;
}

.save-btn {
  margin-top: 48rpx;
  width: 100%;
  height: 96rpx;
  background: #007AFF;
  color: #fff;
  border-radius: 16rpx;
  font-size: 32rpx;
}
</style>