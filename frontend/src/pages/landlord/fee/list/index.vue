<template>
  <view class="fee-list-page">
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
    <scroll-view scroll-y class="fee-list">
      <view v-if="loading && fees.length === 0" class="loading-wrap">
        <up-loading-icon mode="circle"></up-loading-icon>
      </view>
      
      <view v-else-if="fees.length === 0" class="empty-wrap">
        <up-empty text="暂无费用记录" mode="list"></up-empty>
      </view>
      
      <view v-else class="fee-items">
        <view v-for="item in fees" :key="item.id" class="fee-card">
          <view class="fee-header">
            <text class="type">{{ typeText(item.feeType) }}</text>
            <text class="status" :class="item.status === 1 ? 'paid' : 'pending'">
              {{ item.status === 1 ? '已支付' : '待支付' }}
            </text>
          </view>
          <view class="fee-body">
            <view class="row">
              <text class="label">房源</text>
              <text class="value">{{ item.propertyName }}</text>
            </view>
            <view class="row">
              <text class="label">租客</text>
              <text class="value">{{ item.tenantName }}</text>
            </view>
            <view class="row">
              <text class="label">金额</text>
              <text class="value amount">¥{{ item.amount }}</text>
            </view>
            <view class="row">
              <text class="label">月份</text>
              <text class="value">{{ item.billMonth }}</text>
            </view>
          </view>
        </view>
      </view>
    </scroll-view>

    <!-- 新增按钮 -->
    <view class="add-btn" @click="onAddFee">
      <up-icon name="plus" color="#fff" size="24"></up-icon>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getOwnerOtherFees, createOtherFee } from '@/services/otherfee'

const tabs = [
  { name: '全部', value: '' },
  { name: '待支付', value: '0' },
  { name: '已支付', value: '1' }
]

const currentTab = ref(0)
const fees = ref<any[]>([])
const loading = ref(false)

const typeText = (type: string) => {
  const map = { management: '管理费', network: '网络费', garbage: '垃圾费', other: '其他' }
  return map[type] || '其他'
}

const fetchFees = async () => {
  loading.value = true
  try {
    const status = tabs[currentTab].value
    const res = await getOwnerOtherFees(status ? { status: Number(status) } : {})
    fees.value = res?.list || []
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  } finally {
    loading.value = false
  }
}

const onTabChange = (index: number) => {
  currentTab.value = index
  fetchFees()
}

const onAddFee = () => {
  uni.navigateTo({ url: '/pages/landlord/fee/add/index' })
}

onMounted(() => {
  fetchFees()
})
</script>

<style scoped lang="scss">
.fee-list-page {
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

.fee-list {
  height: calc(100vh - 100rpx - 120rpx);
  padding: 20rpx;
}

.loading-wrap, .empty-wrap {
  padding: 100rpx 0;
}

.fee-card {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.fee-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
  
  .type {
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
  }
}

.fee-body {
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

.add-btn {
  position: fixed;
  right: 40rpx;
  bottom: 40rpx;
  width: 100rpx;
  height: 100rpx;
  background-color: #0087FF;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 20rpx rgba(0, 135, 255, 0.4);
}
</style>