<template>
  <view class="profile-page">
    <!-- 头像 -->
    <view class="section avatar-section" @click="onChangeAvatar">
      <text class="label">头像</text>
      <view class="avatar-wrap">
        <image 
          class="avatar" 
          :src="formData.avatarUrl || '/static/default-avatar.png'" 
          mode="aspectFill"
        />
        <text class="arrow">›</text>
      </view>
    </view>

    <!-- 基本信息 -->
    <view class="section">
      <view class="section-title">基本信息</view>
      
      <view class="form-item">
        <text class="label">昵称</text>
        <input 
          v-model="formData.nickname" 
          class="input" 
          placeholder="请输入昵称"
        />
      </view>
      
      <view class="form-item">
        <text class="label">手机号</text>
        <input 
          v-model="formData.phone" 
          class="input" 
          type="number"
          placeholder="请输入手机号"
          maxlength="11"
        />
      </view>
      
      <view class="form-item">
        <text class="label">邮箱</text>
        <input 
          v-model="formData.email" 
          class="input" 
          type="text"
          placeholder="请输入邮箱（选填）"
        />
      </view>
    </view>

    <!-- 实名认证 -->
    <view class="section">
      <view class="section-title">实名认证</view>
      
      <view class="form-item">
        <text class="label">真实姓名</text>
        <input 
          v-model="formData.realName" 
          class="input" 
          placeholder="请输入真实姓名"
        />
      </view>
      
      <view class="form-item">
        <text class="label">身份证号</text>
        <input 
          v-model="formData.idCard" 
          class="input" 
          placeholder="请输入身份证号"
          maxlength="18"
        />
      </view>
    </view>

    <!-- 提交 -->
    <view class="submit-btn" @click="onSave">
      <text>保存</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getUserInfo, updateUserInfo } from '@/services/auth'

const formData = reactive({
  nickname: '',
  phone: '',
  email: '',
  avatarUrl: '',
  realName: '',
  idCard: ''
})

const fetchUserInfo = async () => {
  try {
    const res = await getUserInfo()
    if (res) {
      formData.nickname = res.nickname || ''
      formData.phone = res.phone || ''
      formData.email = res.email || ''
      formData.avatarUrl = res.avatarUrl || ''
      formData.realName = res.realName || ''
      formData.idCard = res.idCard || ''
    }
  } catch (e) {
    console.error('fetchUserInfo error:', e)
  }
}

const onChangeAvatar = () => {
  uni.chooseImage({
    count: 1,
    sizeType: ['compressed'],
    sourceType: ['album', 'camera'],
    success: (res) => {
      // TODO: 上传头像到服务器
      formData.avatarUrl = res.tempFilePaths[0]
      uni.showToast({ title: '头像已选择', icon: 'success' })
    }
  })
}

const onSave = () => {
  if (!formData.nickname) {
    uni.showToast({ title: '请输入昵称', icon: 'none' })
    return
  }
  if (!formData.phone) {
    uni.showToast({ title: '请输入手机号', icon: 'none' })
    return
  }
  if (!/^1\d{10}$/.test(formData.phone)) {
    uni.showToast({ title: '手机号格式不正确', icon: 'none' })
    return
  }

  uni.showLoading({ title: '保存中...' })
  
  updateUserInfo({
    nickname: formData.nickname,
    phone: formData.phone,
    email: formData.email,
    avatarUrl: formData.avatarUrl,
    realName: formData.realName,
    idCard: formData.idCard
  })
    .then(() => {
      uni.showToast({ title: '保存成功', icon: 'success' })
      setTimeout(() => {
        uni.navigateBack()
      }, 1500)
    })
    .catch((err) => {
      uni.showToast({ title: err.message || '保存失败', icon: 'none' })
    })
    .finally(() => {
      uni.hideLoading()
    })
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped lang="scss">
.profile-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

.section {
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.avatar-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  .label {
    font-size: 30rpx;
    color: #333;
  }
  
  .avatar-wrap {
    display: flex;
    align-items: center;
    
    .avatar {
      width: 100rpx;
      height: 100rpx;
      border-radius: 50%;
      background-color: #f5f5f5;
    }
    
    .arrow {
      margin-left: 20rpx;
      font-size: 32rpx;
      color: #999;
    }
  }
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 24rpx;
}

.form-item {
  display: flex;
  align-items: center;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
  
  &:last-child {
    border-bottom: none;
  }
  
  .label {
    width: 180rpx;
    font-size: 28rpx;
    color: #666;
  }
  
  .input {
    flex: 1;
    font-size: 28rpx;
    color: #333;
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