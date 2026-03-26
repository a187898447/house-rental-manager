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
import { ref, computed, onMounted } from 'vue'
import { toRaw } from 'vue'
import { usePropertyStore } from '@/stores/property'
import PropertyCard from '@/components/PropertyCard.vue'

const propertyStore = usePropertyStore()

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

// 使用 toRaw 处理 Proxy
const properties = computed(() => toRaw(propertyStore.properties))

const filteredList = computed(() => {
  const statusMap = [null, 0, 1]
  const list = properties.value || []
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
</script>

<style scoped lang="scss">
.property-list-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx 30rpx 20rpx;
  background-color: #fff;

  .title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
  }

  .add-btn {
    padding: 12rpx 24rpx;
    background-color: #0087FF;
    border-radius: 8rpx;

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
  border-bottom: 1rpx solid #eee;

  .tab-item {
    flex: 1;
    text-align: center;
    padding: 24rpx 0;
    font-size: 28rpx;
    color: #666;
    position: relative;

    &.active {
      color: #0087FF;
      font-weight: bold;

      &::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%);
        width: 60rpx;
        height: 4rpx;
        background-color: #0087FF;
        border-radius: 2rpx;
      }
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
