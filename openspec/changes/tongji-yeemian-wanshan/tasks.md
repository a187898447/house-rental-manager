# tongji-yeemian-wanshan Tasks

## 任务清单

每个任务前增加判断：检查功能是否已有代码实现，如有则验证优化，如无则实现。

---

## Task-1: property-fields-fix - Property 字段修复

### 状态: ✅ 已实现

### 检查结果

| 字段 | 代码位置 | 状态 |
|------|----------|------|
| name | `rental-property/.../entity/Property.java` | ✅ 已实现 |
| address | `rental-property/.../entity/Property.java` | ✅ 已实现 |
| building | `rental-property/.../entity/Property.java` | ✅ 已实现 |
| type | `rental-property/.../entity/Property.java` | ✅ 已实现 |

### 验证结果

Property entity 已包含所有4个字段：
```java
private String name;
private String address;
private String building;
private String type;
```

### 建议

功能已完整实现，验证通过。后续如有需求可考虑添加字段级注释说明。

---

## Task-2: reminder-logic - 催租提醒逻辑

### 状态: ⚠️ 需验证

### 检查结果

| 功能 | 代码位置 | 状态 |
|------|----------|------|
| 定时任务 | `rental-bill/.../task/BillTask.java` | ⚠️ 需验证逻辑 |
| D+0/D+2/D+3 提醒 | - | ⚠️ 需验证 |
| 租客通知 | - | ⚠️ 需验证 |
| 房东通知 | - | ⚠️ 需验证 |

### 验证任务

1. 检查 BillTask.java 是否实现了三轮提醒逻辑
2. 验证是否区分租客和房东通知
3. 验证通知触发条件（租金到期日判断）

---

## Task-3: utility-management - 水电管理

### 状态: ⚠️ 部分实现

### 检查结果

| 功能 | 代码位置 | 状态 |
|------|----------|------|
| 水电账单实体 | `rental-bill/.../entity/UtilityBill.java` | ✅ 已实现 |
| 楼栋单价配置 | Property.waterPrice/electricityPrice | ✅ 已实现 |
| 抄表录入 | - | ⚠️ 需验证 |
| 用量计算 | - | ⚠️ 需验证 |
| 账单生成 | - | ⚠️ 需验证 |

### 验证任务

1. 检查 UtilityBill 实体是否完整
2. 验证是否有抄表录入 API
3. 验证账单生成逻辑

---

## Task-4: other-fee - 其他费用管理

### 状态: ✅ 已实现

### 检查结果

| 功能 | 代码位置 | 状态 |
|------|----------|------|
| 实体 | `rental-bill/.../entity/OtherFee.java` | ✅ 已实现 |
| Mapper | `rental-bill/.../mapper/OtherFeeMapper.java` | ✅ 已实现 |
| Service | `rental-bill/.../service/OtherFeeService.java` | ✅ 已实现 |
| Controller | `rental-bill/.../controller/OtherFeeController.java` | ✅ 已实现 |

### 验证结果

其他费用模块已完整实现（entity/mapper/service/controller）。

---

## Task-5: deposit-management - 押金管理

### 状态: ⚠️ 需验证

### 检查结果

| 功能 | 代码位置 | 状态 |
|------|----------|------|
| 实体 | `rental-bill/.../entity/Deposit.java` | ✅ 已实现 |
| 押金收取 | - | ⚠️ 需验证 |
| 押金退还 | - | ⚠️ 需验证 |
| refund_amount 字段 | - | ⚠️ 需验证 |

### 验证任务

1. 检查 Deposit 实体是否包含 refund_amount 字段
2. 验证押金退还逻辑
3. 验证是否只记录线下协商结果

---

## Task-6: repair-management - 报修管理

### 状态: ⚠️ 部分实现

### 检查结果

| 功能 | 代码位置 | 状态 |
|------|----------|------|
| 实体 | `rental-bill/.../entity/Repair.java` | ✅ 已实现 |
| 租客提交 | - | ⚠️ 需验证 |
| 房东处理 | - | ⚠️ 需验证 |
| 状态跟踪 | - | ⚠️ 需验证 |

### 验证任务

1. 检查 Repair 实体字段
2. 验证提交和处理 API
3. 验证状态流转逻辑

---

## Task-7: bill-history - 历史账单

### 状态: ⚠️ 需验证

### 检查结果

| 功能 | 代码位置 | 状态 |
|------|----------|------|
| 历史账单查询 | - | ⚠️ 需验证 |
| 历年数据 | - | ⚠️ 需验证 |

### 验证任务

1. 验证是否有历史账单查询 API
2. 检查是否支持按时间范围筛选

---

## Task-8: push-notification - 通用推送

### 状态: ⚠️ 需验证

### 检查结果

| 功能 | 代码位置 | 状态 |
|------|----------|------|
| 通知实体 | - | ⚠️ 需验证 |
| 推送 API | - | ⚠️ 需验证 |
| 已读状态 | - | ⚠️ 需验证 |

### 验证任务

1. 检查 rental-notify 模块
2. 验证通知存储和发送逻辑

---

## Task-9: statistics - 数据统计

### 状态: ⚠️ 需验证

### 检查结果

| 功能 | 代码位置 | 状态 |
|------|----------|------|
| 统计 Controller | `rental-bill/.../controller/StatisticsController.java` | ✅ 已实现 |
| 月度报表 | - | ⚠️ 需验证 |
| 年度报表 | - | ⚠️ 需验证 |

### 验证任务

1. 检查 StatisticsController 接口
2. 验证月度/年度统计逻辑

---

## Task-10: tenant-portal - 租客端

### 状态: ⚠️ 需验证

### 检查结果

| 功能 | 代码位置 | 状态 |
|------|----------|------|
| 账单查看 | - | ⚠️ 需验证 |
| 在线缴费 | - | ⚠️ 需验证 |
| 报修申请 | - | ⚠️ 需验证 |

### 验证任务

1. 验证租客端 API
2. 检查是否支持租客独立登录

---

## Task-11: e-contract - 电子合同

### 状态: ⚠️ 需验证

### 检查结果

| 功能 | 代码位置 | 状态 |
|------|----------|------|
| 合同实体 | `rental-bill/.../entity/Contract.java` | ✅ 已实现 |
| 房东生成 | - | ⚠️ 需验证 |
| 租客签名 | - | ⚠️ 需验证 |
| Canvas 签名 | - | ⚠️ 需验证 |

### 验证任务

1. 检查 Contract 实体
2. 验证合同生成和签名流程
3. 检查前端是否实现 Canvas 签名

---

## 实施建议

### 优先处理

1. **reminder-logic (Task-2)** - 催租提醒是核心功能，需验证并优化
2. **utility-management (Task-3)** - 水电管理使用频繁，需验证完整
3. **deposit-management (Task-5)** - 押金退还字段需确认

### 后续处理

4. repair-management (Task-6)
5. statistics (Task-9)
6. e-contract (Task-11)

### 验证命令

```bash
# 检查实体
ls rental-bill/src/main/java/com/rental/bill/entity/

# 检查 Controller
ls rental-bill/src/main/java/com/rental/bill/controller/

# 检查 Service
ls rental-bill/src/main/java/com/rental/bill/service/
```