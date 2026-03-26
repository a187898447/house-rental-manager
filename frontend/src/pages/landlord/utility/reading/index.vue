<template>
  <view class="utility-reading-page">
    <!-- 房源选择 -->
    <view class="section">
      <view class="section-title">选择房源</view>
      <picker 
        :range="properties" 
        range-key="name"
        @change="onPropertyChange"
      >
        <view class="picker">
          <text v-if="selectedProperty">{{ selectedProperty.name }}</text>
          <text v-else class="placeholder">请选择房源</text>
          <text class="arrow">›</text>
        </view>
      </picker>
    </view>

    <!-- 账单月份 -->
    <view class="section">
      <view class="section-title">账单月份</view>
      <picker 
        mode="date" 
        fields="month" 
        :value="billMonth"
        @change="onMonthChange"
      >
        <view class="picker">
          <text>{{ billMonth || '请选择月份' }}</text>
          <text class="arrow">›</text>
        </view>
      </picker>
    </view>

    <!-- 水表读数 -->
    <view class="section">
      <view class="section-title">水表读数（吨）</view>
      <view class="form-item">
        <text class="label">本月读数</text>
        <input 
          v-model.number="formData.waterReadingCurrent" 
          class="input" 
          type="digit"
          placeholder="请输入水表读数"
        />
      </view>
      <view class="form-item">
        <text class="label">上月读数（自动）</text>
        <input 
          v-model.number="formData.waterReading" 
          class="input" 
          type="digit"
          placeholder="自动获取"
          disabled
        />
      </view>
      <view class="calc-result" v-if="waterUsage > 0">
        <text>用水量：{{ waterUsage }} 吨</text>
        <text>金额：¥{{ waterAmount }}</text>
      </view>
    </view>

    <!-- 电表读数 -->
    <view class="section">
      <view class="section-title">电表读数（度）</view>
      <view class="form-item">
        <text class="label">本月读数</text>
        <input 
          v-model.number="formData.electricityReadingCurrent" 
          class="input" 
          type="digit"
          placeholder="请输入电表读数"
        />
      </view>
      <view class="form-item">
        <text class="label">上月读数（自动）</text>
        <input 
          v-model.number="formData.electricityReading" 
          class="input" 
          type="digit"
          placeholder="自动获取"
          disabled
        />
      </view>
      <view class="calc-result" v-if="electricityUsage > 0">
        <text>用电量：{{ electricityUsage }} 度</text>
        <text>金额：¥{{ electricityAmount }}</text>
      </view>
    </view>

    <!-- 提交 -->
    <view class="submit-btn" @click="onSubmit">
      <text>生成账单</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { getProperties } from '@/services/property'
import { getUtilityConfig } from '@/services/utility'
import { createUtilityBill } from '@/services/utility'

const properties = ref<any[]>([])
const selectedProperty = ref<any>(null)
const billMonth = ref('')
const config = ref<any>(null)

const formData = reactive({
  propertyId: null as number | null,
  tenantId: null as number | null,
  billMonth: '',
  waterReading: null as number | null,
  waterReadingCurrent: null as number | null,
  waterAmount: null as number | null,
  electricityReading: null as number | null,
  electricityReadingCurrent: null as number | null,
  electricityAmount: null as number | null,
  source: 1 // 手动录入
})

// 计算用水量
const waterUsage = computed(() => {
  if (formData.waterReading && formData.waterReadingCurrent) {
    return formData.waterReadingCurrent - formData.waterReading
  }
  return 0
})

// 计算水费
const waterAmount = computed(() => {
  if (waterUsage.value > 0 && config.value?.waterPrice) {
    return (waterUsage.value * config.value.waterPrice).toFixed(2)
  }
  return '0.00'
})

// 用电量
const electricityUsage = computed(() => {
  if (formData.electricityReading && formData.electricityReadingCurrent) {
    return formData.electricityReadingCurrent - formData.electricityReading
  }
  return 0
})

// 电费
const electricityAmount = computed(() => {
  if (electricityUsage.value > 0 && config.value?.electricityPrice) {
    return (electricityUsage.value * config.value.electricityPrice).toFixed(2)
  }
  return '0.00'
})

const fetchProperties = async () => {
  try {
    const res = await getProperties({ status: 1 }) // 已出租
    properties.value = res || []
  } catch (e) {
    console.error('fetchProperties error:', e)
  }
}

const onPropertyChange = async (e: any) => {
  const index = e.detail.value
  selectedProperty.value = properties.value[index]
  formData.propertyId = selectedProperty.value.id
  
  // 获取该房源的水电单价配置
  try {
    const cfg = await getUtilityConfig(String(selectedProperty.value.id))
    config.value = cfg
  } catch (e) {
    console.error('getUtilityConfig error:', e)
  }
  
  // 获取上月读数
  // TODO: 调用 API 获取上月读数
}

const onMonthChange = (e: any) => {
  billMonth.value = e.detail.value
  formData.billMonth = billMonth.value
}

const onSubmit = () => {
  if (!formData.propertyId) {
    uni.showToast({ title: '请选择房源', icon: 'none' })
    return
  }
  if (!formData.billMonth) {
    uni.showToast({ title: '请选择月份', icon: 'none' })
    return
  }
  if (!formData.waterReadingCurrent && !formData.electricityReadingCurrent) {
    uni.showToast({ title: '请至少填写一个读数', icon: 'none' })
    return
  }

  // 计算费用
  formData.waterAmount = waterUsage.value > 0 ? Number(waterAmount.value) : null
  formData.electricityAmount = electricityUsage.value > 0 ? Number(electricityAmount.value) : null

  uni.showLoading({ title: '提交中...' })
  
  createUtilityBill(formData)
    .then(() => {
      uni.showToast({ title: '账单生成成功', icon: 'success' })
      setTimeout(() => {
        uni.navigateBack()
      }, 1500)
    })
    .catch((err) => {
      uni.showToast({ title: err.message || '生成失败', icon: 'none' })
    })
    .finally(() => {
      uni.hideLoading()
    })
}

onMounted(() => {
  fetchProperties()
})
</script>

<style scoped lang="scss">
.utility-reading-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
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

.picker {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 80rpx;
  padding: 0 20rpx;
  background-color: #f5f5f5;
  border-radius: 8rpx;
  
  .placeholder {
    color: #999;
  }
  
  .arrow {
    font-size: 32rpx;
    color: #999;
  }
}

.form-item {
  margin-bottom: 24rpx;
  
  .label {
    display: block;
    font-size: 28rpx;
    color: #666;
    margin-bottom: 12rpx;
  }
  
  .input {
    width: 100%;
    height: 80rpx;
    padding: 0 20rpx;
    background-color: #f5f5f5;
    border-radius: 8rpx;
    font-size: 28rpx;
  }
  
  .input[disabled] {
    color: #999;
  }
}

.calc-result {
  display: flex;
  justify-content: space-between;
  padding: 20rpx;
  background-color: #f0f9ff;
  border-radius: 8rpx;
  font-size: 28rpx;
  color: #0087FF;
}

.submit-btn {
  position: fixed;
  left: 30rpx;
  right: 30rpx;
  bottom: 40rpx;
  height: 88rpx;
  background-color: #0087FF;
  border-radius: 44rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  
  text {
    color: #fff;
    font-size: 32rpx;
  }
}
</style>