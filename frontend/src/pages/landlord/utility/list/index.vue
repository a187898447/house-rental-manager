<template>
  <view class="utility-list-page">
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
    <scroll-view scroll-y class="utility-list">
      <view v-if="loading && bills.length === 0" class="loading-wrap">
        <u-loading mode="circle"></u-loading>
      </view>
      
      <view v-else-if="bills.length === 0" class="empty-wrap">
        <u-empty text="暂无水电账单" mode="list"></u-empty>
      </view>
      
      <view v-else class="bill-items">
        <view v-for="item in bills" :key="item.id" class="bill-card">
          <view class="bill-header">
            <text class="month">{{ item.billMonth }}</text>
            <text class="status" :class="item.status === 1 ? 'paid' : 'pending'">
              {{ item.status === 1 ? '已支付' : '待支付' }}
            </text>
          </view>
          <view class="bill-body">
            <view class="row">
              <text class="label">水费</text>
              <text class="value">¥{{ item.waterAmount || 0 }}</text>
            </view>
            <view class="row">
              <text class="label">电费</text>
              <text class="value">¥{{ item.electricityAmount || 0 }}</text>
            </view>
            <view class="row total">
              <text class="label">合计</text>
              <text class="value">¥{{ (item.waterAmount || 0) + (item.electricityAmount || 0) }}</text>
            </view>
          </view>
        </view>
      </view>
    </scroll-view>

    <!-- 新增按钮 -->
    <view class="add-btn" @click="onAddBill">
      <u-icon name="plus" color="#fff" size="24"></u-icon>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getUtilityBills } from '@/services/utility'

const tabs = [
  { name: '全部', value: '' },
  { name: '待支付', value: '0' },
  { name: '已支付', value: '1' }
]

const currentTab = ref(0)
const bills = ref<any[]>([])
const loading = ref(false)

const fetchBills = async () => {
  loading.value = true
  try {
    const status = tabs[currentTab].value
    const res = await getUtilityBills(status ? { status: Number(status) } : {})
    bills.value = res?.records || res?.list || []
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  } finally {
    loading.value = false
  }
}

const onTabChange = (index: number) => {
  currentTab.value = index
  fetchBills()
}

const onAddBill = () => {
  uni.navigateTo({ url: '/pages/landlord/utility/add/index' })
}

onMounted(() => {
  fetchBills()
})
</script>

<style scoped lang="scss">
.utility-list-page {
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

.utility-list {
  height: calc(100vh - 100rpx - 120rpx);
  padding: 20rpx;
}

.loading-wrap, .empty-wrap {
  padding: 100rpx 0;
}

.bill-card {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.bill-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
  
  .month {
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

.bill-body {
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
    }
    
    &.total {
      border-top: 1px solid #f5f5f5;
      margin-top: 12rpx;
      padding-top: 16rpx;
      
      .value {
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
