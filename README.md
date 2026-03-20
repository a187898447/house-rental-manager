# house-rental-manager

租房小程序 - 房东端 + 租客端

## 技术栈

- uni-app + Vue 3 + TypeScript
- UI 组件：uview-plus
- 状态管理：Pinia

## 项目结构

```
src/
├── pages/           # 页面
│   ├── landlord/   # 房东端
│   └── tenant/     # 租客端
├── components/     # 公共组件
├── composables/    # 组合式函数
├── stores/         # Pinia 状态管理
├── services/       # API 服务层
├── utils/          # 工具函数
├── types/          # TypeScript 类型
└── static/         # 静态资源
```

## 开发

```bash
# 安装依赖
npm install

# 开发微信小程序
npm run dev:mp-weixin

# 构建微信小程序
npm run build:mp-weixin
```
