<template>
  <view class="refund-page" v-if="deposit">
    <!-- 押金信息 -->
    <view class="section info-section">
      <view class="info-row">
        <text class="label">房源</text>
        <text class="value">{{ deposit.propertyName }}</text>
      </view>
      <view class="info-row">
        <text class="label">租客</text>
        <text class="value">{{ deposit.tenantName }}</text>
      </view>
      <view class="info-row">
        <text class="label">押金金额</text>
        <text class="value amount">¥{{ deposit.amount }}</text>
      </view>
    </view>

    <!-- 退还信息 -->
    <view class="section">
      <view class="section-title">退还信息</view>
      
      <view class="form-item">
        <text class="label">实退金额(元)</text>
        <input 
          v-model.number="formData.amount" 
          class="input" 
          type="digit"
          :placeholder="`原押金: ${deposit.amount}元`"
        />
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

    <!-- 提交 -->
    <view class="submit-btn" @click="onSubmit">
      <text>确认退还</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getDepositDetail, refundDeposit } from '@/services/deposit'

const depositId = ref('')
const deposit = ref<any>(null)

const formData = reactive({
  amount: null as number | null,
  remark: ''
})

const fetchDetail = async () => {
  try {
    const res = await getDepositDetail(depositId.value)
    deposit.value = res
    formData.amount = res.amount
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  }
}

const onSubmit = () => {
  if (!formData.amount) {
    uni.showToast({ title: '请输入实退金额', icon: 'none' })
    return
  }

  uni.showLoading({ title: '提交中...' })
  
  refundDeposit(depositId.value, {
    amount: formData.amount,
    remark: formData.remark || undefined
  }).then(() => {
    uni.showToast({ title: '退还成功', icon: 'success' })
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
    depositId.value = options.id
    fetchDetail()
  }
})
</script>

<style scoped lang="scss">
.refund-page {
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
    
    &.amount {
      font-weight: bold;
      color: #0087FF;
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