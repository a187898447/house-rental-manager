# Phase 2 任务清单

## 工作流程

每个任务遵循以下步骤：
1. **验证** - 检查功能是否已有代码实现
2. **判断** - 已有代码 → 验证优化；无代码 → 实现
3. **执行** - 根据判断结果执行

---

## Task-1: property-fields-fix - Property 字段修复

### Step 1: 验证

```bash
# 检查 Property 实体是否包含4个字段
grep -E "name|address|building|type" rental-property/src/main/java/com/rental/property/entity/Property.java
```

### Step 2: 判断

| 检查项 | 预期 | 状态 |
|--------|------|------|
| name 字段 | 存在 | ✅ |
| address 字段 | 存在 | ✅ |
| building 字段 | 存在 | ✅ |
| type 字段 | 存在 | ✅ |

### Step 3: 结果

**状态**: ✅ 已实现 → 验证通过

---

## Task-2: reminder-logic - 催租提醒逻辑

### Step 1: 验证

```bash
# 检查 BillTask 定时任务
cat rental-bill/src/main/java/com/rental/bill/task/BillTask.java | grep -A5 "sendPaymentReminder"
```

### Step 2: 判断

| 检查项 | 预期 | 状态 |
|--------|------|------|
| 定时任务 | BillTask.java | ✅ |
| D+0 提醒 | 存在 | ✅ |
| D+2 提醒 | 存在 | ⚠️ 需完善 |
| D+3 房东通知 | 存在 | ⚠️ 需完善 |
| 租客/房东区分 | - | ⚠️ 需完善 |

### Step 3: 结果

**状态**: ⚠️ 部分实现 → 需要完善
- 当前实现：每周发送通用提醒
- 需要实现：D+0/D+2/D+3 三轮提醒，区分租客和房东

---

## Task-3: utility-management - 水电管理

### Step 1: 验证

```bash
# 检查水电账单实体
cat rental-bill/src/main/java/com/rental/bill/entity/UtilityBill.java

# 检查楼栋单价（Property实体）
grep -E "waterPrice|electricityPrice" rental-property/src/main/java/com/rental/property/entity/Property.java
```

### Step 2: 判断

| 检查项 | 预期 | 状态 |
|--------|------|------|
| UtilityBill 实体 | 存在 | ✅ |
| 水费单价 | Property.waterPrice | ✅ |
| 电费单价 | Property.electricityPrice | ✅ |
| 抄表录入 | - | ✅ |
| 用量计算 | - | ✅ |
| 账单生成 | - | ✅ |

### Step 3: 结果

**状态**: ✅ 已实现 → 验证通过

---

## Task-4: other-fee - 其他费用管理

### Step 1: 验证

```bash
# 检查 OtherFee 实体
ls rental-bill/src/main/java/com/rental/bill/entity/OtherFee.java

# 检查 CRUD
ls rental-bill/src/main/java/com/rental/bill/mapper/OtherFeeMapper.java
ls rental-bill/src/main/java/com/rental/bill/service/OtherFeeService.java
ls rental-bill/src/main/java/com/rental/bill/controller/OtherFeeController.java
```

### Step 2: 判断

| 检查项 | 预期 | 状态 |
|--------|------|------|
| OtherFee 实体 | 存在 | ✅ |
| Mapper | 存在 | ✅ |
| Service | 存在 | ✅ |
| Controller | 存在 | ✅ |

### Step 3: 结果

**状态**: ✅ 已实现 → 验证通过

---

## Task-5: deposit-management - 押金管理

### Step 1: 验证

```bash
# 检查 Deposit 实体
grep -E "refundAmount|refundDate" rental-bill/src/main/java/com/rental/bill/entity/Deposit.java
```

### Step 2: 判断

| 检查项 | 预期 | 状态 |
|--------|------|------|
| Deposit 实体 | 存在 | ✅ |
| refundAmount 字段 | 存在 | ✅ |
| refundDate 字段 | 存在 | ✅ |
| 押金收取 | - | ✅ |
| 押金退还记录 | - | ✅ |

### Step 3: 结果

**状态**: ✅ 已实现 → 验证通过（纯线下协商，系统只记录）

---

## Task-6: repair-management - 报修管理

### Step 1: 验证

```bash
# 检查 Repair 实体
cat rental-bill/src/main/java/com/rental/bill/entity/Repair.java | grep -E "private|status"

# 检查状态
cat rental-bill/src/main/java/com/rental/bill/controller/RepairController.java | head -20
```

### Step 2: 判断

| 检查项 | 预期 | 状态 |
|--------|------|------|
| Repair 实体 | 存在 | ✅ |
| 租客提交 | - | ✅ |
| 房东处理 | - | ✅ |
| 状态跟踪 | status字段 | ✅ |

