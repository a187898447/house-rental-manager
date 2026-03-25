<template>
  <view class="checkin-page">
    <!-- 房源选择 -->
    <view class="section">
      <view class="section-title">选择房源</view>
      <view class="property-select" @click="showPropertyPicker = true">
        <text :class="formData.propertyId ? '' : 'placeholder'">
          {{ selectedPropertyName || '请选择房源' }}
        </text>
        <text class="arrow">></text>
      </view>
    </view>

    <!-- 租客信息 -->
    <view class="section">
      <view class="section-title">租客信息</view>
      
      <view class="form-item">
        <text class="label">姓名</text>
        <input v-model="formData.name" class="input" placeholder="请输入租客姓名" />
      </view>
      
      <view class="form-item">
        <text class="label">手机号</text>
        <input v-model="formData.phone" class="input" placeholder="请输入手机号" type="number" maxlength="11" />
      </view>
      
      <view class="form-item">
        <text class="label">身份证号</text>
        <input v-model="formData.idCard" class="input" placeholder="请输入身份证号(选填)" />
      </view>
    </view>

    <!-- 租约信息 -->
    <view class="section">
      <view class="section-title">租约信息</view>
      
      <view class="form-item">
        <text class="label">开始日期</text>
        <view class="date-picker" @click="showStartDate = true">
          <text :class="formData.leaseStart ? '' : 'placeholder'">
            {{ formData.leaseStart || '请选择开始日期' }}
          </text>
        </view>
      </view>
      
      <view class="form-item">
        <text class="label">结束日期</text>
        <view class="date-picker" @click="showEndDate = true">
          <text :class="formData.leaseEnd ? '' : 'placeholder'">
            {{ formData.leaseEnd || '请选择结束日期' }}
          </text>
        </view>
      </view>
    </view>

    <!-- 紧急联系人 -->
    <view class="section">
      <view class="section-title">紧急联系人(选填)</view>
      
      <view class="form-item">
        <text class="label">联系人</text>
        <input v-model="formData.emergencyContact" class="input" placeholder="请输入联系人姓名" />
      </view>
      
      <view class="form-item">
        <text class="label">联系电话</text>
        <input v-model="formData.emergencyPhone" class="input" placeholder="请输入联系电话" type="number" />
      </view>
    </view>

    <!-- 提交 -->
    <view class="submit-btn" @click="onSubmit">
      <text>确认入住</text>
    </view>

    <!-- 房源选择器 -->
    <up-picker 
      v-if="showPropertyPicker" 
      :show="showPropertyPicker" 
      :columns="[properties]" 
      key-name="name"
      @confirm="onPropertyConfirm"
      @cancel="showPropertyPicker = false"
    />

    <!-- 日期选择器 -->
    <up-datetime-picker
      v-if="showStartDate"
      :show="showStartDate"
      mode="date"
      title="选择开始日期"
      @confirm="onStartDateConfirm"
      @cancel="showStartDate = false"
    />
    <up-datetime-picker
      v-if="showEndDate"
      :show="showEndDate"
      mode="date"
      title="选择结束日期"
      @confirm="onEndDateConfirm"
      @cancel="showEndDate = false"
    />
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { checkIn } from '@/services/tenant'
import { getProperties } from '@/services/property'

const formData = reactive({
  propertyId: '',
  name: '',
  phone: '',
  idCard: '',
  leaseStart: '',
  leaseEnd: '',
  emergencyContact: '',
  emergencyPhone: ''
})

const properties = ref<any[]>([])
const selectedPropertyName = ref('')
const showPropertyPicker = ref(false)
const showStartDate = ref(false)
const showEndDate = ref(false)

const fetchProperties = async () => {
  try {
    const res = await getProperties({ size: 100 })
    properties.value = res?.list || []
  } catch (e) {
    // ignore
  }
}

const onPropertyConfirm = (e: any) => {
  const selected = e.value[0]
  if (selected) {
    formData.propertyId = String(selected.id)
    selectedPropertyName.value = selected.name
  }
  showPropertyPicker.value = false
}

const onStartDateConfirm = (e: any) => {
  const date = new Date(e.value)
  formData.leaseStart = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
  showStartDate.value = false
}

const onEndDateConfirm = (e: any) => {
  const date = new Date(e.value)
  formData.leaseEnd = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
  showEndDate.value = false
}

const onSubmit = () => {
  if (!formData.propertyId) {
    uni.showToast({ title: '请选择房源', icon: 'none' })
    return
  }
  if (!formData.name) {
    uni.showToast({ title: '请输入姓名', icon: 'none' })
    return
  }
  if (!formData.phone) {
    uni.showToast({ title: '请输入手机号', icon: 'none' })
    return
  }
  if (!formData.leaseStart || !formData.leaseEnd) {
    uni.showToast({ title: '请选择租约日期', icon: 'none' })
    return
  }

  uni.showLoading({ title: '提交中...' })
  
  checkIn({
    propertyId: formData.propertyId,
    name: formData.name,
    phone: formData.phone,
    idCard: formData.idCard || undefined,
    leaseStart: formData.leaseStart,
    leaseEnd: formData.leaseEnd,
    emergencyContact: formData.emergencyContact || undefined,
    emergencyPhone: formData.emergencyPhone || undefined
  }).then(() => {
    uni.showToast({ title: '入住成功', icon: 'success' })
    setTimeout(() => uni.navigateBack(), 1500)
  }).catch((err) => {
    uni.showToast({ title: err.message || '提交失败', icon: 'none' })
  }).finally(() => {
    uni.hideLoading()
  })
}

onMounted(() => {
  fetchProperties()
})
</script>

<style scoped lang="scss">
.checkin-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

.section {
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
  
  .section-title {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 24rpx;
  }
}

.property-select {
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
    color: #999;
  }
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
    height: 80rpx;
    padding: 0 20rpx;
    background-color: #f5f5f5;
    border-radius: 8rpx;
    font-size: 28rpx;
  }
  
  .placeholder {
    color: #999;
  }
}

.date-picker {
  height: 80rpx;
  padding: 0 20rpx;
  background-color: #f5f5f5;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
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