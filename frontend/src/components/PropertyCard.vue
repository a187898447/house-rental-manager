<template>
  <view class="property-card" @click="handleClick">
    <!-- 房源图片 -->
    <view class="card-image">
      <image 
        v-if="data.images && data.images.length > 0" 
        :src="data.images[0]" 
        mode="aspectFill"
      />
      <view v-else class="no-image">
        <u-icon name="picture" size="40" color="#ccc"></u-icon>
      </view>
      <!-- 状态标签 -->
      <view class="status-tag" :class="statusClass">
        {{ statusText }}
      </view>
    </view>
    
    <!-- 房源信息 -->
    <view class="card-info">
      <view class="info-row">
        <text class="building">{{ data.building }}</text>
        <text class="unit">{{ data.unit }}-{{ data.roomNumber }}</text>
      </view>
      <view class="info-row">
        <text class="rent">¥{{ data.rent }}/月</text>
        <text v-if="data.area" class="area">{{ data.area }}㎡</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { Property } from '@/types'

const props = defineProps<{
  data: Property
}>()

const emit = defineEmits(['click'])

const statusMap: Record<string, string> = {
  vacant: '未出租',
  rented: '已出租',
  rented_unpaid: '未缴费',
  rented_paid: '已缴费'
}

const statusClass = computed(() => {
  const status = props.data.status
  if (status === 'vacant') return 'status-vacant'
  if (status === 'rented_unpaid') return 'status-unpaid'
  return 'status-rented'
})

const statusText = computed(() => statusMap[props.data.status] || '')

const handleClick = () => {
  emit('click', props.data)
}
</script>

<style lang="scss" scoped>
.property-card {
  background-color: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
}

.card-image {
  position: relative;
  width: 100%;
  height: 320rpx;
  background-color: #f5f5f5;
  
  image {
    width: 100%;
    height: 100%;
  }
  
  .no-image {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .status-tag {
    position: absolute;
    top: 16rpx;
    right: 16rpx;
    padding: 6rpx 16rpx;
    border-radius: 20rpx;
    font-size: 24rpx;
    color: #fff;
    
    &.status-vacant {
      background-color: #5AC725;
    }
    
    &.status-unpaid {
      background-color: #FFB100;
    }
    
    &.status-rented {
      background-color: #0087FF;
    }
  }
}

.card-info {
  padding: 20rpx;
  
  .info-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .building {
    font-size: 28rpx;
    font-weight: 600;
    color: #333;
  }
  
  .unit {
    font-size: 26rpx;
    color: #666;
  }
  
  .rent {
    font-size: 32rpx;
    font-weight: 600;
    color: #0087FF;
  }
  
  .area {
    font-size: 24rpx;
    color: #999;
  }
}
</style>
