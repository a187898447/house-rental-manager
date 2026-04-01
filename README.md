# 租房小程序前端

基于 UniApp + Vue 3 + TypeScript 的多端前端应用

## 技术栈

- **框架**: UniApp (Vue 3)
- **语言**: TypeScript 5.3+
- **状态管理**: Pinia 2.3+
- **构建工具**: Vite 5.2+
- **UI**: 原生 UniApp 组件

## 功能模块

### 房东端
- 📊 首页统计（房源数、租客数、收入、待收租）
- 🏠 房源管理（添加、编辑、删除、列表、筛选）
- 👤 租客管理（入住登记、租客列表、退租办理）
- 💰 租金管理（租金记录、收租提醒、催租提醒 D+0/D+2/D+3）
- 🔑 押金管理（收取、退还、记录）
- 💧 水电管理（单价配置、抄表录入、账单生成）
- 📝 其他费用（自定义收费）
- 📊 数据统计（收入报表）

### 租客端
- 📱 租客首页
- 📄 账单查看（租金、水电、其他费用）
- 💳 在线缴费（微信支付）
- 📝 电子合同（查看、签署）
- ✍️ Canvas 手写签名
- 🔧 报修申请（拍照、描述）
- 📞 联系房东

## 项目结构

```
house-rental-manager-frontend/
├── src/
│   ├── pages/              # 页面
│   │   ├── index/          # 首页
│   │   ├── login/          # 登录页
│   │   ├── landlord/       # 房东端页面
│   │   └── tenant/         # 租客端页面
│   ├── services/           # API 服务
│   │   ├── auth.ts         # 认证服务
│   │   ├── property.ts     # 房源服务
│   │   ├── tenant.ts       # 租客服务
│   │   ├── rent.ts         # 租金服务
│   │   ├── deposit.ts      # 押金服务
│   │   ├── utility.ts      # 水电服务
│   │   ├── repair.ts       # 报修服务
│   │   └── contract.ts     # 合同服务
│   ├── stores/             # Pinia Store
│   │   ├── user.ts         # 用户状态
│   │   └── property.ts     # 房源状态
│   ├── types/              # TypeScript 类型定义
│   ├── App.vue             # 应用入口
│   ├── main.ts             # 入口文件
│   ├── pages.json          # 页面配置
│   └── uni.scss            # 全局样式变量
├── package.json
├── vite.config.ts
└── tsconfig.json
```

## 快速开始

### 安装依赖

```bash
npm install
```

### 开发模式

```bash
# 微信小程序
npm run dev:mp-weixin

# H5
npm run dev:h5
```

### 构建

```bash
# 微信小程序
npm run build:mp-weixin

# H5
npm run build:h5
```

## API 对接

后端服务地址配置在 `src/services/index.ts`：

```typescript
const BASE_URL = '/api'
```

开发环境通过 Vite 代理到后端：

```typescript
// vite.config.ts
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true
  }
}
```

## 后端 API 列表

### 认证模块
- `POST /api/user/wx-login` - 微信登录
- `POST /api/user/phone-login` - 手机号验证码登录
- `GET /api/user/info` - 获取用户信息

### 房源模块
- `POST /api/property` - 添加房源
- `PUT /api/property` - 编辑房源
- `DELETE /api/property/{id}` - 删除房源
- `GET /api/property/list` - 房源列表
- `GET /api/property/{id}` - 房源详情

### 租客模块
- `POST /api/tenant/check-in` - 入住登记
- `POST /api/tenant/check-out` - 退租办理
- `GET /api/tenant/list` - 租客列表
- `GET /api/tenant/{id}` - 租客详情

### 租金模块
- `POST /api/rent` - 创建租金记录
- `POST /api/rent/{id}/payment` - 确认收款
- `GET /api/rent/list` - 租金记录列表
- `GET /api/rent/due-today` - 今日待收租（D+0）
- `GET /api/rent/overdue` - 催租提醒（D+2/D+3）

## 开发规范

遵循 `../config/frontend-coding-rules.md` 前端开发规范：

- ✅ 使用 Script Setup 语法
- ✅ TypeScript 类型定义
- ✅ 统一 API 请求封装
- ✅ 错误处理
- ✅ Scoped 样式

## License

MIT
