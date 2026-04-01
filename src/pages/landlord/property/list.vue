<template>
  <view class="container">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <input 
        class="search-input" 
        v-model="keyword" 
        placeholder="搜索房源名称/地址"
        @confirm="handleSearch"
      />
      <view class="filter-btn" @click="showFilter = !showFilter">
        <text>筛选</text>
      </view>
    </view>

    <!-- 筛选条件 -->
    <view class="filter-bar" v-if="showFilter">
      <view 
        class="filter-item" 
        :class="{ active: filterStatus === 0 }"
        @click="filterStatus = 0"
      >
        全部
      </view>
      <view 
        class="filter-item" 
        :class="{ active: filterStatus === 1 }"
        @click="filterStatus = 1"
      >
        未出租
      </view>
      <view 
        class="filter-item" 
        :class="{ active: filterStatus === 2 }"
        @click="filterStatus = 2"
      >
        已出租
      </view>
    </view>

    <!-- 房源列表 -->
    <scroll-view class="property-list" scroll-y @scrolltolower="loadMore">
      <view 
        class="property-card" 
        v-for="item in propertyList" 
        :key="item.id"
        @click="goToDetail(item.id)"
      >
        <view class="property-header">
          <text class="property-name">{{ item.name }}</text>
          <view class="status-tag" :class="item.status === 0 ? 'available' : 'rented'">
            {{ item.status === 0 ? '未出租' : '已出租' }}
          </view>
        </view>
        <view class="property-info">
          <text class="address">📍 {{ item.address }}</text>
          <text class="layout">🏠 {{ item.layout }}</text>
          <text class="area">📐 {{ item.area }}㎡</text>
        </view>
        <view class="property-footer">
          <text class="rent">¥{{ item.rentAmount }}/月</text>
          <view class="action-btns" v-if="item.status === 0">
            <text class="action-btn" @click.stop="handleEdit(item)">编辑</text>
            <text class="action-btn delete" @click.stop="handleDelete(item.id)">删除</text>
          </view>
        </view>
      </view>

      <!-- 空状态 -->
      <view class="empty-state" v-if="propertyList.length === 0">
        <text class="empty-text">暂无房源</text>
        <text class="empty-hint">点击右下角添加房源</text>
      </view>
    </scroll-view>

    <!-- 添加按钮 -->
    <view class="add-btn" @click="goToAdd">
      <text class="add-icon">+</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getPropertyList, deleteProperty } from '@/services/property'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const keyword = ref('')
const showFilter = ref(false)
const filterStatus = ref<number>(0)
const propertyList = ref<any[]>([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

/**
 * 加载房源列表
 */
async function loadList(append = false) {
  try {
    const status = filterStatus.value === 0 ? undefined : (filterStatus.value - 1)
    const result = await getPropertyList(
      userStore.userInfo?.id || 0,
      status,
      pageNum.value,
      pageSize.value
    )
    
    if (append) {
      propertyList.value = [...propertyList.value, ...result.records]
    } else {
      propertyList.value = result.records
    }
    total.value = result.total
  } catch (error) {
    console.error('加载房源列表失败:', error)
  }
}

/**
 * 加载更多
 */
function loadMore() {
  if (propertyList.value.length < total.value) {
    pageNum.value++
    loadList(true)
  }
}

/**
 * 搜索
 */
function handleSearch() {
  pageNum.value = 1
  loadList()
}

/**
 * 添加房源
 */
function goToAdd() {
  uni.navigateTo({ url: '/pages/landlord/property/edit' })
}

/**
 * 编辑房源
 */
function handleEdit(item: any) {
  uni.navigateTo({ 
    url: `/pages/landlord/property/edit?id=${item.id}` 
  })
}

/**
 * 删除房源
 */
async function handleDelete(id: number) {
  uni.showModal({
    title: '确认删除',
    content: '确定要删除该房源吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          await deleteProperty(id)
          uni.showToast({ title: '删除成功', icon: 'success' })
          loadList()
        } catch (error) {
          console.error('删除失败:', error)
        }
      }
    }
  })
}

/**
 * 查看详情
 */
function goToDetail(id: number) {
  // TODO: 跳转到详情页
  uni.showToast({ title: '详情页开发中', icon: 'none' })
}

onMounted(() => {
  loadList()
})
</script>

<style lang="scss" scoped>
.container {
  padding: 24rpx;
  padding-bottom: 120rpx;
  background: #f5f5f5;
  min-height: 100vh;
}

.search-bar {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 12rpx;
  padding: 16rpx 20rpx;
  margin-bottom: 20rpx;

  .search-input {
    flex: 1;
    height: 60rpx;
    background: #f5f5f5;
    border-radius: 8rpx;
    padding: 0 20rpx;
    font-size: 26rpx;
  }

  .filter-btn {
    margin-left: 20rpx;
    padding: 12rpx 24rpx;
    background: #667eea;
    color: #fff;
    border-radius: 8rpx;
    font-size: 24rpx;
  }
}

.filter-bar {
  display: flex;
  background: #fff;
  border-radius: 12rpx;
  padding: 16rpx 20rpx;
  margin-bottom: 20rpx;

  .filter-item {
    padding: 8rpx 24rpx;
    margin-right: 16rpx;
    background: #f5f5f5;
    border-radius: 8rpx;
    font-size: 24rpx;
    color: #666;

    &.active {
      background: #667eea;
      color: #fff;
    }
  }
}

.property-list {
  height: calc(100vh - 300rpx);

  .property-card {
    background: #fff;
    border-radius: 12rpx;
    padding: 24rpx;
    margin-bottom: 20rpx;

    .property-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16rpx;

      .property-name {
        font-size: 30rpx;
        font-weight: bold;
        color: #333;
      }

      .status-tag {
        padding: 6rpx 16rpx;
        border-radius: 8rpx;
        font-size: 22rpx;

        &.available {
          background: #e8f5e9;
          color: #4caf50;
        }

        &.rented {
          background: #ffebee;
          color: #f44336;
        }
      }
    }

    .property-info {
      display: flex;
      flex-wrap: wrap;
      margin-bottom: 16rpx;

      text {
        margin-right: 24rpx;
        font-size: 24rpx;
        color: #666;
      }
    }

    .property-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .rent {
        font-size: 32rpx;
        font-weight: bold;
        color: #f44336;
      }

      .action-btns {
        display: flex;

        .action-btn {
          padding: 8rpx 20rpx;
          margin-left: 12rpx;
          background: #667eea;
          color: #fff;
          border-radius: 8rpx;
          font-size: 22rpx;

          &.delete {
            background: #f44336;
          }
        }
      }
    }
  }
}

.empty-state {
  text-align: center;
  padding: 100rpx 0;

  .empty-text {
    display: block;
    font-size: 28rpx;
    color: #999;
    margin-bottom: 12rpx;
  }

  .empty-hint {
    display: block;
    font-size: 24rpx;
    color: #ccc;
  }
}

.add-btn {
  position: fixed;
  right: 40rpx;
  bottom: 100rpx;
  width: 100rpx;
  height: 100rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 20rpx rgba(102, 126, 234, 0.4);

  .add-icon {
    font-size: 64rpx;
    color: #fff;
    line-height: 1;
  }
}
</style>
