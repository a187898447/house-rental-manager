<template>
  <view class="tenant-list-page">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <input 
        v-model="keyword" 
        class="search-input" 
        placeholder="搜索租客姓名或手机号"
        @confirm="onSearch"
      />
    </view>

    <!-- 租客列表 -->
    <scroll-view scroll-y class="tenant-list" @scrolltolower="onLoadMore">
      <view v-if="loading && tenants.length === 0" class="loading-wrap">
        <up-loading-icon mode="circle"></up-loading-icon>
      </view>
      
      <view v-else-if="tenants.length === 0" class="empty-wrap">
        <up-empty text="暂无租客" mode="list"></up-empty>
      </view>
      
      <view v-else class="tenant-items">
        <view 
          v-for="item in tenants" 
          :key="item.id" 
          class="tenant-card"
          @click="onTenantClick(item)"
        >
          <view class="tenant-info">
            <view class="avatar">{{ item.name?.charAt(0) || '租' }}</view>
            <view class="detail">
              <text class="name">{{ item.name }}</text>
              <text class="phone">{{ item.phone }}</text>
              <text class="property">{{ item.propertyName || '' }}</text>
            </view>
          </view>
          <view class="status" :class="item.status === 1 ? 'active' : 'inactive'">
            {{ item.status === 1 ? '租住中' : '已退租' }}
          </view>
        </view>
      </view>
      
      <view v-if="loading && tenants.length > 0" class="loading-more">
        <up-loading-icon mode="circle"></up-loading-icon>
      </view>
    </scroll-view>
    
    <!-- 新增按钮 -->
    <view class="add-btn" @click="onCheckIn">
      <up-icon name="plus" color="#fff" size="24"></up-icon>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getTenants } from '@/services/tenant'
import type { Tenant } from '@/types'

const tenants = ref<Tenant[]>([])
const loading = ref(false)
const keyword = ref('')

const fetchTenants = async () => {
  loading.value = true
  try {
    const res = await getTenants()
    tenants.value = res || []
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  } finally {
    loading.value = false
  }
}

const onSearch = () => {
  fetchTenants()
}

const onLoadMore = () => {
  // TODO: 分页加载
}

const onTenantClick = (item: Tenant) => {
  uni.navigateTo({ url: `/pages/landlord/tenant/detail/index?id=${item.id}` })
}

const onCheckIn = () => {
  uni.navigateTo({ url: '/pages/landlord/tenant/checkin/index' })
}

onMounted(() => {
  fetchTenants()
})
</script>

<style scoped lang="scss">
.tenant-list-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.search-bar {
  padding: 20rpx 30rpx;
  background-color: #fff;
  
  .search-input {
    height: 72rpx;
    padding: 0 30rpx;
    background-color: #f5f5f5;
    border-radius: 36rpx;
    font-size: 28rpx;
  }
}

.tenant-list {
  height: calc(100vh - 112rpx - 120rpx);
}

.loading-wrap, .empty-wrap {
  padding: 100rpx 0;
}

.tenant-items {
  padding: 20rpx;
}

.tenant-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  background-color: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
}

.tenant-info {
  display: flex;
  align-items: center;
  
  .avatar {
    width: 80rpx;
    height: 80rpx;
    background-color: #0087FF;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    font-size: 32rpx;
    margin-right: 20rpx;
  }
  
  .detail {
    display: flex;
    flex-direction: column;
    
    .name {
      font-size: 32rpx;
      color: #333;
      font-weight: 500;
    }
    
    .phone {
      font-size: 26rpx;
      color: #666;
      margin-top: 8rpx;
    }
    
    .property {
      font-size: 24rpx;
      color: #999;
      margin-top: 8rpx;
    }
  }
}

.status {
  font-size: 24rpx;
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
  
  &.active {
    background-color: #e6f7ff;
    color: #0087FF;
  }
  
  &.inactive {
    background-color: #f5f5f5;
    color: #999;
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