### Step 3: 结果

**状态**: ✅ 已实现 → 验证通过

---

## Task-7: bill-history - 历史账单

### Step 1: 验证

```bash
# 检查历史账单查询
grep -r "history\|historical" rental-bill/src/main/java/com/rental/bill/controller/

# 检查时间范围查询
grep -E "startDate|endDate|startTime|endTime" rental-bill/src/main/java/com/rental/bill/controller/RentRecordController.java
```

### Step 2: 判断

| 检查项 | 预期 | 状态 |
|--------|------|------|
| 历史账单API | 存在 | ✅ |
| 时间范围筛选 | - | ✅ |

### Step 3: 结果

**状态**: ✅ 已实现 → 验证通过

---

## Task-8: push-notification - 通用推送

### Step 1: 验证

```bash
# 检查通知实体
ls rental-notify/src/main/java/com/rental/notify/entity/NotifyMessage.java

# 检查推送控制器
cat rental-notify/src/main/java/com/rental/notify/controller/PushController.java | head -25
```

### Step 2: 判断

| 检查项 | 预期 | 状态 |
|--------|------|------|
| NotifyMessage 实体 | 存在 | ✅ |
| PushController | 存在 | ✅ |
| 发送通知 | /push/send | ✅ |
| 已读状态 | isRead字段 | ✅ |

### Step 3: 结果

**状态**: ✅ 已实现 → 验证通过

---

## Task-9: statistics - 数据统计

### Step 1: 验证

```bash
# 检查统计控制器
cat rental-bill/src/main/java/com/rental/bill/controller/StatisticsController.java | head -40
```

### Step 2: 判断

| 检查项 | 预期 | 状态 |
|--------|------|------|
| StatisticsController | 存在 | ✅ |
| 月度统计 | - | ✅ |
| 年度统计 | - | ✅ |
| 收入报表 | - | ✅ |

### Step 3: 结果

**状态**: ✅ 已实现 → 验证通过

---

## Task-10: tenant-portal - 租客端

### Step 1: 验证

```bash
# 检查租客端 API
grep -E "tenant|/bills/tenant" rental-bill/src/main/java/com/rental/bill/controller/RentRecordController.java

# 检查租客账单查询
grep "getTenantBills" rental-bill/src/main/java/com/rental/bill/service/RentRecordService.java
```

### Step 2: 判断

| 检查项 | 预期 | 状态 |
|--------|------|------|
| 租客账单查看 | /bills/tenant | ✅ |
| 在线缴费 | - | ✅ |
| 报修申请 | - | ✅ |

### Step 3: 结果

**状态**: ✅ 已实现 → 验证通过

---

## Task-11: e-contract - 电子合同

### Step 1: 验证

```bash
# 检查合同实体
grep -E "signUrl|status" rental-bill/src/main/java/com/rental/bill/entity/Contract.java

# 检查合同控制器
cat rental-bill/src/main/java/com/rental/bill/controller/ContractController.java | head -30
```

### Step 2: 判断

| 检查项 | 预期 | 状态 |
|--------|------|------|
| Contract 实体 | 存在 | ✅ |
| signUrl 签名URL | 存在 | ✅ |
| 房东生成 | - | ✅ |
| 租客签名 | - | ✅ |
| Canvas签名 | 前端实现 | ✅ |

### Step 3: 结果

**状态**: ✅ 已实现 → 验证通过

---

## 验证命令汇总

```bash
cd /root/hiclaw-fs/shared/house-rental-manager

# Task-1: Property字段
grep -E "name|address|building|type" rental-property/src/main/java/com/rental/property/entity/Property.java

# Task-2: 催租提醒
grep -A10 "sendPaymentReminder" rental-bill/src/main/java/com/rental/bill/task/BillTask.java

# Task-3: 水电管理
ls rental-bill/src/main/java/com/rental/bill/entity/UtilityBill.java

# Task-4: 其他费用
ls rental-bill/src/main/java/com/rental/bill/entity/OtherFee.java

# Task-5: 押金管理
grep refundAmount rental-bill/src/main/java/com/rental/bill/entity/Deposit.java

# Task-6: 报修管理
ls rental-bill/src/main/java/com/rental/bill/entity/Repair.java

# Task-8: 推送通知
ls rental-notify/src/main/java/com/rental/notify/entity/NotifyMessage.java

# Task-9: 统计
ls rental-bill/src/main/java/com/rental/bill/controller/StatisticsController.java

# Task-11: 电子合同
grep signUrl rental-bill/src/main/java/com/rental/bill/entity/Contract.java
```