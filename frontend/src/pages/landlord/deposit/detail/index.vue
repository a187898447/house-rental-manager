<template>
  <view class="deposit-detail-page" v-if="deposit">
    <!-- 状态 -->
    <view class="status-banner" :class="statusClass">
      <text class="status-text">{{ statusText }}</text>
    </view>

    <!-- 基本信息 -->
    <view class="section">
      <view class="section-title">基本信息</view>
      <view class="info-row">
        <text class="label">房源</text>
        <text class="value">{{ deposit.propertyName }}</text>
      </view>
      <view class="info-row">
        <text class="label">房源地址</text>
        <text class="value">{{ deposit.propertyAddress }}</text>
      </view>
      <view class="info-row">
        <text class="label">租客</text>
        <text class="value">{{ deposit.tenantName }}</text>
      </view>
      <view class="info-row">
        <text class="label">联系电话</text>
        <text class="value">{{ deposit.tenantPhone }}</text>
      </view>
    </view>

    <!-- 押金信息 -->
    <view class="section">
      <view class="section-title">押金信息</view>
      <view class="info-row">
        <text class="label">押金金额</text>
        <text class="value amount">¥{{ deposit.amount }}</text>
      </view>
      <view class="info-row" v-if="deposit.payMethod">
        <text class="label">支付方式</text>
        <text class="value">{{ payMethodText }}</text>
      </view>
      <view class="info-row" v-if="deposit.payTime">
        <text class="label">支付时间</text>
        <text class="value">{{ deposit.payTime }}</text>
      </view>
      <view class="info-row" v-if="deposit.refundAmount">
        <text class="label">实退金额</text>
        <text class="value amount">¥{{ deposit.refundAmount }}</text>
      </view>
      <view class="info-row" v-if="deposit.refundTime">
        <text class="label">退还时间</text>
        <text class="value">{{ deposit.refundTime }}</text>
      </view>
      <view class="info-row" v-if="deposit.refundRemark">
        <text class="label">退还备注</text>
        <text class="value">{{ deposit.refundRemark }}</text>
      </view>
    </view>

    <!-- 合同信息 -->
    <view class="section" v-if="deposit.contractId">
      <view class="section-title">合同信息</view>
      <view class="info-row">
        <text class="label">合同编号</text>
        <text class="value">{{ deposit.contractId }}</text>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-btns" v-if="deposit.status === 0">
      <view class="btn primary" @click="onPay">
        <text>确认收款</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { getDepositDetail, payDeposit } from '@/services/deposit'

const deposit = ref<any>(null)

const statusText = computed(() => {
  const map = { 0: '待缴纳', 1: '已缴纳', 2: '部分退还', 3: '已退还' }
  return map[deposit.value?.status] || '未知'
})

const statusClass = computed(() => {
  const map = { 0: 'pending', 1: 'paid', 2: 'partial', 3: 'refunded' }
  return map[deposit.value?.status] || ''
})

const payMethodText = computed(() => {
  const map = { 
    'alipay': '支付宝', 
    'wechat': '微信', 
    'bank': '银行转账', 
    'cash': '现金' 
  }
  return map[deposit.value?.payMethod] || deposit.value?.payMethod || '-'
})

const fetchDetail = async () => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1] as any
  const id = currentPage?.options?.id
  
  if (!id) {
    uni.showToast({ title: '参数错误', icon: 'none' })
    return
  }
  
  try {
    const res = await getDepositDetail(id)
    deposit.value = res
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  }
}

const onPay = () => {
  uni.showModal({
    title: '确认收款',
    content: `确认收到押金 ¥${deposit.value.amount} 吗？`,
    success: async (res) => {
      if (res.confirm) {
        try {
          await payDeposit(deposit.value.id, 'cash')
          uni.showToast({ title: '操作成功', icon: 'success' })
          fetchDetail()
        } catch (e: any) {
          uni.showToast({ title: e?.message || '操作失败', icon: 'none' })
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
.deposit-detail-page {
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
    background-color: #e6f7ff;
    .status-text { color: #1890ff; }
  }
  &.partial {
    background-color: #fff1f0;
    .status-text { color: #ff4d4f; }
  }
  &.refunded {
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
  }
}
</style>