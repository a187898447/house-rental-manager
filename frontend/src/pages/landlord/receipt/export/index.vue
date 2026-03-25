<template>
  <view class="receipt-export-page">
    <!-- 筛选 -->
    <view class="filter-bar">
      <view class="filter-item">
        <text class="label">类型</text>
        <picker :range="typeOptions" @change="onTypeChange">
          <text>{{ typeOptions[currentType] || '全部' }}</text>
        </picker>
      </view>
      <view class="filter-item">
        <text class="label">月份</text>
        <picker mode="date" fields="month" @change="onMonthChange">
          <text>{{ currentMonth || '全部' }}</text>
        </picker>
      </view>
    </view>

    <!-- 收据列表 -->
    <scroll-view scroll-y class="receipt-list">
      <view v-if="loading && receipts.length === 0" class="loading-wrap">
        <up-loading-icon mode="circle"></up-loading-icon>
      </view>
      
      <view v-else-if="receipts.length === 0" class="empty-wrap">
        <up-empty text="暂无收据" mode="list"></up-empty>
      </view>
      
      <view v-else class="receipt-items">
        <view v-for="item in receipts" :key="item.id" class="receipt-card">
          <view class="receipt-header">
            <text class="type">{{ typeText(item.type) }}</text>
            <text class="amount">¥{{ item.amount }}</text>
          </view>
          <view class="receipt-body">
            <view class="row">
              <text class="label">房源</text>
              <text class="value">{{ item.propertyName }}</text>
            </view>
            <view class="row">
              <text class="label">租客</text>
              <text class="value">{{ item.tenantName }}</text>
            </view>
            <view class="row">
              <text class="label">月份</text>
              <text class="value">{{ item.billMonth }}</text>
            </view>
            <view class="row">
              <text class="label">开具时间</text>
              <text class="value">{{ item.createdAt }}</text>
            </view>
          </view>
          <view class="receipt-footer">
            <view class="export-btn" @click="onExport(item)">导出 PDF</view>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getReceipts } from '@/services/receipt'

const typeOptions = ['全部', '租金', '押金', '水电', '其他']
const currentType = ref(0)
const currentMonth = ref('')
const receipts = ref<any[]>([])
const loading = ref(false)

const typeText = (type: string) => {
  const map = { rent: '租金', deposit: '押金', utility: '水电', other: '其他' }
  return map[type] || '其他'
}

const fetchReceipts = async () => {
  loading.value = true
  try {
    const params: any = {}
    if (currentType.value > 0) {
      const types = ['rent', 'deposit', 'utility', 'other']
      params.type = types[currentType.value - 1]
    }
    if (currentMonth.value) {
      params.month = currentMonth.value
    }
    const res = await getReceipts(params)
    receipts.value = res || []
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  } finally {
    loading.value = false
  }
}

const onTypeChange = (e: any) => {
  currentType.value = e.detail.value
  fetchReceipts()
}

const onMonthChange = (e: any) => {
  currentMonth.value = e.detail.value
  fetchReceipts()
}

const onExport = (item: any) => {
  uni.showToast({ title: '导出功能开发中', icon: 'none' })
}

onMounted(() => {
  fetchReceipts()
})
</script>

<style scoped lang="scss">
.receipt-export-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.filter-bar {
  display: flex;
  background-color: #fff;
  padding: 20rpx 30rpx;
  gap: 20rpx;
  
  .filter-item {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 72rpx;
    padding: 0 20rpx;
    background-color: #f5f5f5;
    border-radius: 8rpx;
    
    .label {
      font-size: 26rpx;
      color: #666;
    }
    
    text {
      font-size: 26rpx;
      color: #333;
    }
  }
}

.receipt-list {
  height: calc(100vh - 132rpx);
  padding: 20rpx;
}

.loading-wrap, .empty-wrap {
  padding: 100rpx 0;
}

.receipt-card {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.receipt-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
  
  .type {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
  }
  
  .amount {
    font-size: 36rpx;
    font-weight: bold;
    color: #0087FF;
  }
}

.receipt-body {
  .row {
    display: flex;
    justify-content: space-between;
    padding: 12rpx 0;
    
    .label {
      font-size: 26rpx;
      color: #666;
    }
    
    .value {
      font-size: 26rpx;
      color: #333;
    }
  }
}

.receipt-footer {
  margin-top: 20rpx;
  padding-top: 20rpx;
  border-top: 1px solid #f5f5f5;
  
  .export-btn {
    width: 100%;
    height: 72rpx;
    background-color: #0087FF;
    border-radius: 36rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    font-size: 28rpx;
  }
}
</style>