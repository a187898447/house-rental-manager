<template>
  <view class="notification-settings-page">
    <view class="section">
      <view class="section-title">收租提醒</view>
      <view class="switch-item">
        <text class="label">提前提醒天数</text>
        <up-number-box v-model="notifyDays" :min="1" :max="30"></up-number-box>
      </view>
    </view>

    <view class="section">
      <view class="section-title">通知方式</view>
      <view class="switch-item">
        <text class="label">短信通知</text>
        <switch :checked="smsEnabled" @change="smsEnabled = !smsEnabled" color="#0087FF"/>
      </view>
      <view class="switch-item">
        <text class="label">微信通知</text>
        <switch :checked="wechatEnabled" @change="wechatEnabled = !wechatEnabled" color="#0087FF"/>
      </view>
    </view>

    <view class="save-btn" @click="onSave">保存设置</view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const notifyDays = ref(3)
const smsEnabled = ref(true)
const wechatEnabled = ref(true)

const onSave = () => {
  uni.showToast({ title: '保存成功', icon: 'success' })
  setTimeout(() => uni.navigateBack(), 1500)
}
</script>

<style scoped lang="scss">
.notification-settings-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 30rpx;
}

.section {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 24rpx;
}

.switch-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1px solid #f5f5f5;
  
  &:last-child {
    border-bottom: none;
  }
  
  .label {
    font-size: 28rpx;
    color: #333;
  }
}

.save-btn {
  height: 88rpx;
  background-color: #0087FF;
  border-radius: 44rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 32rpx;
}
</style>
