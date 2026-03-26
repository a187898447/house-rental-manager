<template>
  <view class="fee-detail-page" v-if="fee">
    <!-- 状态 -->
    <view class="status-banner" :class="fee.status === 1 ? 'paid' : 'pending'">
      <text class="status-text">{{ fee.status === 1 ? '已支付' : '待支付' }}</text>
    </view>

    <!-- 基本信息 -->
    <view class="section">
      <view class="section-title">基本信息</view>
      <view class="info-row">
        <text class="label">费用类型</text>
        <text class="value">{{ typeText(fee.feeType) }}</text>
      </view>
      <view class="info-row">
        <text class="label">房源</text>
        <text class="value">{{ fee.propertyName }}</text>
      </view>
      <view class="info-row">
        <text class="label">租客</text>
        <text class="value">{{ fee.tenantName }}</text>
      </view>
      <view class="info-row">
        <text class="label">金额</text>
        <text class="value amount">¥{{ fee.amount }}</text>
      </view>
      <view class="info-row">
        <text class="label">账单月份</text>
        <text class="value">{{ fee.month }}</text>
      </view>
      <view class="info-row" v-if="fee.remark">
        <text class="label">备注</text>
        <text class="value">{{ fee.remark }}</text>
      </view>
      <view class="info-row" v-if="fee.payDate">
        <text class="label">支付时间</text>
        <text class="value">{{ fee.payDate }}</text>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-btns" v-if="fee.status === 0">
      <view class="btn danger" @click="onDelete">
        <text>删除</text>
      </view>
      <view class="btn primary" @click="onPay">
        <text>确认支付</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { getOtherFeeDetail, payOtherFee, deleteOtherFee } from '@/services/otherfee'

const fee = ref<any>(null)

const typeText = (type: string) => {
  const map = { 
    property: '物业费', 
    garbage: '垃圾清运费', 
    network: '网络费',
    tv: '电视费',
    parking: '车位费',
    other: '其他'
  }
  return map[type] || type
}

const fetchDetail = async () => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1] as any
  const id = currentPage?.options?.id
  
  if (!id) {
    uni.showToast({ title: '参数错误', icon: 'none' })
    return
  }
  
  try {
    const res = await getOtherFeeDetail(Number(id))
    fee.value = res
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  }
}

const onPay = () => {
  uni.showModal({
    title: '确认支付',
    content: `确认该费用已支付吗？`,
    success: async (res) => {
      if (res.confirm) {
        try {
          await payOtherFee(fee.value.id)
          uni.showToast({ title: '操作成功', icon: 'success' })
          fetchDetail()
        } catch (e: any) {
          uni.showToast({ title: e?.message || '操作失败', icon: 'none' })
        }
      }
    }
  })
}

const onDelete = () => {
  uni.showModal({
    title: '确认删除',
    content: '确定要删除这条费用记录吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          await deleteOtherFee(fee.value.id)
          uni.showToast({ title: '删除成功', icon: 'success' })
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
        } catch (e: any) {
          uni.showToast({ title: e?.message || '删除失败', icon: 'none' })
        }
      }
    }
  })
}

onMounted(() => {
  fetchDetail()
})
</script>

<style scoped lang="scss">
.fee-detail-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

.status-banner {
  padding: 40rpx 30rpx;
  
  &.pending {
    background-color: #fff7e6;
    .status-text { color: #fa8c16; }
  }
  &.paid {
    background-color: #f6ffed;
    .status-text { color: #52c41a; }
  }
  
  .status-text {
    font-size: 32rpx;
    font-weight: bold;
  }
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
  padding: 16rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
  
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
    
    &.amount {
      font-weight: bold;
      color: #ff4d4f;
    }
  }
}

.action-btns {
  position: fixed;
  left: 30rpx;
  right: 30rpx;
  bottom: 40rpx;
  display: flex;
  gap: 20rpx;
  
  .btn {
    flex: 1;
    height: 88rpx;
    border-radius: 44rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 32rpx;
    
    &.primary {
      background-color: #0087FF;
      color: #fff;
    }
    &.danger {
      background-color: #ff4d4f;
      color: #fff;
    }
  }
}
</style>