<template>
  <view class="deposit-list-page">
    <!-- tabs -->
    <view class="tabs">
      <view 
        v-for="(tab, index) in tabs" 
        :key="index"
        class="tab" 
        :class="{ active: currentTab === index }"
        @click="onTabChange(index)"
      >
        {{ tab.name }}
      </view>
    </view>

    <!-- 列表 -->
    <scroll-view scroll-y class="deposit-list">
      <view v-if="loading && deposits.length === 0" class="loading-wrap">
        <up-loading-icon mode="circle"></up-loading-icon>
      </view>
      
      <view v-else-if="deposits.length === 0" class="empty-wrap">
        <up-empty text="暂无押金记录" mode="list"></up-empty>
      </view>
      
      <view v-else class="deposit-items">
        <view v-for="item in deposits" :key="item.id" class="deposit-card">
          <view class="deposit-header">
            <text class="property">{{ item.propertyName }}</text>
            <text class="status" :class="statusClass(item.status)">
              {{ statusText(item.status) }}
            </text>
          </view>
          <view class="deposit-body">
            <view class="row">
              <text class="label">租客</text>
              <text class="value">{{ item.tenantName }}</text>
            </view>
            <view class="row">
              <text class="label">押金金额</text>
              <text class="value amount">¥{{ item.amount }}</text>
            </view>
            <view class="row" v-if="item.status === 1">
              <text class="label">实退金额</text>
              <text class="value">¥{{ item.refundAmount }}</text>
            </view>
          </view>
          <view class="deposit-footer" v-if="item.status === 0">
            <view class="refund-btn" @click="onRefund(item)">退还押金</view>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getDeposits, refundDeposit } from '@/services/deposit'

const tabs = [
  { name: '全部', value: '' },
  { name: '待退还', value: '0' },
  { name: '已退还', value: '1' }
]

const currentTab = ref(0)
const deposits = ref<any[]>([])
const loading = ref(false)

const statusText = (status: number) => {
  const map = { 0: '待退还', 1: '已退还', 2: '部分退还' }
  return map[status] || '未知'
}

const statusClass = (status: number) => {
  const map = { 0: 'pending', 1: 'paid', 2: 'partial' }
  return map[status] || ''
}

const fetchDeposits = async () => {
  loading.value = true
  try {
    const status = tabs[currentTab].value
    const res = await getDeposits(status ? { status: Number(status) } : {})
    deposits.value = res || []
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  } finally {
    loading.value = false
  }
}

const onTabChange = (index: number) => {
  currentTab.value = index
  fetchDeposits()
}

const onRefund = (item: any) => {
  uni.navigateTo({ url: `/pages/landlord/deposit/refund/index?id=${item.id}` })
}

onMounted(() => {
  fetchDeposits()
})
</script>

<style scoped lang="scss">
.deposit-list-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.tabs {
  display: flex;
  background-color: #fff;
  padding: 20rpx 0;
  
  .tab {
    flex: 1;
    text-align: center;
    font-size: 28rpx;
    color: #666;
    padding: 16rpx 0;
    border-bottom: 4rpx solid transparent;
    
    &.active {
      color: #0087FF;
      border-bottom-color: #0087FF;
    }
  }
}

.deposit-list {
  height: calc(100vh - 100rpx);
  padding: 20rpx;
}

.loading-wrap, .empty-wrap {
  padding: 100rpx 0;
}

.deposit-card {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.deposit-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
  
  .property {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
  }
  
  .status {
    font-size: 24rpx;
    padding: 4rpx 16rpx;
    border-radius: 20rpx;
    
    &.pending { background-color: #fff7e6; color: #FF9500; }
    &.paid { background-color: #e6fff2; color: #52C41A; }
    &.partial { background-color: #e6f7ff; color: #0087FF; }
  }
}

.deposit-body {
  .row {
    display: flex;
    justify-content: space-between;
    padding: 12rpx 0;
    
    .label {
      font-size: 28rpx;
      color: #666;
    }
    
    .value {
      font-size: 28rpx;
      color: #333;
      
      &.amount {
        font-weight: bold;
        color: #0087FF;
      }
    }
  }
}

.deposit-footer {
  margin-top: 20rpx;
  padding-top: 20rpx;
  border-top: 1px solid #f5f5f5;
  
  .refund-btn {
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