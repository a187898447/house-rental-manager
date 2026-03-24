<template>
  <view class="history-page">
    <!-- 筛选 tabs -->
    <view class="tabs">
      <view 
        v-for="(tab, index) in tabs" 
        :key="index"
        class="tab" 
        :class="{ active: currentTab === index }"
        @click="onTabChange(index)"
      >
        {{ tab.name }}
      </view>
    </view>

    <!-- 日志列表 -->
    <scroll-view scroll-y class="history-list">
      <view v-if="loading && logs.length === 0" class="loading-wrap">
        <u-loading mode="circle"></u-loading>
      </view>
      
      <view v-else-if="logs.length === 0" class="empty-wrap">
        <u-empty text="暂无操作记录" mode="list"></u-empty>
      </view>
      
      <view v-else class="log-items">
        <view v-for="item in logs" :key="item.id" class="log-item">
          <view class="log-icon" :class="iconClass(item.actionType)">
            <text>{{ iconText(item.actionType) }}</text>
          </view>
          <view class="log-content">
            <text class="action">{{ actionText(item.actionType) }}</text>
            <text class="detail">{{ item.description }}</text>
            <text class="time">{{ item.createdAt }}</text>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getOperationLogs } from '@/services/operation-log'

const tabs = [
  { name: '全部', value: '' },
  { name: '房源', value: 'property' },
  { name: '租客', value: 'tenant' },
  { name: '账单', value: 'bill' },
  { name: '押金', value: 'deposit' }
]

const currentTab = ref(0)
const logs = ref<any[]>([])
const loading = ref(false)

const iconClass = (type: string) => {
  const map: Record<string, string> = {
    property: 'property',
    tenant: 'tenant',
    bill: 'bill',
    deposit: 'deposit',
    utility: 'utility',
    repair: 'repair'
  }
  return map[type] || 'default'
}

const iconText = (type: string) => {
  const map: Record<string, string> = {
    property: '房',
    tenant: '客',
    bill: '账',
    deposit: '押',
    utility: '水电',
    repair: '修'
  }
  return map[type] || '记'
}

const actionText = (type: string) => {
  const map: Record<string, string> = {
    property_add: '新增房源',
    property_edit: '编辑房源',
    property_delete: '删除房源',
    tenant_checkin: '入住登记',
    tenant_checkout: '退租办理',
    bill_create: '创建账单',
    bill_pay: '账单支付',
    bill_remind: '发送催租',
    deposit_pay: '缴纳押金',
    deposit_refund: '退还押金',
    utility_create: '录入水电',
    repair_submit: '提交报修',
    repair_complete: '完成报修'
  }
  return map[type] || '操作记录'
}

const fetchLogs = async () => {
  loading.value = true
  try {
    const type = tabs[currentTab].value
    const res = await getOperationLogs(type ? { type } : {})
    logs.value = res || []
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  } finally {
    loading.value = false
  }
}

const onTabChange = (index: number) => {
  currentTab.value = index
  fetchLogs()
}

onMounted(() => {
  fetchLogs()
})
</script>

<style scoped lang="scss">
.history-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.tabs {
  display: flex;
  background-color: #fff;
  padding: 20rpx 0;
  flex-wrap: wrap;
  gap: 10rpx;
  
  .tab {
    padding: 16rpx 30rpx;
    font-size: 26rpx;
    color: #666;
    background-color: #f5f5f5;
    border-radius: 30rpx;
    
    &.active {
      color: #fff;
      background-color: #0087FF;
    }
  }
}

.history-list {
  height: calc(100vh - 120rpx);
  padding: 20rpx;
}

.loading-wrap, .empty-wrap {
  padding: 100rpx 0;
}

.log-item {
  display: flex;
  background-color: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 16rpx;
}

.log-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  flex-shrink: 0;
  
  text {
    font-size: 28rpx;
    color: #fff;
    font-weight: bold;
  }
  
  &.property { background-color: #0087FF; }
  &.tenant { background-color: #52C41A; }
  &.bill { background-color: #FF9500; }
  &.deposit { background-color: #722ED1; }
  &.utility { background-color: #13C2C2; }
  &.repair { background-color: #EB2F96; }
  &.default { background-color: #999; }
}

.log-content {
  flex: 1;
  
  .action {
    display: block;
    font-size: 28rpx;
    color: #333;
    font-weight: 500;
    margin-bottom: 8rpx;
  }
  
  .detail {
    display: block;
    font-size: 26rpx;
    color: #666;
    margin-bottom: 8rpx;
  }
  
  .time {
    display: block;
    font-size: 24rpx;
    color: #999;
  }
}
</style>