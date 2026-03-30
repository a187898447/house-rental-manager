# Phase 1 技术设计方案

## Context

### 当前状态

- **数据库**：MySQL 8
- **后端**：Spring Cloud Alibaba 微服务架构
  - rental-user: 用户、认证
  - rental-property: 房源管理
  - rental-bill: 租金、租客管理
- **前端**：UniApp + Vue 3 + TypeScript
- **已实现功能**：
  - 用户登录注册（账号密码）
  - 房源 CRUD（含逻辑删除）
  - 租客入住/退租
  - 租金记录和统计
  - 微信登录（WxServiceImpl 存在但未完整集成）

### 技术约束

- Phase 1 使用账号密码登录，微信登录暂不完整集成
- 租金按月计算，月中入住/退租按天比例计算
- 收租提醒使用系统内推送，微信公众号暂不接入

## Goals / Non-Goals

**Goals:**
- 完善房源管理 CRUD
- 实现租客入住/退租全流程
- 实现租金记录、提醒、统计
- 修复已知 Bug

**Non-Goals:**
- 完整微信登录（Phase 2+）
- 在线支付功能（Phase 3+）
- 微信公众号推送（Phase 2+）

## Decisions

### Decision 1: 房源逻辑删除

**选择**：使用 MyBatis-Plus 的 @TableLogic 注解实现逻辑删除

**理由**：
- 保留历史数据
- 不影响已关联的租客、账单
- 实现简单

**实现**：
```java
@TableLogic
private Integer deleted;
```

### Decision 2: 租金按天计算

**选择**：公式计算 = 月租金 × (租住天数 / 30)

**理由**：
- 符合 PRD 要求
- 计算简单，易于理解

### Decision 3: 收租提醒机制

**选择**：定时任务 + 固定日期触发

**实现**：
```java
@Scheduled(cron = "0 0 3 * * ?")  // 每天3点检查
public void sendPaymentReminder() {
    // 检查是否有账单需要提醒
}
```

### Decision 4: 租客状态管理

**选择**：
- status=0: 未入住
- status=1: 已入住
- status=2: 已退租

**实现**：Tenant 实体维护 status 字段

## Risks / Trade-offs

| 风险 | 影响 | 缓解 |
|------|------|------|
| 微信登录未集成 | 用户需账号登录 | Phase 1 可接受 |
| 提醒推送单一 | 通知可能不及时 | 后续接入公众号 |
| 按天计算不精确 | 闰年/月份天数不同 | 简化为固定30天 |

## Migration Plan

### 已完成

1. Property 实体实现 ✅
2. Tenant 入住/退租 ✅
3. RentRecord 记录 ✅

### 待完成

1. design.md 创建
2. reminder-logic 完善

## Open Questions

1. 微信登录是否需要回退代码？
2. 按天计算是否需要考虑实际天数？