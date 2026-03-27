<template>
  <view class="property-list-page">
    <!-- 顶部标题 -->
    <view class="header">
      <text class="title">房源管理</text>
      <view class="add-btn" @click="goToAdd">
        <text>+ 新增房源</text>
      </view>
    </view>

    <!-- 标签页 -->
    <view class="tabs">
      <view 
        v-for="(tab, index) in tabs" 
        :key="tab.value"
        :class="['tab-item', { active: currentTab === index }]"
        @click="onTabChange(index)"
      >
        <text>{{ tab.name }}</text>
      </view>
    </view>

    <!-- 房源列表 -->
    <scroll-view 
      scroll-y 
      class="list-container"
      :refresher-enabled="true"
      :refresher-triggered="refreshing"
      @refresherrefresh="onRefresh"
      @scrolltolower="onLoadMore"
    >
      <view v-if="loading && properties.length === 0" class="loading">
        <text>加载中...</text>
      </view>

      <view v-else-if="filteredList.length === 0" class="empty">
        <text class="empty-text">暂无房源</text>
        <text class="empty-hint">点击上方"新增房源"添加</text>
      </view>

      <view v-else class="property-cards">
        <PropertyCard
          v-for="item in filteredList"
          :key="item.id"
          :data="item"
          @click="goToDetail(String(item.id))"
        />
      </view>

      <view v-if="loadingMore" class="load-more">
        <text>加载中...</text>
      </view>
    </scroll-view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, toRaw } from 'vue'
import { onShow } from '@dcloudio/vue-esc'
import { storeToRefs } from 'pinia'
import { usePropertyStore } from '@/stores/property'
import PropertyCard from '@/components/PropertyCard.vue'

const propertyStore = usePropertyStore()
// 使用 storeToRefs 保持响应性
const { properties } = storeToRefs(propertyStore)

const tabs = [
  { name: '全部', value: '' },
  { name: '未出租', value: 0 },
  { name: '已出租', value: 1 }
]

const currentTab = ref(0)
const refreshing = ref(false)
const loadingMore = ref(false)
const page = ref(1)
const pageSize = ref(10)

const loading = computed(() => propertyStore.loading)

const filteredList = computed(() => {
  const statusMap = [null, 0, 1]
  const list = toRaw(properties.value) || []
  // 全部 tab
  if (currentTab.value === 0) return list
  return list.filter((p: any) => p.status === statusMap[currentTab.value])
})

// 标签页切换
const onTabChange = (index: number) => {
  currentTab.value = index
  // 重新获取对应状态的数据
  fetchData()
}

// 获取数据
const fetchData = async () => {
  const status = tabs[currentTab.value].value
  await propertyStore.fetchProperties()
}

// 下拉刷新
const onRefresh = async () => {
  refreshing.value = true
  page.value = 1
  await fetchData()
  refreshing.value = false
}

// 上拉加载更多
const onLoadMore = async () => {
  if (loadingMore.value) return
  loadingMore.value = true
  page.value++
  // TODO: 调用分页接口
  loadingMore.value = false
}

// 跳转新增页
const goToAdd = () => {
  uni.navigateTo({
    url: '/pages/landlord/property/add'
  })
}

// 跳转详情页
const goToDetail = (id: string) => {
  uni.navigateTo({
    url: `/pages/landlord/property/detail?id=${id}`
  })
}

// 页面加载
onMounted(() => {
  fetchData()
})

// 每次页面显示时刷新数据
onShow(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
.property-list-page {
  min-height: 100vh;
  background-color: #F5F7FA;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx 30rpx 20rpx;
  background: linear-gradient(135deg, #667EEA 0%, #764BA2 100%);

  .title {
    font-size: 40rpx;
    font-weight: bold;
    color: #fff;
  }

  .add-btn {
    padding: 16rpx 30rpx;
    background-color: rgba(255, 255, 255, 0.2);
    border-radius: 30rpx;
    border: 1rpx solid rgba(255, 255, 255, 0.4);

    text {
      color: #fff;
      font-size: 28rpx;
    }
  }
}

.tabs {
  display: flex;
  background-color: #fff;
  padding: 0 30rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);

  .tab-item {
    flex: 1;
    text-align: center;
    padding: 28rpx 0;
    font-size: 28rpx;
    color: #999;
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
      width: 50rpx;
      height: 6rpx;
      background: linear-gradient(90deg, #667EEA, #764BA2);
      border-radius: 3rpx;
    }
  }
}

.list-container {
  height: calc(100vh - 200rpx);
  padding: 20rpx 30rpx;
}

.loading, .load-more {
  text-align: center;
  padding: 30rpx;
  color: #999;
  font-size: 28rpx;
}

.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 200rpx 0;

  .empty-text {
    font-size: 32rpx;
    color: #333;
    margin-bottom: 16rpx;
  }

  .empty-hint {
    font-size: 28rpx;
    color: #999;
  }
}

.property-cards {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}
</style>
