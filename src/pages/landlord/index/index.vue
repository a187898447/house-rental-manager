<template>
  <view class="container">
    <!-- 顶部统计 -->
    <view class="stats-card">
      <view class="stat-item">
        <text class="stat-value">{{ stats.propertyCount }}</text>
        <text class="stat-label">房源总数</text>
      </view>
      <view class="stat-item">
        <text class="stat-value">{{ stats.tenantCount }}</text>
        <text class="stat-label">在租房客</text>
      </view>
      <view class="stat-item">
        <text class="stat-value">¥{{ stats.monthlyIncome }}</text>
        <text class="stat-label">本月收入</text>
      </view>
      <view class="stat-item">
        <text class="stat-value">{{ stats.unpaidCount }}</text>
        <text class="stat-label">待收租</text>
      </view>
    </view>

    <!-- 功能入口 -->
    <view class="menu-grid">
      <view class="menu-item" @click="goTo('/pages/landlord/property/list')">
        <view class="menu-icon">🏠</view>
        <text class="menu-text">房源管理</text>
      </view>
      <view class="menu-item" @click="goTo('/pages/landlord/tenant/list')">
        <view class="menu-icon">👤</view>
        <text class="menu-text">租客管理</text>
      </view>
      <view class="menu-item" @click="goTo('/pages/landlord/rent/list')">
        <view class="menu-icon">💰</view>
        <text class="menu-text">租金管理</text>
      </view>
      <view class="menu-item" @click="goTo('/pages/tenant/bill/index')">
        <view class="menu-icon">📊</view>
        <text class="menu-text">数据统计</text>
      </view>
    </view>

    <!-- 待办提醒 -->
    <view class="todo-section">
      <view class="section-header">
        <text class="section-title">待办提醒</text>
        <text class="section-more">查看全部 ></text>
      </view>
      <view class="todo-list">
        <view class="todo-item" v-if="todos.dueToday > 0">
          <text class="todo-icon">📅</text>
          <text class="todo-text">今日待收租 {{ todos.dueToday }} 笔</text>
        </view>
        <view class="todo-item" v-if="todos.overdue2 > 0">
          <text class="todo-icon">⚠️</text>
          <text class="todo-text">逾期 2 天未交租 {{ todos.overdue2 }} 笔</text>
        </view>
        <view class="todo-item" v-if="todos.overdue3 > 0">
          <text class="todo-icon">❗</text>
          <text class="todo-text">逾期 3 天未交租 {{ todos.overdue3 }} 笔</text>
        </view>
        <view class="todo-empty" v-if="!hasTodos">
          <text class="empty-text">暂无待办事项 👍</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const stats = ref({
  propertyCount: 0,
  tenantCount: 0,
  monthlyIncome: 0,
  unpaidCount: 0,
})

const todos = ref({
  dueToday: 0,
  overdue2: 0,
  overdue3: 0,
})

const hasTodos = computed(() => {
  return todos.value.dueToday > 0 || todos.value.overdue2 > 0 || todos.value.overdue3 > 0
})

/**
 * 加载统计数据
 */
function loadStats() {
  // TODO: 调用后端 API 获取统计数据
  stats.value = {
    propertyCount: 12,
    tenantCount: 8,
    monthlyIncome: 15600,
    unpaidCount: 3,
  }
  todos.value = {
    dueToday: 2,
    overdue2: 1,
    overdue3: 0,
  }
}

/**
 * 页面跳转
 */
function goTo(url: string) {
  uni.navigateTo({ url })
}

// 页面加载时初始化
loadStats()
</script>

<style lang="scss" scoped>
.container {
  padding: 24rpx;
  background: #f5f5f5;
  min-height: 100vh;
}

.stats-card {
  display: flex;
  justify-content: space-between;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16rpx;
  padding: 32rpx 20rpx;
  margin-bottom: 24rpx;

  .stat-item {
    text-align: center;

    .stat-value {
      display: block;
      font-size: 36rpx;
      font-weight: bold;
      color: #fff;
      margin-bottom: 8rpx;
    }

    .stat-label {
      display: block;
      font-size: 22rpx;
      color: rgba(255, 255, 255, 0.8);
    }
  }
}

.menu-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
  background: #fff;
  border-radius: 16rpx;
  padding: 32rpx 20rpx;
  margin-bottom: 24rpx;

  .menu-item {
    text-align: center;

    .menu-icon {
      font-size: 48rpx;
      margin-bottom: 12rpx;
    }

    .menu-text {
      font-size: 24rpx;
      color: #333;
    }
  }
}

.todo-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;

    .section-title {
      font-size: 30rpx;
      font-weight: bold;
      color: #333;
    }

    .section-more {
      font-size: 24rpx;
      color: #999;
    }
  }

  .todo-list {
    .todo-item {
      display: flex;
      align-items: center;
      padding: 20rpx 0;
      border-bottom: 1rpx solid #f5f5f5;

      &:last-child {
        border-bottom: none;
      }

      .todo-icon {
        font-size: 32rpx;
        margin-right: 16rpx;
      }

      .todo-text {
        font-size: 26rpx;
        color: #333;
      }
    }

    .todo-empty {
      text-align: center;
      padding: 40rpx 0;

      .empty-text {
        font-size: 26rpx;
        color: #999;
      }
    }
  }
}
</style>
