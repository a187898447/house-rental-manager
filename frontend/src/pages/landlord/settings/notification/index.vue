<template>
  <view class="notification-settings-page">
    <view class="section">
      <view class="section-title">收租提醒</view>
      <view class="switch-item">
        <text class="label">提前提醒天数</text>
        <up-number-box v-model="notifyDays" :min="1" :max="30"></up-number-box>
      </view>
    </view>

    <view class="section">
      <view class="section-title">通知方式</view>
      <view class="switch-item">
        <text class="label">短信通知</text>
        <switch :checked="smsEnabled" @change="smsEnabled = !smsEnabled" color="#0087FF"/>
      </view>
      <view class="switch-item">
        <text class="label">微信通知</text>
        <switch :checked="wechatEnabled" @change="wechatEnabled = !wechatEnabled" color="#0087FF"/>
      </view>
    </view>

    <view class="save-btn" @click="onSave">保存设置</view>
  </view>
</template>

<script setup lang="ts">
import { ref, onShow } from 'vue'
import { getNotificationSettings, updateNotificationSettings } from '@/services/notification'

const notifyDays = ref(3)
const smsEnabled = ref(true)
const wechatEnabled = ref(true)
const loading = ref(false)

// 每次页面显示时刷新数据
onShow(async () => {
  loading.value = true
  try {
    const settings = await getNotificationSettings()
    if (settings) {
      notifyDays.value = settings.notifyDays || 3
      smsEnabled.value = settings.smsEnabled ?? true
      wechatEnabled.value = settings.wechatEnabled ?? true
    }
  } catch (e) {
    console.error('获取通知设置失败', e)
  } finally {
    loading.value = false
  }
})

const onSave = async () => {
  loading.value = true
  try {
    await updateNotificationSettings({
      notifyDays: notifyDays.value,
      smsEnabled: smsEnabled.value,
      wechatEnabled: wechatEnabled.value
    })
    uni.showToast({ title: '保存成功', icon: 'success' })
    setTimeout(() => uni.navigateBack(), 1500)
  } catch (e: any) {
    uni.showToast({ title: e.message || '保存失败', icon: 'none' })
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.notification-settings-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 30rpx;
}

.section {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 24rpx;
}

.switch-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1px solid #f5f5f5;
  
  &:last-child {
    border-bottom: none;
  }
  
  .label {
    font-size: 28rpx;
    color: #333;
  }
}

.save-btn {
  height: 88rpx;
  background: linear-gradient(135deg, #667EEA 0%, #764BA2 100%);
  border-radius: 44rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 20rpx rgba(102, 126, 234, 0.4);
  color: #fff;
  font-size: 32rpx;
  font-weight: 600;
}
</style>
