<template>
  <view class="add-property-page">
    <!-- 标题 -->
    <view class="header">
      <text class="title">{{ isEdit ? '编辑房源' : '新增房源' }}</text>
    </view>

    <!-- 表单 -->
    <view class="form">
      <!-- 楼栋选择 -->
      <view class="form-item">
        <text class="label">楼栋</text>
        <input 
          v-model="formData.building" 
          class="input" 
          placeholder="请输入楼栋" 
        />
      </view>

      <!-- 单元选择 -->
      <view class="form-item">
        <text class="label">单元</text>
        <input 
          v-model="formData.unit" 
          class="input" 
          placeholder="请输入单元" 
        />
      </view>

      <!-- 房间号 -->
      <view class="form-item">
        <text class="label">房间号</text>
        <input 
          v-model="formData.roomNumber" 
          class="input" 
          placeholder="请输入房间号" 
        />
      </view>

      <!-- 租金 -->
      <view class="form-item">
        <text class="label">租金(元/月)</text>
        <input 
          v-model.number="formData.rent" 
          type="digit"
          class="input" 
          placeholder="请输入租金" 
        />
      </view>

      <!-- 面积 -->
      <view class="form-item">
        <text class="label">面积(㎡)</text>
        <input 
          v-model.number="formData.area" 
          type="digit"
          class="input" 
          placeholder="请输入面积" 
        />
      </view>

      <!-- 房源照片 -->
      <view class="form-item">
        <text class="label">房源照片</text>
        <view class="image-upload">
          <view 
            v-for="(img, index) in formData.images" 
            :key="index"
            class="image-item"
          >
            <image :src="img" mode="aspectFill" class="preview-img" />
            <view class="delete-btn" @click="removeImage(index)">
              <text>×</text>
            </view>
          </view>
          <view class="add-image" @click="chooseImage">
            <text class="plus">+</text>
            <text class="text">添加照片</text>
          </view>
        </view>
      </view>

      <!-- 备注 -->
      <view class="form-item">
        <text class="label">备注</text>
        <textarea 
          v-model="formData.remark" 
          class="textarea" 
          placeholder="请输入备注(可选)"
          auto-height
        />
      </view>

      <!-- 提交按钮 -->
      <view class="submit-btn" @click="onSubmit">
        <text>{{ isEdit ? '保存修改' : '提交' }}</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { usePropertyStore } from '@/stores/property'
import { getPropertyDetail } from '@/services/property'

const propertyStore = usePropertyStore()

const isEdit = ref(true)
const propertyId = ref('')
const loading = ref(false)

const formData = reactive({
  building: '',
  unit: '',
  roomNumber: '',
  rent: '',
  area: '',
  images: [] as string[],
  remark: ''
})

// 选择图片
const chooseImage = () => {
  uni.chooseImage({
    count: 9 - formData.images.length,
    sizeType: ['compressed'],
    sourceType: ['album', 'camera'],
    success: (res) => {
      formData.images.push(...res.tempFilePaths)
    }
  })
}

// 删除图片
const removeImage = (index: number) => {
  formData.images.splice(index, 1)
}

// 提交
const onSubmit = async () => {
  // 表单验证
  if (!formData.building) {
    uni.showToast({ title: '请输入楼栋', icon: 'none' })
    return
  }
  if (!formData.roomNumber) {
    uni.showToast({ title: '请输入房间号', icon: 'none' })
    return
  }
  if (!formData.rent) {
    uni.showToast({ title: '请输入租金', icon: 'none' })
    return
  }

  uni.showLoading({ title: '提交中...' })

  try {
    if (isEdit.value) {
      await propertyStore.editProperty(propertyId.value, formData)
      uni.showToast({ title: '修改成功', icon: 'success' })
    } else {
      await propertyStore.addProperty(formData)
      uni.showToast({ title: '添加成功', icon: 'success' })
    }
    
    setTimeout(() => {
      uni.navigateBack()
    }, 1500)
  } catch (error) {
    uni.showToast({ title: '提交失败', icon: 'none' })
  } finally {
    uni.hideLoading()
  }
}

onMounted(async () => {
  // 获取页面参数
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1] as any
  const options = currentPage?.options || {}
  
  if (options.id) {
    isEdit.value = true
    propertyId.value = options.id
    
    // 加载房源详情
    loading.value = true
    try {
      const detail = await getPropertyDetail(Number(options.id))
      formData.building = detail.building || ''
      formData.unit = detail.unit || ''
      formData.roomNumber = detail.roomNumber || ''
      formData.rent = detail.rentAmount || detail.rent || ''
      formData.area = detail.area || ''
      formData.images = detail.images || []
      formData.remark = detail.remark || ''
    } catch (e) {
      uni.showToast({ title: '加载失败', icon: 'none' })
    } finally {
      loading.value = false
    }
  }
})
</script>

<style scoped lang="scss">
.add-property-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.header {
  padding: 30rpx;
  background-color: #fff;

  .title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
  }
}

.form {
  padding: 30rpx;
  background-color: #fff;
  margin-top: 20rpx;
}

.form-item {
  margin-bottom: 30rpx;

  .label {
    display: block;
    font-size: 28rpx;
    color: #333;
    margin-bottom: 16rpx;
    font-weight: 500;
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

.image-upload {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;

  .image-item {
    position: relative;
    width: 160rpx;
    height: 160rpx;

    .preview-img {
      width: 100%;
      height: 100%;
      border-radius: 8rpx;
    }

    .delete-btn {
      position: absolute;
      top: -10rpx;
      right: -10rpx;
      width: 40rpx;
      height: 40rpx;
      background-color: #ff4d4f;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;

      text {
        color: #fff;
        font-size: 28rpx;
        line-height: 1;
      }
    }
  }

  .add-image {
    width: 160rpx;
    height: 160rpx;
    background-color: #f5f5f5;
    border-radius: 8rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    border: 2rpx dashed #ddd;

    .plus {
      font-size: 48rpx;
      color: #999;
      line-height: 1;
    }

    .text {
      font-size: 24rpx;
      color: #999;
      margin-top: 8rpx;
    }
  }
}

.submit-btn {
  margin-top: 60rpx;
  height: 88rpx;
  background-color: #0087FF;
  border-radius: 44rpx;
  display: flex;
  align-items: center;
  justify-content: center;

  text {
    color: #fff;
    font-size: 32rpx;
    font-weight: 500;
  }
}
</style>
