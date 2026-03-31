# 前端开发规范 (Vue 3 + UniApp)

## 代码格式

### 缩进
- 2 空格缩进

### 文件命名
- 组件: PascalCase (例：UserInfo.vue)
- 工具: camelCase (例：auth.ts)

## Vue 3 规范

### 1. 使用 Script Setup
```typescript
<script setup lang="ts">
const count = ref(0)
</script>
```

### 2. 类型定义
```typescript
interface User {
  id: number
  name: string
}
```

### 3. Props 定义
```typescript
interface Props {
  title: string
  count?: number
}
const props = withDefaults(defineProps<Props>(), {
  count: 0
})
```

## API 请求规范

### 1. 统一请求封装
```typescript
import { request } from '@/services/index'

export const getUserInfo = () => {
  return request({ url: '/api/user/info', method: 'GET' })
}
```

### 2. 错误处理
```typescript
try {
  const res = await getUserInfo()
} catch (error) {
  uni.showToast({ title: error.message, icon: 'none' })
}
```

## 样式规范

### 1. 使用 Scoped
```vue
<style scoped>
</style>
```

### 2. 单位
- 使用 rpx

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