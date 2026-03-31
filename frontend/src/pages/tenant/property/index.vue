<template>
  <view class="tenant-property">
    <view class="empty" v-if="!property">
      <text class="empty-text">暂无绑定房源</text>
      <button class="bind-btn" @click="bindProperty">绑定房源</button>
    </view>
    <view class="property-detail" v-else>
      <image class="property-img" :src="property.image || ''" mode="aspectFill" />
      <view class="property-info">
        <text class="name">{{ property.name }}</text>
        <text class="address">{{ property.address }}</text>
        <view class="tags">
          <text class="tag">{{ property.building }}栋</text>
          <text class="tag">{{ property.roomNo }}室</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const property = ref<any>(null)

const bindProperty = () => {
  uni.showToast({ title: '请联系房东绑定房源', icon: 'none' })
}
</script>

<style scoped>
.tenant-property {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 24rpx;
}

.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 200rpx 0;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
  margin-bottom: 32rpx;
}

.bind-btn {
  width: 200rpx;
  height: 80rpx;
  background: #007AFF;
  color: #fff;
  border-radius: 40rpx;
  font-size: 28rpx;
}

.property-detail {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}

.property-img {
  width: 100%;
  height: 400rpx;
  background: #eee;
}

.property-info {
  padding: 24rpx;
}

.name {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}

.address {
  font-size: 26rpx;
  color: #666;
  margin-top: 8rpx;
  display: block;
}

.tags {
  display: flex;
  margin-top: 16rpx;
}

.tag {
  background: #f0f0f0;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: #666;
  margin-right: 16rpx;
}
</style>