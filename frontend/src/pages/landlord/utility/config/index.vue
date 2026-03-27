<template>
  <view class="utility-config-page">
    <!-- 水费配置 -->
    <view class="section">
      <view class="section-title">水费配置</view>
      
      <view class="form-item">
        <text class="label">水费单价（元/吨）</text>
        <input 
          v-model.number="formData.waterPrice" 
          class="input" 
          type="digit"
          placeholder="请输入水费单价"
        />
      </view>
    </view>

    <!-- 电费配置 -->
    <view class="section">
      <view class="section-title">电费配置</view>
      
      <view class="form-item">
        <text class="label">电费单价（元/度）</text>
        <input 
          v-model.number="formData.electricityPrice" 
          class="input" 
          type="digit"
          placeholder="请输入电费单价"
        />
      </view>
    </view>

    <!-- 提交 -->
    <view class="submit-btn" @click="onSubmit">
      <text>保存配置</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getUtilityConfig, setUtilityConfig } from '@/services/utility'

const propertyId = ref('')

const formData = reactive({
  waterPrice: null as number | null,
  electricityPrice: null as number | null
})

const fetchConfig = async () => {
  if (!propertyId.value) return
  try {
    const res = await getUtilityConfig(propertyId.value)
    if (res) {
      formData.waterPrice = res.waterRate
      formData.electricityPrice = res.electricityRate
    }
  } catch (e) {
    // ignore
  }
}

const onSubmit = () => {
  if (!propertyId.value) {
    uni.showToast({ title: '请先选择房源', icon: 'none' })
    return
  }
  if (!formData.waterPrice && formData.waterPrice !== 0) {
    uni.showToast({ title: '请输入水费单价', icon: 'none' })
    return
  }
  if (!formData.electricityPrice && formData.electricityPrice !== 0) {
    uni.showToast({ title: '请输入电费单价', icon: 'none' })
    return
  }

  uni.showLoading({ title: '保存中...' })
  
  setUtilityConfig(propertyId.value, {
    waterRate: formData.waterPrice,
    electricityRate: formData.electricityPrice
  }).then(() => {
    uni.showToast({ title: '保存成功', icon: 'success' })
  }).catch((err) => {
    uni.showToast({ title: err.message || '保存失败', icon: 'none' })
  }).finally(() => {
    uni.hideLoading()
  })
}

onMounted(() => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1] as any
  const options = currentPage?.options || {}
  
  propertyId.value = options.propertyId || ''
  fetchConfig()
})
</script>

<style scoped lang="scss">
.utility-config-page {
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

.form-item {
  margin-bottom: 24rpx;
  
  .label {
    display: block;
    font-size: 28rpx;
    color: #333;
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
}

.submit-btn {
  position: fixed;
  left: 30rpx;
  right: 30rpx;
  bottom: 40rpx;
  height: 88rpx;
  background: linear-gradient(135deg, #667EEA 0%, #764BA2 100%);
  border-radius: 44rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 20rpx rgba(102, 126, 234, 0.4);
  
  text {
    color: #fff;
    font-size: 32rpx;
    font-weight: 600;
  }
}
</style>