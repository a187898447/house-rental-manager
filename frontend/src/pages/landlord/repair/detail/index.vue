<template>
  <view class="repair-detail-page" v-if="repair">
    <!-- 状态 -->
    <view class="status-banner" :class="statusClass(repair.status)">
      <text class="status-text">{{ statusText(repair.status) }}</text>
    </view>

    <!-- 基本信息 -->
    <view class="section">
      <view class="section-title">报修信息</view>
      <view class="info-row">
        <text class="label">报修标题</text>
        <text class="value">{{ repair.title }}</text>
      </view>
      <view class="info-row">
        <text class="label">房源</text>
        <text class="value">{{ repair.propertyName }}</text>
      </view>
      <view class="info-row">
        <text class="label">租客</text>
        <text class="value">{{ repair.tenantName }}</text>
      </view>
      <view class="info-row">
        <text class="label">联系电话</text>
        <text class="value">{{ repair.tenantPhone }}</text>
      </view>
      <view class="info-row">
        <text class="label">问题描述</text>
        <text class="value">{{ repair.description }}</text>
      </view>
      <view class="info-row">
        <text class="label">提交时间</text>
        <text class="value">{{ repair.createdAt }}</text>
      </view>
    </view>

    <!-- 处理信息 -->
    <view class="section" v-if="repair.status >= 1">
      <view class="section-title">处理信息</view>
      <view class="info-row" v-if="repair.handlerName">
        <text class="label">处理人</text>
        <text class="value">{{ repair.handlerName }}</text>
      </view>
      <view class="info-row" v-if="repair.processTime">
        <text class="label">开始处理时间</text>
        <text class="value">{{ repair.processTime }}</text>
      </view>
      <view class="info-row" v-if="repair.completeTime">
        <text class="label">完成时间</text>
        <text class="value">{{ repair.completeTime }}</text>
      </view>
      <view class="info-row" v-if="repair.remark">
        <text class="label">处理备注</text>
        <text class="value">{{ repair.remark }}</text>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-btns" v-if="repair.status === 0">
      <view class="btn danger" @click="onCancel">
        <text>取消报修</text>
      </view>
      <view class="btn primary" @click="onStartProcess">
        <text>开始处理</text>
      </view>
    </view>
    <view class="action-btns" v-else-if="repair.status === 1">
      <view class="btn primary" @click="onComplete">
        <text>完成处理</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { getRepairDetail, startRepairProcess, completeRepair, cancelRepair } from '@/services/repair'

const repair = ref<any>(null)

const statusText = (status: number) => {
  const map = { 0: '待处理', 1: '处理中', 2: '已完成', 3: '已取消' }
  return map[status] || '未知'
}

const statusClass = (status: number) => {
  const map = { 0: 'pending', 1: 'processing', 2: 'completed', 3: 'cancelled' }
  return map[status] || ''
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
    const res = await getRepairDetail(id)
    repair.value = res
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  }
}

const onStartProcess = () => {
  uni.showModal({
    title: '开始处理',
    content: '确认开始处理此报修吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          await startRepairProcess(repair.value.id)
          uni.showToast({ title: '已开始处理', icon: 'success' })
          fetchDetail()
        } catch (e: any) {
          uni.showToast({ title: e?.message || '操作失败', icon: 'none' })
        }
      }
    }
  })
}

const onComplete = () => {
  uni.showModal({
    title: '完成处理',
    content: '确认已完成维修吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          await completeRepair(repair.value.id)
          uni.showToast({ title: '处理完成', icon: 'success' })
          fetchDetail()
        } catch (e: any) {
          uni.showToast({ title: e?.message || '操作失败', icon: 'none' })
        }
      }
    }
  })
}

const onCancel = () => {
  uni.showModal({
    title: '取消报修',
    content: '确定要取消此报修吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          await cancelRepair(repair.value.id)
          uni.showToast({ title: '已取消', icon: 'success' })
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
.repair-detail-page {
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
  &.processing {
    background-color: #e6f7ff;
    .status-text { color: #1890ff; }
  }
  &.completed {
    background-color: #f6ffed;
    .status-text { color: #52c41a; }
  }
  &.cancelled {
    background-color: #f5f5f5;
    .status-text { color: #999; }
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
    flex-shrink: 0;
  }
  
  .value {
    font-size: 28rpx;
    color: #333;
    text-align: right;
    word-break: break-all;
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