<template>
  <view class="statistics-page">
    <!-- 汇总卡片 -->
    <view class="summary-cards">
      <view class="card">
        <text class="value">{{ stats.totalProperties }}</text>
        <text class="label">房源总数</text>
      </view>
      <view class="card">
        <text class="value">{{ stats.rentedCount }}</text>
        <text class="label">已出租</text>
      </view>
      <view class="card">
        <text class="value">¥{{ stats.monthlyRent }}</text>
        <text class="label">月租金</text>
      </view>
      <view class="card">
        <text class="value">¥{{ stats.pendingAmount }}</text>
        <text class="label">待收金额</text>
      </view>
    </view>

    <!-- 空置率图表 -->
    <view class="chart-section">
      <view class="section-title">房源状态分布</view>
      <view class="chart-box">
        <!-- 简化：显示文字统计 -->
        <view class="stat-row">
          <text class="label">空置中</text>
          <text class="value">{{ stats.vacantCount }} 套</text>
        </view>
        <view class="stat-row">
          <text class="label">租住中</text>
          <text class="value">{{ stats.rentedCount }} 套</text>
        </view>
        <view class="stat-row">
          <text class="label">待缴费</text>
          <text class="value">{{ stats.pendingCount }} 套</text>
        </view>
      </view>
    </view>

    <!-- 最近收入 -->
    <view class="recent-section">
      <view class="section-title">本月收入</view>
      <view class="income-list">
        <view class="income-item">
          <text class="label">租金收入</text>
          <text class="value">¥{{ stats.rentIncome }}</text>
        </view>
        <view class="income-item">
          <text class="label">押金收入</text>
          <text class="value">¥{{ stats.depositIncome }}</text>
        </view>
        <view class="income-item">
          <text class="label">其他收入</text>
          <text class="value">¥{{ stats.otherIncome }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const stats = ref({
  totalProperties: 0,
  rentedCount: 0,
  vacantCount: 0,
  pendingCount: 0,
  monthlyRent: 0,
  pendingAmount: 0,
  rentIncome: 0,
  depositIncome: 0,
  otherIncome: 0
})

// TODO: 调用后端 API 获取统计数据
</script>

<style scoped lang="scss">
.statistics-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 30rpx;
}

.summary-cards {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
  margin-bottom: 30rpx;
}

.card {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 40rpx 20rpx;
  text-align: center;
  
  .value {
    font-size: 44rpx;
    font-weight: bold;
    color: #0087FF;
    display: block;
    margin-bottom: 10rpx;
  }
  
  .label {
    font-size: 24rpx;
    color: #666;
  }
}

.chart-section, .recent-section {
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

.chart-box {
  .stat-row {
    display: flex;
    justify-content: space-between;
    padding: 16rpx 0;
    border-bottom: 1px solid #f5f5f5;
    
    &:last-child {
      border-bottom: none;
    }
    
    .label {
      font-size: 28rpx;
      color: #666;
    }
    
    .value {
      font-size: 28rpx;
      color: #333;
    }
  }
}

.income-list {
  .income-item {
    display: flex;
    justify-content: space-between;
    padding: 20rpx 0;
    border-bottom: 1px solid #f5f5f5;
    
    &:last-child {
      border-bottom: none;
    }
    
    .label {
      font-size: 28rpx;
      color: #666;
    }
    
    .value {
      font-size: 28rpx;
      color: #52C41A;
      font-weight: bold;
    }
  }
}
</style>
