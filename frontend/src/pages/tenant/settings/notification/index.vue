<template>
  <view class="notification-page">
    <view class="switch-list">
      <view class="switch-item">
        <text class="label">账单通知</text>
        <switch :checked="notifyBill" @change="notifyBill = !notifyBill" color="#007AFF" />
      </view>
      <view class="switch-item">
        <text class="label">合同通知</text>
        <switch :checked="notifyContract" @change="notifyContract = !notifyContract" color="#007AFF" />
      </view>
      <view class="switch-item">
        <text class="label">报修进度通知</text>
        <switch :checked="notifyRepair" @change="notifyRepair = !notifyRepair" color="#007AFF" />
      </view>
      <view class="switch-item">
        <text class="label">系统通知</text>
        <switch :checked="notifySystem" @change="notifySystem = !notifySystem" color="#007AFF" />
      </view>
    </view>
    <button class="save-btn" @click="saveNotification">保存设置</button>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const notifyBill = ref(true)
const notifyContract = ref(true)
const notifyRepair = ref(true)
const notifySystem = ref(true)

const saveNotification = () => {
  uni.showToast({ title: '保存成功', icon: 'success' })
  setTimeout(() => uni.navigateBack(), 1500)
}
</script>

<style scoped>
.notification-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 24rpx;
}

.switch-list {
  background: #fff;
  border-radius: 16rpx;
}

.switch-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 32rpx 24rpx;
  border-bottom: 1rpx solid #f5f5f5;
}

.label {
  font-size: 28rpx;
  color: #333;
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