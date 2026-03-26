<template>
  <view class="fee-create-page">
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

    <!-- 租客选择 -->
    <view class="section" v-if="selectedProperty">
      <view class="section-title">选择租客</view>
      <picker 
        :range="tenants" 
        range-key="name"
        @change="onTenantChange"
      >
        <view class="picker">
          <text v-if="selectedTenant">{{ selectedTenant.name }}</text>
          <text v-else class="placeholder">请选择租客</text>
          <text class="arrow">›</text>
        </view>
      </picker>
    </view>

    <!-- 费用类型 -->
    <view class="section">
      <view class="section-title">费用类型</view>
      <view class="fee-types">
        <view 
          v-for="(type, index) in feeTypes" 
          :key="index"
          class="type-item"
          :class="{ active: formData.feeType === type.value }"
          @click="formData.feeType = type.value"
        >
          <text>{{ type.label }}</text>
        </view>
      </view>
    </view>

    <!-- 费用信息 -->
    <view class="section">
      <view class="section-title">费用信息</view>
      <view class="form-item">
        <text class="label">金额（元）</text>
        <input 
          v-model.number="formData.amount" 
          class="input" 
          type="digit"
          placeholder="请输入金额"
        />
      </view>
      <view class="form-item">
        <text class="label">账单月份</text>
        <picker 
          mode="date" 
          fields="month" 
          :value="formData.month"
          @change="onMonthChange"
        >
          <view class="picker">
            <text>{{ formData.month || '请选择月份' }}</text>
            <text class="arrow">›</text>
          </view>
        </picker>
      </view>
      <view class="form-item">
        <text class="label">备注（选填）</text>
        <textarea 
          v-model="formData.remark" 
          class="textarea" 
          placeholder="请输入备注"
          auto-height
        />
      </view>
    </view>

    <!-- 提交 -->
    <view class="submit-btn" @click="onSubmit">
      <text>创建费用</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getProperties } from '@/services/property'
import { getTenantsByProperty } from '@/services/tenant'
import { createOtherFee } from '@/services/otherfee'

const properties = ref<any[]>([])
const selectedProperty = ref<any>(null)
const tenants = ref<any[]>([])
const selectedTenant = ref<any>(null)

const feeTypes = [
  { label: '物业费', value: 'property' },
  { label: '垃圾清运费', value: 'garbage' },
  { label: '网络费', value: 'network' },
  { label: '电视费', value: 'tv' },
  { label: '车位费', value: 'parking' },
  { label: '其他', value: 'other' }
]

const formData = reactive({
  propertyId: null as number | null,
  tenantId: null as number | null,
  feeType: '',
  amount: null as number | null,
  month: '',
  remark: ''
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
  selectedTenant.value = null
  tenants.value = []
  
  // 获取该房源的租客
  try {
    const res = await getTenantsByProperty(String(selectedProperty.value.id))
    tenants.value = res || []
  } catch (e) {
    console.error('getTenantsByProperty error:', e)
  }
}

const onTenantChange = (e: any) => {
  const index = e.detail.value
  selectedTenant.value = tenants.value[index]
  formData.tenantId = selectedTenant.value.id
}

const onMonthChange = (e: any) => {
  formData.month = e.detail.value
}

const onSubmit = () => {
  if (!formData.propertyId) {
    uni.showToast({ title: '请选择房源', icon: 'none' })
    return
  }
  if (!formData.tenantId) {
    uni.showToast({ title: '请选择租客', icon: 'none' })
    return
  }
  if (!formData.feeType) {
    uni.showToast({ title: '请选择费用类型', icon: 'none' })
    return
  }
  if (!formData.amount) {
    uni.showToast({ title: '请输入金额', icon: 'none' })
    return
  }
  if (!formData.month) {
    uni.showToast({ title: '请选择月份', icon: 'none' })
    return
  }

  uni.showLoading({ title: '提交中...' })
  
  createOtherFee({
    tenantId: formData.tenantId,
    propertyId: formData.propertyId,
    feeType: formData.feeType,
    amount: formData.amount,
    month: formData.month,
    remark: formData.remark
  })
    .then(() => {
      uni.showToast({ title: '创建成功', icon: 'success' })
      setTimeout(() => {
        uni.navigateBack()
      }, 1500)
    })
    .catch((err) => {
      uni.showToast({ title: err.message || '创建失败', icon: 'none' })
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
.fee-create-page {
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

.fee-types {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  
  .type-item {
    padding: 16rpx 32rpx;
    background-color: #f5f5f5;
    border-radius: 8rpx;
    font-size: 28rpx;
    color: #666;
    
    &.active {
      background-color: #e6f7ff;
      color: #0087FF;
      border: 1rpx solid #0087FF;
    }
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
  
  .textarea {
    width: 100%;
    min-height: 160rpx;
    padding: 20rpx;
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