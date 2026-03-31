<template>
  <view class="tenant-index">
    <!-- 顶部信息 -->
    <view class="header">
      <view class="user-info">
        <image class="avatar" :src="userInfo.avatar || '/static/default-avatar.png'" mode="aspectFill" />
        <view class="info">
          <text class="nickname">{{ userInfo.nickname || '租客' }}</text>
          <text class="phone">{{ userInfo.phone || '' }}</text>
        </view>
      </view>
    </view>

    <!-- 我的房源 -->
    <view class="section">
      <view class="section-title">我的房源</view>
      <view class="property-card" @click="goToProperty">
        <image class="property-img" src="" mode="aspectFill" />
        <view class="property-info">
          <text class="property-name">{{ propertyInfo.name || '未绑定房源' }}</text>
          <text class="property-address">{{ propertyInfo.address || '请绑定房源' }}</text>
        </view>
        <text class="arrow">></text>
      </view>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-grid">
      <view class="menu-item" @click="goToPage('/pages/tenant/bill/list/index')">
        <text class="menu-icon">📄</text>
        <text class="menu-text">账单明细</text>
      </view>
      <view class="menu-item" @click="goToPage('/pages/tenant/contract/view/index')">
        <text class="menu-icon">📝</text>
        <text class="menu-text">合同查看</text>
      </view>
      <view class="menu-item" @click="goToPage('/pages/tenant/repair/apply/index')">
        <text class="menu-icon">🔧</text>
        <text class="menu-text">报修申请</text>
      </view>
      <view class="menu-item" @click="goToPage('/pages/tenant/contact/index/index')">
        <text class="menu-icon">📞</text>
        <text class="menu-text">联系房东</text>
      </view>
      <view class="menu-item" @click="goToSettings">
        <text class="menu-icon">⚙️</text>
        <text class="menu-text">设置</text>
      </view>
    </view>

    <!-- 待缴费提醒 -->
    <view class="notice" v-if="pendingBillCount > 0" @click="goToPage('/pages/tenant/bill/list/index')">
      <text class="notice-text">您有 {{ pendingBillCount }} 条待缴费账单，点击查看</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onShow } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const userInfo = ref<any>({})
const propertyInfo = ref<any>({})
const pendingBillCount = ref(0)

onShow(() => {
  // 获取用户信息
  if (userStore.userInfo) {
    userInfo.value = userStore.userInfo
  }
  
  // TODO: 获取房源信息
  // TODO: 获取待缴费账单数量
})

const goToPage = (url: string) => {
  uni.navigateTo({ url })
}

const goToProperty = () => {
  uni.navigateTo({ url: '/pages/tenant/property/index/index' })
}

const goToSettings = () => {
  uni.navigateTo({ url: '/pages/tenant/settings/index/index' })
}
</script>

<style scoped>
.tenant-index {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 24rpx;
}

.header {
  background: #007AFF;
  border-radius: 16rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 96rpx;
  height: 96rpx;
  border-radius: 48rpx;
  background: #fff;
  margin-right: 24rpx;
}

.info {
  display: flex;
  flex-direction: column;
}

.nickname {
  font-size: 32rpx;
  color: #fff;
  font-weight: 600;
}

.phone {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-top: 8rpx;
}

.section {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 24rpx;
}

.section-title {
  font-size: 28rpx;
  color: #333;
  font-weight: 600;
  margin-bottom: 16rpx;
}

.property-card {
  display: flex;
  align-items: center;
  background: #f9f9f9;
  border-radius: 12rpx;
  padding: 20rpx;
}

.property-img {
  width: 120rpx;
  height: 80rpx;
  background: #eee;
  border-radius: 8rpx;
  margin-right: 16rpx;
}

.property-info {
  flex: 1;
}

.property-name {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
}

.property-address {
  font-size: 24rpx;
  color: #999;
  margin-top: 8rpx;
}

.arrow {
  font-size: 28rpx;
  color: #999;
}

.menu-grid {
  display: flex;
  flex-wrap: wrap;
  background: #fff;
  border-radius: 16rpx;
  padding: 16rpx;
}

.menu-item {
  width: 25%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24rpx 0;
}

.menu-icon {
  font-size: 48rpx;
  margin-bottom: 8rpx;
}

.menu-text {
  font-size: 24rpx;
  color: #666;
}

.notice {
  background: #fff7e6;
  border-radius: 12rpx;
  padding: 24rpx;
  margin-top: 24rpx;
  text-align: center;
}

.notice-text {
  font-size: 26rpx;
  color: #fa8c16;
}
</style>