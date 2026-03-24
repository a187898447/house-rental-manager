<template>
  <view class="checkout-page">
    <!-- 租客信息 -->
    <view class="section tenant-info">
      <view class="info-row">
        <text class="label">租客姓名</text>
        <text class="value">{{ tenant?.name }}</text>
      </view>
      <view class="info-row">
        <text class="label">手机号</text>
        <text class="value">{{ tenant?.phone }}</text>
      </view>
      <view class="info-row">
        <text class="label">房源</text>
        <text class="value">{{ tenant?.propertyName }}</text>
      </view>
      <view class="info-row">
        <text class="label">租约期</text>
        <text class="value">{{ tenant?.leaseStartDate }} ~ {{ tenant?.leaseEndDate }}</text>
      </view>
    </view>

    <!-- 退租信息 -->
    <view class="section">
      <view class="section-title">退租信息</view>
      
      <view class="form-item">
        <text class="label">退租日期</text>
        <view class="date-picker" @click="showDatePicker = true">
          <text :class="formData.checkoutDate ? '' : 'placeholder'">
            {{ formData.checkoutDate || '请选择退租日期' }}
          </text>
        </view>
      </view>
      
      <view class="form-item">
        <text class="label">备注</text>
        <textarea 
          v-model="formData.remark" 
          class="textarea" 
          placeholder="请输入备注(选填)"
          auto-height
        />
      </view>
    </view>

    <!-- 押金信息 -->
    <view class="section">
      <view class="section-title">押金退还</view>
      
      <view class="form-item">
        <text class="label">实退金额(元)</text>
        <input 
          v-model.number="formData.refundAmount" 
          class="input" 
          type="digit"
          :placeholder="`原押金: ${tenant?.depositAmount || 0}元`"
        />
      </view>
      
      <view class="tip">
        押金原金额: {{ tenant?.depositAmount || 0 }} 元
      </view>
    </view>

    <!-- 提交 -->
    <view class="submit-btn" @click="onSubmit">
      <text>确认退租</text>
    </view>

    <!-- 日期选择器 -->
    <u-datetime-picker
      v-if="showDatePicker"
      :show="showDatePicker"
      mode="date"
      title="选择退租日期"
      @confirm="onDateConfirm"
      @cancel="showDatePicker = false"
    />
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getTenantDetail, checkOut } from '@/services/tenant'

const tenantId = ref('')
const tenant = ref<any>(null)
const showDatePicker = ref(false)

const formData = reactive({
  checkoutDate: '',
  remark: '',
  refundAmount: null as number | null
})

const fetchTenantDetail = async () => {
  try {
    const res = await getTenantDetail(tenantId.value)
    tenant.value = res
    formData.refundAmount = res.depositAmount
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  }
}

const onDateConfirm = (e: any) => {
  const date = new Date(e.value)
  formData.checkoutDate = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
  showDatePicker.value = false
}

const onSubmit = () => {
  if (!formData.checkoutDate) {
    uni.showToast({ title: '请选择退租日期', icon: 'none' })
    return
  }

  uni.showLoading({ title: '提交中...' })
  
  checkOut(tenantId.value, {
    checkoutDate: formData.checkoutDate,
    remark: formData.remark,
    refundAmount: formData.refundAmount || undefined
  }).then(() => {
    uni.showToast({ title: '退租成功', icon: 'success' })
    setTimeout(() => uni.navigateBack(), 1500)
  }).catch((err) => {
    uni.showToast({ title: err.message || '提交失败', icon: 'none' })
  }).finally(() => {
    uni.hideLoading()
  })
}

onMounted(() => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1] as any
  const options = currentPage?.options || {}
  
  if (options.id) {
    tenantId.value = options.id
    fetchTenantDetail()
  }
})
</script>

<style scoped lang="scss">
.checkout-page {
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

.tenant-info {
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
}

.form-item {
  margin-bottom: 24rpx;
  
  .label {
    display: block;
    font-size: 28rpx;
    color: #333;
    margin-bottom: 12rpx;
  }
  
  .input, .textarea {
    width: 100%;
    padding: 20rpx;
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

.tip {
  font-size: 24rpx;
  color: #999;
  margin-top: 16rpx;
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