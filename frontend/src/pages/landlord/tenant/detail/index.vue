<template>
  <view class="tenant-detail-page" v-if="tenant">
    <!-- 基本信息 -->
    <view class="section">
      <view class="section-title">基本信息</view>
      <view class="info-row">
        <text class="label">姓名</text>
        <text class="value">{{ tenant.name }}</text>
      </view>
      <view class="info-row">
        <text class="label">手机号</text>
        <text class="value">{{ tenant.phone }}</text>
      </view>
      <view class="info-row">
        <text class="label">身份证号</text>
        <text class="value">{{ tenant.idCard || '-' }}</text>
      </view>
      <view class="info-row">
        <text class="label">状态</text>
        <text class="value" :class="tenant.status === 1 ? 'active' : ''">
          {{ tenant.status === 1 ? '租住中' : '已退租' }}
        </text>
      </view>
    </view>

    <!-- 房源信息 -->
    <view class="section">
      <view class="section-title">房源信息</view>
      <view class="info-row">
        <text class="label">房源</text>
        <text class="value">{{ tenant.propertyName || '-' }}</text>
      </view>
      <view class="info-row">
        <text class="label">租约期</text>
        <text class="value">{{ tenant.leaseStartDate }} ~ {{ tenant.leaseEndDate }}</text>
      </view>
    </view>

    <!-- 紧急联系人 -->
    <view class="section" v-if="tenant.emergencyContact">
      <view class="section-title">紧急联系人</view>
      <view class="info-row">
        <text class="label">联系人</text>
        <text class="value">{{ tenant.emergencyContact }}</text>
      </view>
      <view class="info-row">
        <text class="label">联系电话</text>
        <text class="value">{{ tenant.emergencyPhone }}</text>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="actions" v-if="tenant.status === 1">
      <view class="action-btn checkout" @click="onCheckout">办理退租</view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getTenantDetail } from '@/services/tenant'

const tenantId = ref('')
const tenant = ref<any>(null)

const fetchDetail = async () => {
  try {
    const res = await getTenantDetail(tenantId.value)
    tenant.value = res
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  }
}

const onCheckout = () => {
  uni.navigateTo({ url: `/pages/landlord/tenant/checkout/index?id=${tenantId.value}` })
}

onMounted(() => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1] as any
  const options = currentPage?.options || {}
  tenantId.value = options.id
  fetchDetail()
})
</script>

<style scoped lang="scss">
.tenant-detail-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.section {
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 24rpx;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 20rpx 0;
  border-bottom: 1px solid #f5f5f5;
  
  &:last-child {
    border-bottom: none;
  }
  
  .label {
    font-size: 28rpx;
    color: #666;
  }
  
  .value {
    font-size: 28rpx;
    color: #333;
    
    &.active {
      color: #52C41A;
    }
  }
}

.actions {
  padding: 30rpx;
  
  .action-btn {
    width: 100%;
    height: 88rpx;
    background-color: #FF9500;
    border-radius: 44rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    font-size: 32rpx;
  }
}
</style>
