<template>
  <view class="rent-list-page">
    <!-- 筛选 tabs -->
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

    <!-- 账单列表 -->
    <scroll-view scroll-y class="bill-list" @scrolltolower="onLoadMore">
      <view v-if="loading && bills.length === 0" class="loading-wrap">
        <u-loading mode="circle"></u-loading>
      </view>
      
      <view v-else-if="bills.length === 0" class="empty-wrap">
        <u-empty text="暂无账单" mode="list"></u-empty>
      </view>
      
      <view v-else class="bill-items">
        <view 
          v-for="item in bills" 
          :key="item.id" 
          class="bill-card"
          @click="onBillClick(item)"
        >
          <view class="bill-header">
            <text class="month">{{ item.payMonth }}</text>
            <text class="status" :class="statusClass(item.status)">
              {{ statusText(item.status) }}
            </text>
          </view>
          <view class="bill-body">
            <view class="property-name">{{ item.propertyName }}</view>
            <view class="amount">¥{{ item.amount }}</view>
          </view>
          <view class="bill-footer">
            <text class="date">创建于 {{ item.createdAt }}</text>
            <view class="actions" v-if="item.status === 0">
              <text class="remind-btn" @click.stop="onRemind(item)">催租</text>
              <text class="pay-btn" @click.stop="onPay(item)">确认收款</text>
            </view>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { getRentBills } from '@/services/rent'

const tabs = [
  { name: '全部', value: '' },
  { name: '待支付', value: '0' },
  { name: '已支付', value: '1' },
  { name: '已逾期', value: '2' }
]

const currentTab = ref(0)
const bills = ref<any[]>([])
const loading = ref(false)

const statusText = (status: number) => {
  const map = { 0: '待支付', 1: '已支付', 2: '已逾期', 3: '已取消' }
  return map[status] || '未知'
}

const statusClass = (status: number) => {
  const map = { 0: 'pending', 1: 'paid', 2: 'overdue', 3: 'cancelled' }
  return map[status] || ''
}

const fetchBills = async () => {
  loading.value = true
  try {
    const status = tabs[currentTab].value
    const res = await getRentBills(status ? { status: Number(status) } : {})
    bills.value = res || []
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

const onLoadMore = () => {
  // TODO: 分页
}

const onBillClick = (item: any) => {
  uni.navigateTo({ url: `/pages/landlord/rent/detail/index?id=${item.id}` })
}

const onRemind = (item: any) => {
  uni.showToast({ title: '催租提醒已发送', icon: 'success' })
}

const onPay = (item: any) => {
  uni.showModal({
    title: '确认收款',
    content: `确认收到 ${item.amount} 元租金吗？`,
    success: (res) => {
      if (res.confirm) {
        uni.showToast({ title: '已确认收款', icon: 'success' })
        fetchBills()
      }
    }
  })
}

onMounted(() => {
  fetchBills()
})
</script>

<style scoped lang="scss">
.rent-list-page {
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

.bill-list {
  height: calc(100vh - 100rpx);
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
    
    &.pending {
      background-color: #fff7e6;
      color: #FF9500;
    }
    &.paid {
      background-color: #e6fff2;
      color: #52C41A;
    }
    &.overdue {
      background-color: #fff1f0;
      color: #FF4D4F;
    }
  }
}

.bill-body {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  
  .property-name {
    font-size: 28rpx;
    color: #666;
  }
  
  .amount {
    font-size: 40rpx;
    font-weight: bold;
    color: #333;
  }
}

.bill-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  .date {
    font-size: 24rpx;
    color: #999;
  }
  
  .actions {
    display: flex;
    gap: 16rpx;
    
    .remind-btn, .pay-btn {
      font-size: 24rpx;
      padding: 8rpx 20rpx;
      border-radius: 20rpx;
    }
    
    .remind-btn {
      border: 1px solid #0087FF;
      color: #0087FF;
    }
    
    .pay-btn {
      background-color: #0087FF;
      color: #fff;
    }
  }
}
</style>