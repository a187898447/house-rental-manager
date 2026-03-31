## Why

租房小程序需要实现完整的租赁管理流程，包括房东端和租客端的所有功能，替代传统的记事本/Excel管理方式，实现房源数字化管理。

## What Changes

### 房东端功能
- 微信一键登录 + 手机号登录（房东/住户角色选择）
- 房源管理：添加、编辑、删除、列表、详情
- 租客管理：入住登记、租客列表、退租办理
- 租金管理：租金记录、收租提醒、催租提醒（D+0/D+2/D+3）
- 押金管理：押金收取、退还、记录
- 水电管理：单价配置、抄表录入、账单生成
- 其他费用：自定义收费（管理费、网络费、垃圾费等）
- 历史账单：历年租金/水电/其他费用可追溯
- 电子收据：收据导出（PDF/图片）
- 报修管理：报修记录、处理、状态跟踪
- 数据统计：月度/年度收入报表

### 租客端功能
- 微信登录/手机号登录
- 电子合同：房东生成、租客签署
- 手写签名：Canvas签名组件
- 账单查看：租金、水电、其他费用明细
- 在线缴费：微信支付
- 报修申请：拍照+描述问题提交维修请求
- 联系房东：消息/电话沟通
- 房源查看：查看绑定房源信息
- 设置：个人资料、通知设置

## Capabilities

### New Capabilities

- `login-auth`: 微信登录 + 手机号登录 + 角色选择（房东/住户）
- `property-management`: 房源管理（CRUD + 状态管理）
- `tenant-management`: 租客管理（入住登记、列表、退租）
- `rent-management`: 租金管理（记录、提醒、催租）
- `deposit-management`: 押金管理（收取、退还、记录）
- `utility-management`: 水电管理（单价配置、抄表、账单生成）
- `other-fee`: 其他费用管理（自定义收费）
- `bill-history`: 历史账单查询
- `receipt-export`: 电子收据导出
- `repair-management`: 报修管理（提交、处理、跟踪）
- `push-notification`: 消息通知
- `statistics`: 数据统计（收入报表）
- `tenant-portal`: 租客端门户
- `contract-signing`: 电子合同签订
- `canvas-signature`: Canvas手写签名
- `online-payment`: 在线支付（微信）
- `contact-landlord`: 联系房东

### Modified Capabilities

- (无 - 全新系统)

## Impact

- 后端：rental-user, rental-property, rental-bill, rental-gateway
- 前端：UniApp 多端小程序
- 数据库：MySQL 8.0
- 第三方：微信登录、微信支付