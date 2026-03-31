# 前端开发规范 (Vue 3 + UniApp)

## 代码格式

### 缩进
- 2 空格缩进
- 不使用 Tab

### 文件命名
- 组件: PascalCase
  - 例：UserInfo.vue, PropertyList.vue
- 工具: camelCase
  - 例：auth.ts, request.ts

### Vue 文件结构
```vue
<template>
  <!-- 模板内容 -->
</template>

<script setup lang="ts">
// Script
</script>

<style scoped>
// 样式
</style>
```

## 命名规范

### 变量命名
- camelCase
- 例：userInfo, propertyList

### 常量命名
- UPPER_SNAKE_CASE
- 例：MAX_UPLOAD_SIZE

### Props 命名
- camelCase (defineProps)
- kebab-case (模板中使用)

### 事件命名
- kebab-case
- 例：@handle-click, @update-value

## Vue 3 规范

### 1. 使用 Script Setup
```typescript
// 推荐
<script setup lang="ts">
const count = ref(0)
</script>

// 不推荐
<script lang="ts">
export default {
  data() {
    return { count: 0 }
  }
}
</script>
```

### 2. 类型定义
```typescript
// 使用类型定义
interface UserInfo {
  id: number
  name: string
}

// 使用 ref
const user = ref<UserInfo | null>(null)
```

### 3. 计算属性
```typescript
// 避免副作用
const fullName = computed(() => `${firstName.value} ${lastName.value}`)
```

### 4. 组件 Props
```typescript
// 定义 Props
interface Props {
  title: string
  count?: number
}

const props = withDefaults(defineProps<Props>(), {
  count: 0
})
```

### 5. 组件 Emit
```typescript
// 定义 Emit
const emit = defineEmits<{
  (e: 'update', value: string): void
  (e: 'delete', id: number): void
}>()
```

## UniApp 规范

### 1. 页面路径
- pages/模块名/页面名/index
- 例：pages/landlord/property/list/index.vue

### 2. API 封装
```typescript
// 按模块划分
// services/auth.ts - 认证相关
// services/property.ts - 房源相关
```

### 3. Store 使用
```typescript
// 使用 Pinia
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
```

### 4. 样式单位
- 使用 rpx
- 例：padding: 24rpx

## API 请求规范

### 1. 统一请求封装
```typescript
// 使用统一的 request 方法
import { request } from '@/services/index'

export const getUserInfo = () => {
  return request({
    url: '/api/user/info',
    method: 'GET'
  })
}
```

### 2. 错误处理
```typescript
try {
  const res = await getUserInfo()
  // 处理成功
} catch (error) {
  // 处理错误
  uni.showToast({ title: error.message, icon: 'none' })
}
```

## 样式规范

### 1. 使用 Scoped
```vue
<style scoped>
/* 样式仅作用于当前组件 */
</style>
```

### 2. 类名命名
- BEM 风格 (可选)
- 例：block__element--modifier

### 3. 响应式
- 使用 rpx / upx
- 避免固定宽度

## 性能规范

### 1. 图片优化
- 使用懒加载
- 合适格式 (webp)

### 2. 列表优化
- 使用虚拟列表（长列表）
- key 绑定

### 3. 避免重复请求
- 使用缓存
- 防抖/节流

## TypeScript 规范

### 1. 类型定义
```typescript
// 接口
interface User {
  id: number
  name: string
}

// 类型别名
type Status = 'pending' | 'done'
```

### 2. 严格模式
- 开启 strict: true
- 避免 any

### 3. 泛型使用
```typescript
// 通用函数
function clone<T>(obj: T): T {
  return JSON.parse(JSON.stringify(obj))
}
```

## Git 提交规范

### 提交信息格式
```
type: description

- 具体修改1
- 具体修改2
```

### type 类型
- feat: 新功能
- fix: 修复
- refactor: 重构
- docs: 文档
- style: 样式
- test: 测试