<template>
  <view class="repair-list-page">
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
    <scroll-view scroll-y class="repair-list">
      <view v-if="loading && repairs.length === 0" class="loading-wrap">
        <up-loading-icon mode="circle"></up-loading-icon>
      </view>
      
      <view v-else-if="repairs.length === 0" class="empty-wrap">
        <up-empty text="暂无报修" mode="list"></up-empty>
      </view>
      
      <view v-else class="repair-items">
        <view v-for="item in repairs" :key="item.id" class="repair-card" @click="onRepairClick(item)">
          <view class="repair-header">
            <text class="title">{{ item.title }}</text>
            <text class="status" :class="statusClass(item.status)">
              {{ statusText(item.status) }}
            </text>
          </view>
          <view class="repair-body">
            <text class="property">{{ item.propertyName }}</text>
            <text class="desc">{{ item.description }}</text>
            <text class="date">{{ item.createdAt }}</text>
          </view>
          <view class="repair-footer" v-if="item.status === 0">
            <view class="handle-btn" @click.stop="onHandle(item)">开始处理</view>
          </view>
          <view class="repair-footer" v-else-if="item.status === 1">
            <view class="complete-btn" @click.stop="onComplete(item)">完成处理</view>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getOwnerRepairs, handleRepair, completeRepair } from '@/services/repair'

const tabs = [
  { name: '全部', value: '' },
  { name: '待处理', value: '0' },
  { name: '处理中', value: '1' },
  { name: '已完成', value: '2' }
]

const currentTab = ref(0)
const repairs = ref<any[]>([])
const loading = ref(false)

const statusText = (status: number) => {
  const map = { 0: '待处理', 1: '处理中', 2: '已完成', 3: '已取消' }
  return map[status] || '未知'
}

const statusClass = (status: number) => {
  const map = { 0: 'pending', 1: 'processing', 2: 'completed', 3: 'cancelled' }
  return map[status] || ''
}

const fetchRepairs = async () => {
  loading.value = true
  try {
    const status = tabs[currentTab].value
    const res = await getOwnerRepairs(status ? { status } : {})
    repairs.value = res || []
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  } finally {
    loading.value = false
  }
}

const onTabChange = (index: number) => {
  currentTab.value = index
  fetchRepairs()
}

const onRepairClick = (item: any) => {
  uni.navigateTo({ url: `/pages/landlord/repair/detail/index?id=${item.id}` })
}

const onHandle = (item: any) => {
  handleRepair(item.id).then(() => {
    uni.showToast({ title: '已开始处理', icon: 'success' })
    fetchRepairs()
  })
}

const onComplete = (item: any) => {
  uni.showModal({
    title: '完成处理',
    content: '确认已完成此报修吗？',
    success: (res) => {
      if (res.confirm) {
        completeRepair(item.id).then(() => {
          uni.showToast({ title: '已完成', icon: 'success' })
          fetchRepairs()
        })
      }
    }
  })
}

onMounted(() => {
  fetchRepairs()
})
</script>

<style scoped lang="scss">
.repair-list-page {
  min-height: 100vh;
  background-color: #F5F7FA;
}

.tabs {
  display: flex;
  background-color: #fff;
  padding: 0 20rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
  
  .tab {
    flex: 1;
    text-align: center;
    font-size: 28rpx;
    color: #999;
    padding: 28rpx 0;
    position: relative;
    
    &.active {
      color: #667EEA;
      font-weight: 600;
    }
    
    &.active::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%);
      width: 40rpx;
      height: 6rpx;
      background: linear-gradient(90deg, #667EEA, #764BA2);
      border-radius: 3rpx;
    }
  }
}

.repair-list {
  height: calc(100vh - 100rpx);
  padding: 24rpx;
}

.loading-wrap, .empty-wrap {
  padding: 100rpx 0;
}

.repair-card {
  background-color: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.repair-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16rpx;
  
  .title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
  }
  
  .status {
    font-size: 24rpx;
    padding: 4rpx 16rpx;
    border-radius: 20rpx;
    
    &.pending { background-color: #fff7e6; color: #FF9500; }
    &.processing { background-color: #e6f7ff; color: #0087FF; }
    &.completed { background-color: #e6fff2; color: #52C41A; }
  }
}

.repair-body {
  .property {
    font-size: 26rpx;
    color: #666;
    display: block;
    margin-bottom: 8rpx;
  }
  
  .desc {
    font-size: 28rpx;
    color: #333;
    display: block;
    margin-bottom: 8rpx;
  }
  
  .date {
    font-size: 24rpx;
    color: #999;
  }
}

.repair-footer {
  margin-top: 20rpx;
  padding-top: 20rpx;
  border-top: 1px solid #f5f5f5;
  
  .handle-btn, .complete-btn {
    width: 100%;
    height: 72rpx;
    border-radius: 36rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 28rpx;
  }
  
  .handle-btn {
    border: 1px solid #0087FF;
    color: #0087FF;
  }
  
  .complete-btn {
    background-color: #52C41A;
    color: #fff;
  }
}
</style>