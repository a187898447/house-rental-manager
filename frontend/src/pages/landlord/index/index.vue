<template>
  <view class="property-list-page">
    <!-- 顶部标题区域 -->
    <view class="header">
      <view class="title">我的房源</view>
      <view class="stats">
        <view class="stat-item">
          <text class="num">{{ vacantCount }}</text>
          <text class="label">待出租</text>
        </view>
        <view class="stat-item">
          <text class="num">{{ rentedCount }}</text>
          <text class="label">已出租</text>
        </view>
      </view>
    </view>
    
    <!-- 顶部 Tabs -->
    <view class="tabs">
      <view 
        v-for="(tab, index) in tabs" 
        :key="index"
        class="tab-item" 
        :class="{ active: currentTab === index }"
        @click="onTabChange(index)"
      >
        {{ tab.name }}
      </view>
    </view>
    
    <!-- 房源列表 -->
    <view class="property-list">
      <view v-if="loading && toRaw(properties).length === 0" class="loading-wrap">
        <u-loading-icon mode="circle"></u-loading-icon>
      </view>
      
      <view v-else-if="filteredList.length === 0" class="empty-wrap">
        <u-empty text="暂无房源" mode="list"></u-empty>
      </view>
      
      <view v-else class="property-items">
        <PropertyCard 
          v-for="item in filteredList" 
          :key="item.id" 
          :data="item"
          @click="onPropertyClick(item)"
        />
      </view>
      
      <view v-if="loading && toRaw(properties).length > 0" class="loading-more">
        <u-loading-icon mode="circle"></u-loading-icon>
      </view>
    </view>
    
    <!-- 新增按钮 -->
    <view class="add-btn" @click="onAddProperty">
      <u-icon name="plus" color="#fff" size="24"></u-icon>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, toRaw } from 'vue'
import { storeToRefs } from 'pinia'
import type { Property } from '@/types'
import { usePropertyStore } from '@/stores/property'
import PropertyCard from '@/components/PropertyCard.vue'

const propertyStore = usePropertyStore()
// 使用 storeToRefs 保持响应性
const { properties } = storeToRefs(propertyStore)

const currentTab = ref(0)
const loading = ref(false)

const tabs = [
  { name: '全部', value: '' },
  { name: '未出租', value: 0 },
  { name: '已出租', value: 1 }
]

const statusMap = [null, 0, 1]

// 统计
const vacantCount = computed(() => {
  const list = toRaw(properties.value) || []
  return list.filter((p: any) => p.status === 0).length
})
const rentedCount = computed(() => {
  const list = toRaw(properties.value) || []
  return list.filter((p: any) => p.status === 1).length
})

const filteredList = computed(() => {
  const status = statusMap[currentTab.value]
  const list = toRaw(properties.value) || []
  // 全部 tab 时 status 为 null，返回全部
  if (currentTab.value === 0) return list
  return list.filter((p: any) => p.status === status)
})

const onTabChange = (index: number) => {
  currentTab.value = index
}

const onLoadMore = () => {
  // TODO: 加载更多
}

const onPropertyClick = (item: Property) => {
  uni.navigateTo({
    url: `/pages/landlord/property/edit/index?id=${item.id}`
  })
}

const onAddProperty = () => {
  uni.navigateTo({
    url: '/pages/landlord/property/add/index'
  })
}

// 页面加载
onMounted(() => {
  loading.value = true
  propertyStore.fetchProperties().finally(() => {
    loading.value = false
  })
})
</script>

<style lang="scss" scoped>
.property-list-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #F5F7FA;
}

/* 顶部搜索栏区域 */
.header {
  background: linear-gradient(135deg, #667EEA 0%, #764BA2 100%);
  padding: 30rpx;
  
  .title {
    font-size: 40rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 20rpx;
  }
  
  .search-box {
    background-color: rgba(255, 255, 255, 0.2);
    border-radius: 40rpx;
    padding: 16rpx 30rpx;
    display: flex;
    align-items: center;
    
    .placeholder {
      color: rgba(255, 255, 255, 0.8);
      font-size: 28rpx;
    }
  }
}

.tabs {
  display: flex;
  background-color: #fff;
  padding: 0 20rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
  
  .tab-item {
    flex: 1;
    text-align: center;
    font-size: 28rpx;
    color: #999;
    padding: 28rpx 0;
    position: relative;
    transition: all 0.3s ease;
    
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
      width: 60rpx;
      height: 6rpx;
      background: linear-gradient(90deg, #667EEA, #764BA2);
      border-radius: 3rpx;
    }
  }
}

.property-list {
  flex: 1;
  padding: 24rpx;
}

.scroll-view {
  height: 100%;
}

.property-items {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.loading-wrap,
.empty-wrap,
.loading-more {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 100rpx 0;
}

.add-btn {
  position: fixed;
  right: 40rpx;
  bottom: 60rpx;
  width: 120rpx;
  height: 120rpx;
  background: linear-gradient(135deg, #667EEA 0%, #764BA2 100%);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0 8rpx 30rpx rgba(102, 126, 234, 0.5);
  
  &:active {
    transform: scale(0.95);
  }
}
</style>
