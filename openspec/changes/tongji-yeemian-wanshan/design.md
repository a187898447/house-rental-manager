# Phase 2 技术设计方案

## Context

### 当前状态

- **Phase 1 已完成**：登录、房源管理、租客管理、租金管理基础功能
- **数据库**：MySQL 8
- **后端**：Spring Cloud Alibaba 微服务架构
- **前端**：UniApp + Vue 3 + TypeScript
- **系统状态**：完整的微服务框架，已包含 user/property/bill/gateway 等服务

### 本系统是完整的框架，不是单 Phase 2

- 登录认证（JWT + Spring Security）
- 房源管理（楼栋、房源 CRUD）
- 租客管理（入住/退租）
- 租金管理（租金账单、支付）
- 水电管理（楼栋单价、抄表、账单）
- 押金管理（收取/退还记录）
- 报修管理（提交/处理/状态）
- 通知系统（系统内消息）
- 数据统计（收入报表）

### Phase 2 需要解决的问题

1. Property entity 缺少 4 个字段（name/address/building/type）
2. 催租提醒逻辑（D+0/D+2/D+3）
3. 水电管理（楼栋单价+抄表+账单生成）
4. 其他费用管理
5. 押金管理（纯线下协商，系统只记录）
6. 报修管理
7. 历史账单
8. 通用推送通知
9. 数据统计
10. 租客端（账单+缴费+报修）
11. 电子合同

### 技术约束

- 微信公众号模板消息暂缓，Phase 2 先实现通用推送
- 水电官方API对接暂缓，Phase 2 实现手动录入
- 电子收据移至 Phase 3

## Goals / Non-Goals

**Goals:**
- 修复 Property entity 字段缺失问题
- 实现完整的水电费用管理流程
- 实现催租提醒（固定到期日触发三轮提醒）
- 实现押金收取/退还记录（纯线下协商）
- 实现报修提交/处理/状态跟踪
- 实现租客端基本功能（查看账单+在线缴费+报修申请）
- 实现电子合同（房东生成+租客手写签名）
- 实现数据统计报表

**Non-Goals:**
- 微信公众号模板消息推送（Phase 3+）
- 水电官方API对接（Phase 3+）
- 电子收据PDF导出（Phase 3）
- 预约看房功能
- 租客房东即时消息

## Decisions

### Decision 1: 水电账单生成方式

**选择**：手动抄表录入 + 自动计算账单

**理由**：
- 官方API对接暂缓
- 房东手动录入更可控
- 计算逻辑简单（水费=用量×单价）

**实现**：
```java
// 用量计算
BigDecimal waterUsage = currentReading - lastReading;
BigDecimal waterAmount = waterUsage * building.getWaterPrice();
```

### Decision 2: 催租提醒触发机制

**选择**：定时任务 + 固定到期日判断

**理由**：
- 简单可靠
- 不依赖外部服务
- 租客逾期才触发下一轮

**实现**：
```java
// BillTask 定时检查
// D+0: 租客第1次提醒
// D+2: 租客第2次提醒
// D+3: 租客第3次提醒 + 房东通知
```

### Decision 3: 押金退还处理

**选择**：纯线下协商，系统只记录最终金额

**理由**：
- 符合 PRD 要求
- 减少系统复杂度
- 避免纠纷

**实现**：
```sql
deposit 表增加字段：
- refund_amount: 实退金额（线下协商后填写）
- refund_date: 退还日期
- checkout_photos: 退房照片JSON（可选）
```

### Decision 4: 租客签名方式

**选择**：前端 Canvas 手绘签名

**理由**：
- 微信小程序天然支持
- 实现简单
- 法律效力

**实现**：
```typescript
// 前端 Canvas 绘制签名
// 上传签名图片到 OSS
// 保存签名URL到 contract 表
```

### Decision 5: 推送通知方式

**选择**：系统内通知机制（数据库记录）

**理由**：
- 微信公众号暂缓
- 实现简单
- 可扩展

**实现**：
```sql
message 表：
- type: 通知类型（reminder/push/alarm）
- is_read: 已读状态
```

## 技术架构

### 微服务划分

| 服务 | 职责 |
|------|------|
| rental-user | 用户、认证 |
| rental-property | 房源、楼栋 |
| rental-bill | 账单、租金、水电、押金、报修 |
| rental-notify | 通知推送 |
| rental-gateway | API网关 |

### 新增数据模型

```
utility_bill     # 水电账单
other_fee        # 其他费用
deposit          # 押金（含refund_amount）
repair_request   # 报修
contract         # 电子合同（含签名）
message          # 通知
```

### API 设计

| 模块 | 接口 | 方法 |
|------|------|------|
| 水电 | /api/bill/utility/* | GET/POST |
| 其他费用 | /api/bill/other/* | GET/POST |
| 押金 | /api/bill/deposit/* | GET/POST |
| 报修 | /api/bill/repair/* | GET/POST/PUT |
| 合同 | /api/bill/contract/* | GET/POST |
| 统计 | /api/statistics/* | GET |

## Risks / Trade-offs

| 风险 | 影响 | 缓解措施 |
|------|------|----------|
| 水电抄表错误 | 账单金额不准 | 提供修改功能 |
| 催租提醒打扰 | 用户体验 | 可设置关闭 |
| 合同法律效力 | 纠纷 | 提示仅作参考 |
| 推送送达率 | 通知看不到 | 提供多渠道备选 |

## Migration Plan

### Phase 2 初期（第1-2周）

1. 修复 Property entity 字段
2. 实现水电基础功能（楼栋配置+抄表+账单）
3. 实现押金管理

### Phase 2 中期（第3-4周）

4. 实现催租提醒定时任务
5. 实现报修管理
6. 实现其他费用

### Phase 2 后期（第5-6周）

7. 实现电子合同
8. 实现数据统计
9. 实现租客端
10. 实现通用推送

### 部署

```bash
# 1. 执行 SQL 脚本
mysql -u root -p rental_db < sql/init.sql

# 2. 编译后端
mvn clean package -DskipTests

# 3. 启动服务
docker-compose up -d
```

### 回滚

```bash
# 回滚代码
git revert <commit>

# 回滚数据库（手动备份）
mysql -u root -p rental_db < backup.sql
```

## Open Questions

1. **推送渠道**：微信模板消息暂缓，使用数据库记录是否足够？
2. **电子合同**：是否需要集成电子签章服务？
3. **在线支付**：微信支付如何对接？
4. **租客注册**：是否需要独立租客端注册流程？