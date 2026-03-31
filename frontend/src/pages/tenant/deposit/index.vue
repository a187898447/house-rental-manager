<template>
  <view class="deposit-page">
    <view class="deposit-card">
      <view class="title">押金信息</view>
      <view class="amount">¥{{ depositAmount }}</view>
      <view class="status" :class="statusClass">{{ statusText }}</view>
    </view>
    
    <view class="detail-list">
      <view class="detail-item">
        <text class="label">缴纳日期</text>
        <text class="value">{{ payDate }}</text>
      </view>
      <view class="detail-item">
        <text class="label">押金金额</text>
        <text class="value">¥{{ depositAmount }}</text>
      </view>
      <view class="detail-item">
        <text class="label">预计退还</text>
        <text class="value">{{ refundDate }}</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const depositAmount = ref(2000)
const payDate = ref('2026-01-15')
const refundDate = ref('租约结束后退还')
const status = ref('paid')

const statusClass = computed(() => status.value)
const statusText = computed(() => '已缴纳')
</script>

<style scoped>
.deposit-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 24rpx;
}

.deposit-card {
  background: #007AFF;
  border-radius: 16rpx;
  padding: 48rpx 24rpx;
  text-align: center;
  margin-bottom: 24rpx;
}

.title {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
}

.amount {
  font-size: 64rpx;
  font-weight: 600;
  color: #fff;
  margin: 24rpx 0;
}

.status {
  display: inline-block;
  background: rgba(255, 255, 255, 0.2);
  padding: 8rpx 24rpx;
  border-radius: 24rpx;
  font-size: 24rpx;
  color: #fff;
}

.detail-list {
  background: #fff;
  border-radius: 16rpx;
  padding: 0 24rpx;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  padding: 32rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.label {
  font-size: 28rpx;
  color: #666;
}

.value {
  font-size: 28rpx;
  color: #333;
}
</style>