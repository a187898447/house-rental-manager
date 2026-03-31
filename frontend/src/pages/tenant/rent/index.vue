<template>
  <view class="rent-page">
    <view class="current-bill">
      <view class="title">当前待缴租金</view>
      <view class="amount">¥{{ currentRent }}</view>
      <view class="due-date">到期日期: {{ dueDate }}</view>
      <button class="pay-btn" @click="goToPay">立即支付</button>
    </view>
    
    <view class="history-section">
      <view class="section-title">历史租金</view>
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
import { ref } from 'vue'

const currentRent = ref(2500)
const dueDate = ref('2026-04-01')

const historyList = ref([
  { id: 1, month: '2026-03', amount: 2500, status: 'paid', statusText: '已支付' },
  { id: 2, month: '2026-02', amount: 2500, status: 'paid', statusText: '已支付' },
  { id: 3, month: '2026-01', amount: 2500, status: 'paid', statusText: '已支付' },
])

const goToPay = () => {
  uni.navigateTo({ url: '/pages/tenant/bill/pay/index' })
}
</script>

<style scoped>
.rent-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 24rpx;
}

.current-bill {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
  font-size: 72rpx;
  font-weight: 600;
  color: #fff;
  margin: 24rpx 0;
}

.due-date {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.7);
  margin-bottom: 32rpx;
}

.pay-btn {
  width: 200rpx;
  height: 80rpx;
  background: #fff;
  color: #667eea;
  border-radius: 40rpx;
  font-size: 28rpx;
}

.history-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
}

.section-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 16rpx;
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
</style>