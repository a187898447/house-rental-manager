<template>
  <view class="property-detail-page">
    <!-- 图片轮播 -->
    <swiper class="image-swiper" indicator-dots circular autoplay>
      <swiper-item v-for="(img, index) in images" :key="index">
        <image :src="img" mode="aspectFill" class="swiper-image" />
      </swiper-item>
      <swiper-item v-if="images.length === 0">
        <view class="empty-image">
          <text>暂无图片</text>
        </view>
      </swiper-item>
    </swiper>

    <!-- 房源基本信息 -->
    <view class="info-section">
      <view class="price-row">
        <text class="price">{{ property?.rent }}</text>
        <text class="unit">元/月</text>
        <text class="status" :class="property?.status">{{ statusText }}</text>
      </view>
      <view class="title-row">
        <text class="title">{{ property?.building }} {{ property?.unit }} {{ property?.roomNumber }}</text>
      </view>
      <view class="tags-row">
        <view class="tag" v-if="property?.area">{{ property?.area }}㎡</view>
        <view class="tag" v-if="property?.floor">第{{ property?.floor }}层</view>
        <view class="tag" v-if="property?.orientation">{{ property?.orientation }}</view>
      </view>
    </view>

    <!-- 房源详情 -->
    <view class="detail-section">
      <text class="section-title">房源详情</text>
      <view class="detail-grid">
        <view class="detail-item">
          <text class="label">楼栋</text>
          <text class="value">{{ property?.building || '-' }}</text>
        </view>
        <view class="detail-item">
          <text class="label">单元</text>
          <text class="value">{{ property?.unit || '-' }}</text>
        </view>
        <view class="detail-item">
          <text class="label">房间号</text>
          <text class="value">{{ property?.roomNumber || '-' }}</text>
        </view>
        <view class="detail-item">
          <text class="label">面积</text>
          <text class="value">{{ property?.area ? property.area + '㎡' : '-' }}</text>
        </view>
        <view class="detail-item">
          <text class="label">户型</text>
          <text class="value">{{ property?.layout || '-' }}</text>
        </view>
        <view class="detail-item">
          <text class="label">楼层</text>
          <text class="value">{{ property?.floor ? '第' + property.floor + '层' : '-' }}</text>
        </view>
        <view class="detail-item">
          <text class="label">朝向</text>
          <text class="value">{{ property?.orientation || '-' }}</text>
        </view>
        <view class="detail-item">
          <text class="label">装修</text>
          <text class="value">{{ property?.decoration || '-' }}</text>
        </view>
      </view>
    </view>

    <!-- 配套设施 -->
    <view class="facility-section" v-if="facilities && facilities.length > 0">
      <text class="section-title">配套设施</text>
      <view class="facility-grid">
        <view class="facility-item" v-for="item in facilities" :key="item">
          <text class="facility-text">{{ item }}</text>
        </view>
      </view>
    </view>

    <!-- 房源描述 -->
    <view class="desc-section" v-if="property?.description">
      <text class="section-title">房源描述</text>
      <text class="description">{{ property?.description }}</text>
    </view>

    <!-- 底部操作栏 -->
    <view class="bottom-bar">
      <view class="action-btn contact" @click="handleContact">
        <text>联系房东</text>
      </view>
      <view class="action-btn reserve" @click="handleReserve" v-if="property?.status === 'vacant'">
        <text>预约看房</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { getPropertyDetail } from '@/services/property'

interface PropertyDetail {
  id: string
  building: string
  unit: string
  roomNumber: string
  rent: number
  area?: number
  floor?: string
  orientation?: string
  layout?: string
  decoration?: string
  status: 'vacant' | 'rented' | 'reserved'
  description?: string
  images: string[]
  facilities?: string[]
}

const property = ref<PropertyDetail | null>(null)
const loading = ref(false)

const images = computed(() => property.value?.images || [])

const statusText = computed(() => {
  const statusMap: Record<string, string> = {
    vacant: '可出租',
    rented: '已出租',
    reserved: '已预约'
  }
  return statusMap[property.value?.status || 'vacant']
})

const facilities = computed(() => property.value?.facilities || [])

// 获取房源详情
const fetchDetail = async () => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1] as any
  const id = currentPage?.options?.id

  if (!id) {
    uni.showToast({ title: '参数错误', icon: 'none' })
    return
  }

  loading.value = true
  try {
    const res = await getPropertyDetail(id)
    property.value = res
  } catch (error) {
    uni.showToast({ title: '获取详情失败', icon: 'none' })
  } finally {
    loading.value = false
  }
}

// 联系房东
const handleContact = () => {
  if (property.value?.phone) {
    uni.makePhoneCall({
      phoneNumber: property.value.phone
    })
  } else {
    uni.showToast({ title: '暂无联系方式', icon: 'none' })
  }
}

// 预约看房
const handleReserve = () => {
  uni.showModal({
    title: '预约看房',
    content: '确定要预约看房吗？',
    success: (res) => {
      if (res.confirm) {
        uni.showToast({ title: '预约成功', icon: 'success' })
      }
    }
  })
}

onMounted(() => {
  fetchDetail()
})
</script>

<style scoped lang="scss">
.property-detail-page {
  padding-bottom: 120rpx;
  background-color: #f5f5f5;
}

.image-swiper {
  width: 100%;
  height: 500rpx;

  .swiper-image {
    width: 100%;
    height: 100%;
  }

  .empty-image {
    width: 100%;
    height: 100%;
    background-color: #eee;
    display: flex;
    align-items: center;
    justify-content: center;

    text {
      color: #999;
      font-size: 28rpx;
    }
  }
}

.info-section {
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;

  .price-row {
    display: flex;
    align-items: baseline;
    margin-bottom: 16rpx;

    .price {
      font-size: 44rpx;
      font-weight: bold;
      color: #ff4d4f;
    }

    .unit {
      font-size: 28rpx;
      color: #666;
      margin-left: 8rpx;
    }

    .status {
      margin-left: auto;
      padding: 8rpx 16rpx;
      border-radius: 4rpx;
      font-size: 24rpx;

      &.vacant {
        background-color: #e6f7ff;
        color: #1890ff;
      }

      &.rented {
        background-color: #fff1f0;
        color: #ff4d4f;
      }

      &.reserved {
        background-color: #fffbe6;
        color: #faad14;
      }
    }
  }

  .title-row {
    margin-bottom: 16rpx;

    .title {
      font-size: 32rpx;
      color: #333;
      font-weight: 500;
    }
  }

  .tags-row {
    display: flex;
    gap: 16rpx;

    .tag {
      padding: 8rpx 16rpx;
      background-color: #f5f5f5;
      border-radius: 4rpx;
      font-size: 24rpx;
      color: #666;
    }
  }
}

.detail-section,
.facility-section,
.desc-section {
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;

  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    display: block;
    margin-bottom: 24rpx;
  }
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24rpx;

  .detail-item {
    display: flex;
    justify-content: space-between;

    .label {
      font-size: 28rpx;
      color: #999;
    }

    .value {
      font-size: 28rpx;
      color: #333;
    }
  }
}

.facility-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;

  .facility-item {
    padding: 12rpx 24rpx;
    background-color: #f5f5f5;
    border-radius: 4rpx;

    .facility-text {
      font-size: 26rpx;
      color: #666;
    }
  }
}

.description {
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  height: 100rpx;
  background-color: #fff;
  border-top: 1rpx solid #eee;

  .action-btn {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 30rpx;

    &.contact {
      background-color: #fff;
      color: #333;
    }

    &.reserve {
      background-color: #0087FF;
      color: #fff;
    }
  }
}
</style>
