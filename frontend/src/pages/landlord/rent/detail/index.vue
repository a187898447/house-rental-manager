<template>
  <view class="bill-detail-page" v-if="detail">
    <!-- 基本信息 -->
    <view class="section info-section">
      <view class="header">
        <text class="month">{{ detail.payMonth }} 月租金</text>
        <text class="status" :class="statusClass(detail.status)">
          {{ statusText(detail.status) }}
        </text>
      </view>
      
      <view class="amount-row">
        <text class="label">租金金额</text>
        <text class="amount">¥{{ detail.amount }}</text>
      </view>
    </view>

    <!-- 房源信息 -->
    <view class="section">
      <view class="section-title">房源信息</view>
      <view class="info-row">
        <text class="label">房源名称</text>
        <text class="value">{{ detail.propertyName }}</text>
      </view>
      <view class="info-row">
        <text class="label">房源地址</text>
        <text class="value">{{ detail.propertyAddress }}</text>
      </view>
    </view>

    <!-- 租客信息 -->
    <view class="section">
      <view class="section-title">租客信息</view>
      <view class="info-row">
        <text class="label">租客姓名</text>
        <text class="value">{{ detail.tenantName }}</text>
      </view>
      <view class="info-row">
        <text class="label">联系电话</text>
        <text class="value">{{ detail.tenantPhone }}</text>
      </view>
    </view>

    <!-- 支付信息 -->
    <view class="section">
      <view class="section-title">支付信息</view>
      <view class="info-row">
        <text class="label">支付方式</text>
        <text class="value">{{ payMethodText(detail.payMethod) }}</text>
      </view>
      <view class="info-row" v-if="detail.payDate">
        <text class="label">支付日期</text>
        <text class="value">{{ detail.payDate }}</text>
      </view>
      <view class="info-row">
        <text class="label">催租次数</text>
        <text class="value">{{ detail.remindCount || 0 }} 次</text>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="actions" v-if="detail.status === 0">
      <view class="action-btn remind" @click="onRemind">
        <text>发送催租提醒</text>
      </view>
      <view class="action-btn confirm" @click="onConfirm">
        <text>确认已收款</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getRentBillDetail, markBillPaid, sendRentReminder } from '@/services/rent'

const detail = ref<any>(null)
const billId = ref('')

const statusText = (status: number) => {
  const map = { 0: '待支付', 1: '已支付', 2: '已逾期', 3: '已取消' }
  return map[status] || '未知'
}

const statusClass = (status: number) => {
  const map = { 0: 'pending', 1: 'paid', 2: 'overdue' }
  return map[status] || ''
}

const payMethodText = (method?: string) => {
  const map = { cash: '现金', transfer: '转账', wechat: '微信', alipay: '支付宝' }
  return map[method || ''] || '未选择'
}

const fetchDetail = async () => {
  try {
    const res = await getRentBillDetail(billId.value)
    detail.value = res
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  }
}

const onRemind = () => {
  sendRentReminder(billId.value).then(() => {
    uni.showToast({ title: '催租提醒已发送', icon: 'success' })
  })
}

const onConfirm = () => {
  uni.showModal({
    title: '确认收款',
    content: `确认收到 ${detail.value.amount} 元租金吗？`,
    success: (res) => {
      if (res.confirm) {
        markBillPaid(billId.value).then(() => {
          uni.showToast({ title: '已确认收款', icon: 'success' })
          fetchDetail()
        })
      }
    }
  })
}

onMounted(() => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1] as any
  const options = currentPage?.options || {}
  
  if (options.id) {
    billId.value = options.id
    fetchDetail()
  }
})
</script>

<style scoped lang="scss">
.bill-detail-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 200rpx;
}

.section {
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.info-section {
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30rpx;
    
    .month {
      font-size: 36rpx;
      font-weight: bold;
      color: #333;
    }
    
    .status {
      font-size: 24rpx;
      padding: 8rpx 20rpx;
      border-radius: 20rpx;
      
      &.pending { background-color: #fff7e6; color: #FF9500; }
      &.paid { background-color: #e6fff2; color: #52C41A; }
      &.overdue { background-color: #fff1f0; color: #FF4D4F; }
    }
  }
  
  .amount-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx 0;
    border-top: 1px solid #f5f5f5;
    
    .label {
      font-size: 28rpx;
      color: #666;
    }
    
    .amount {
      font-size: 48rpx;
      font-weight: bold;
      color: #0087FF;
    }
  }
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
  }
}

.actions {
  position: fixed;
  left: 30rpx;
  right: 30rpx;
  bottom: 40rpx;
  display: flex;
  gap: 20rpx;
  
  .action-btn {
    flex: 1;
    height: 88rpx;
    border-radius: 44rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    
    text {
      font-size: 32rpx;
    }
    
    &.remind {
      border: 2rpx solid #0087FF;
      color: #0087FF;
      background-color: #fff;
    }
    
    &.confirm {
      background-color: #0087FF;
      color: #fff;
    }
  }
}
</style>