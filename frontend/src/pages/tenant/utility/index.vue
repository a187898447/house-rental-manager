<template>
  <view class="utility-page">
    <view class="section">
      <view class="section-title">当前账单</view>
      <view class="bill-card">
        <view class="bill-item">
          <text class="label">水费</text>
          <text class="value">¥{{ waterAmount }}</text>
        </view>
        <view class="bill-item">
          <text class="label">电费</text>
          <text class="value">¥{{ electricityAmount }}</text>
        </view>
        <view class="bill-item total">
          <text class="label">合计</text>
          <text class="value">¥{{ totalAmount }}</text>
        </view>
      </view>
    </view>
    
    <view class="section">
      <view class="section-title">历史记录</view>
      <view class="history-list">
        <view class="history-item" v-for="item in historyList" :key="item.id">
          <view class="info">
            <text class="month">{{ item.month }}</text>
            <text class="amount">¥{{ item.amount }}</text>
          </view>
          <text class="status" :class="item.status">{{ item.statusText }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const waterAmount = ref(0)
const electricityAmount = ref(0)
const totalAmount = computed(() => waterAmount.value + electricityAmount.value)

const historyList = ref([
  { id: 1, month: '2026-02', amount: 156, status: 'paid', statusText: '已支付' },
  { id: 2, month: '2026-01', amount: 142, status: 'paid', statusText: '已支付' },
])
</script>

<style scoped>
.utility-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 24rpx;
}

.section {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 24rpx;
}

.section-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 16rpx;
}

.bill-card {
  background: #f9f9f9;
  border-radius: 12rpx;
  padding: 24rpx;
}

.bill-item {
  display: flex;
  justify-content: space-between;
  padding: 16rpx 0;
}

.bill-item.total {
  border-top: 1rpx solid #eee;
  margin-top: 16rpx;
  padding-top: 24rpx;
  font-weight: 600;
}

.label {
  font-size: 28rpx;
  color: #666;
}

.value {
  font-size: 32rpx;
  color: #333;
  font-weight: 500;
}

.history-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.info {
  display: flex;
  flex-direction: column;
}

.month {
  font-size: 28rpx;
  color: #333;
}

.amount {
  font-size: 26rpx;
  color: #666;
  margin-top: 8rpx;
}

.status {
  font-size: 24rpx;
  padding: 4rpx 12rpx;
  border-radius: 4rpx;
}

.status.paid {
  background: #f6ffed;
  color: #52c41a;
}

.status.unpaid {
  background: #fff2f0;
  color: #ff4d4f;
}
</style>