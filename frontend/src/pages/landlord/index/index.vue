<template>
  <view class="property-list-page">
    <!-- 顶部 Tabs -->
    <u-tabs 
      :list="tabs" 
      :current="currentTab" 
      @change="onTabChange"
      :scrollable="false"
      lineColor="#0087FF"
      activeStyle="{ color: '#0087FF' }"
    />
    
    <!-- 房源列表 -->
    <scroll-view 
      scroll-y 
      class="property-list"
      @scrolltolower="onLoadMore"
      :lower-threshold="100"
    >
      <view v-if="loading && properties.length === 0" class="loading-wrap">
        <u-loading mode="circle"></u-loading>
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
      
      <view v-if="loading && properties.length > 0" class="loading-more">
        <u-loading mode="circle"></u-loading>
      </view>
    </scroll-view>
    
    <!-- 新增按钮 -->
    <view class="add-btn" @click="onAddProperty">
      <u-icon name="plus" color="#fff" size="24"></u-icon>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import type { Property } from '@/types'
import { usePropertyStore } from '@/stores/property'
import PropertyCard from '@/components/PropertyCard.vue'

const propertyStore = usePropertyStore()
const currentTab = ref(0)
const loading = ref(false)

const tabs = [
  { name: '未出租', value: 'vacant' },
  { name: '已出租未缴费', value: 'rented_unpaid' },
  { name: '已出租已缴费', value: 'rented_paid' }
]

const statusMap = ['vacant', 'rented_unpaid', 'rented_paid']

const properties = computed(() => propertyStore.properties)

const filteredList = computed(() => {
  const status = statusMap[currentTab.value]
  return properties.value.filter(p => p.status === status)
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
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #F8F8F8;
}

.property-list {
  flex: 1;
  padding: 20rpx;
}

.property-items {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.loading-wrap,
.empty-wrap,
.loading-more {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 60rpx 0;
}

.add-btn {
  position: fixed;
  right: 30rpx;
  bottom: 30rpx;
  width: 100rpx;
  height: 100rpx;
  background-color: #0087FF;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0 4rpx 20rpx rgba(0, 135, 255, 0.4);
}
</style>
