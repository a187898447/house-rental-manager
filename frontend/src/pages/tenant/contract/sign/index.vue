<template>
  <view class="sign-page">
    <view class="sign-container">
      <view class="sign-title">请在下方签名</view>
      <canvas 
        class="sign-canvas" 
        canvas-id="signCanvas"
        @touchstart="touchStart"
        @touchmove="touchMove"
        @touchend="touchEnd"
      ></canvas>
      <view class="sign-tools">
        <button class="btn-clear" @click="clearCanvas">清除</button>
        <button class="btn-submit" @click="submitSign">确认提交</button>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { signContract } from '@/services/contract'

const contractId = ref('')
const ctx = ref<any>(null)
const isDrawing = ref(false)
const canvasWidth = ref(300)
const canvasHeight = ref(200)

onLoad((options: any) => {
  contractId.value = options.id || ''
  initCanvas()
})

const initCanvas = () => {
  const systemInfo = uni.getSystemInfoSync()
  canvasWidth.value = systemInfo.windowWidth - 64
  canvasHeight.value = 200
  
  const query = uni.createSelectorQuery()
  query.select('.sign-canvas')
    .fields({ node: true, size: true })
    .exec((res) => {
      const canvas = res[0].node
      ctx.value = canvas.getContext('2d')
      const dpr = uni.getSystemInfoSync().pixelRatio
      canvas.width = canvasWidth.value * dpr
      canvas.height = canvasHeight.value * dpr
      ctx.value.scale(dpr, dpr)
      ctx.value.strokeStyle = '#333'
      ctx.value.lineWidth = 3
      ctx.value.lineCap = 'round'
      ctx.value.lineJoin = 'round'
    })
}

const touchStart = (e: any) => {
  isDrawing.value = true
  ctx.value.beginPath()
  ctx.value.moveTo(e.touches[0].x, e.touches[0].y)
}

const touchMove = (e: any) => {
  if (!isDrawing.value) return
  ctx.value.lineTo(e.touches[0].x, e.touches[0].y)
  ctx.value.stroke()
}

const touchEnd = () => {
  isDrawing.value = false
}

const clearCanvas = () => {
  if (!ctx.value) return
  ctx.value.clearRect(0, 0, canvasWidth.value, canvasHeight.value)
}

const submitSign = async () => {
  if (!ctx.value) return
  
  // 获取 Canvas 内容转为图片
  const query = uni.createSelectorQuery()
  query.select('.sign-canvas')
    .fields({ node: true, size: true })
    .exec((res) => {
      const canvas = res[0].node
      uni.canvasToTempFilePath({
        canvas: canvas,
        success: async (res) => {
          // 上传签名图片
          const tempFilePath = res.tempFilePath
          uni.uploadFile({
            url: '/api/upload/signature',
            filePath: tempFilePath,
            name: 'file',
            success: async (uploadRes) => {
              const data = JSON.parse(uploadRes.data)
              if (data.url) {
                // 调用合同签署接口
                await signContract(contractId.value, data.url)
                uni.showToast({ title: '签署成功', icon: 'success' })
                setTimeout(() => {
                  uni.navigateBack()
                }, 1500)
              }
            }
          })
        }
      })
    })
}
</script>

<style scoped>
.sign-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 32rpx;
}

.sign-container {
  background: #fff;
  border-radius: 16rpx;
  padding: 32rpx;
}

.sign-title {
  font-size: 32rpx;
  color: #333;
  margin-bottom: 24rpx;
  text-align: center;
}

.sign-canvas {
  width: 100%;
  height: 400rpx;
  background: #fafafa;
  border: 2rpx dashed #ddd;
  border-radius: 8rpx;
}

.sign-tools {
  display: flex;
  justify-content: space-between;
  margin-top: 32rpx;
}

.btn-clear {
  flex: 1;
  margin-right: 16rpx;
  background: #fff;
  border: 2rpx solid #ddd;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.btn-submit {
  flex: 1;
  margin-left: 16rpx;
  background: #007AFF;
  color: #fff;
  border-radius: 8rpx;
  font-size: 28rpx;
}
</style>
