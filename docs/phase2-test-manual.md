# 租房小程序 Phase 2 测试文档

## 概述

本文档描述租房小程序 Phase 2 功能的测试用例，涵盖催租提醒、水电管理、押金管理、其他费用、报修管理五大模块。

---

## 测试环境

- **后端**: Spring Boot 微服务
- **前端**: UniApp + Vue 3
- **数据库**: MySQL 8.0+
- **网关**: Spring Cloud Gateway

### 服务端口

| 服务 | 端口 |
|-----|------|
| Gateway | 8080 |
| rental-user | 8081 |
| rental-property | 8082 |
| rental-bill | 8083 |
| rental-notify | 8084 |
| rental-pay | 8085 |

---

## 功能模块测试

### 1. 催租提醒

#### 1.1 功能描述
租金账单逾期时，系统自动发送催租提醒。D+0、D+2、D+3 分别触发不同级别的提醒。

#### 1.2 测试用例

| 编号 | 测试场景 | 预期结果 |
|------|----------|----------|
| TC-001 | 租金逾期当天(D+0) | 系统自动生成提醒任务 |
| TC-002 | 租金逾期2天(D+2) | 发送第二轮催租通知 |
| TC-003 | 租金逾期3天(D+3) | 发送高优先级催租通知 |
| TC-004 | 租客支付账单后 | 取消未发送的催租任务 |
| TC-005 | 手动点击催租按钮 | 立即发送催租通知 |

#### 1.3 API 测试

```bash
# 获取租金账单列表
curl -X GET "http://localhost:8083/api/rent/bills/owner" \
  -H "X-User-Id: 1"

# 手动发送催租提醒
curl -X POST "http://localhost:8083/api/rent/bills/{id}/remind" \
  -H "X-User-Id: 1"

# 确认收款
curl -X POST "http://localhost:8083/api/rent/bills/{id}/pay" \
  -H "X-User-Id: 1"
```

---

### 2. 水电管理

#### 2.1 功能描述
配置房源水电单价，手动录入水电表读数，自动生成水电账单。

#### 2.2 测试用例

| 编号 | 测试场景 | 预期结果 |
|------|----------|----------|
| TC-006 | 获取房源水电配置 | 返回水费单价、电费单价 |
| TC-007 | 更新水电单价 | 单价保存成功 |
| TC-008 | 录入水表读数 | 计算用水量和水费 |
| TC-009 | 录入电表读数 | 计算用电量和电费 |
| TC-010 | 生成水电账单 | 账单创建成功 |
| TC-011 | 水电费支付 | 状态变更为已支付 |

#### 2.3 API 测试

```bash
# 获取水电费配置
curl -X GET "http://localhost:8082/api/property/{id}/utility-config" \
  -H "X-User-Id: 1"

# 设置水电费单价
curl -X PUT "http://localhost:8082/api/property/{id}/utility-config?waterPrice=3.0&electricityPrice=0.6" \
  -H "X-User-Id: 1"

# 获取水电账单列表
curl -X GET "http://localhost:8083/api/utility/owner" \
  -H "X-User-Id: 1"

# 创建水电账单
curl -X POST "http://localhost:8083/api/utility/bills" \
  -H "X-User-Id: 1" \
  -d '{
    "propertyId": 1,
    "tenantId": 1,
    "billMonth": "2026-03",
    "waterReading": 100,
    "waterReadingCurrent": 120,
    "waterAmount": 60,
    "electricityReading": 200,
    "electricityReadingCurrent": 250,
    "electricityAmount": 30
  }'

# 支付水电账单
curl -X POST "http://localhost:8083/api/utility/{id}/pay" \
  -H "X-User-Id: 1"
```

---

### 3. 押金管理

#### 3.1 功能描述
房东收取租客押金，支持部分退还和全额退还。

#### 3.2 测试用例

| 编号 | 测试场景 | 预期结果 |
|------|----------|----------|
| TC-012 | 查看押金列表 | 显示所有押金记录 |
| TC-013 | 查看押金详情 | 显示押金金额、状态、支付方式等 |
| TC-014 | 确认收款(缴纳押金) | 押金状态变更为已缴纳 |
| TC-015 | 退还押金(全额) | 押金状态变更为已退还 |
| TC-016 | 退还押金(部分) | 押金状态变更为部分退还 |

#### 3.3 API 测试

```bash
# 获取押金列表
curl -X GET "http://localhost:8083/api/deposit/owner" \
  -H "X-User-Id: 1"

# 获取押金详情
curl -X GET "http://localhost:8083/api/deposit/{id}" \
  -H "X-User-Id: 1"

# 缴纳押金
curl -X POST "http://localhost:8083/api/deposit/{id}/pay?payMethod=cash" \
  -H "X-User-Id: 1"

# 退还押金
curl -X POST "http://localhost:8083/api/deposit/{id}/refund?refundAmount=1000" \
  -H "X-User-Id: 1"
```

---

### 4. 其他费用

#### 4.1 功能描述
管理物业费、垃圾清运费、网络费等其他费用。

#### 4.2 测试用例

| 编号 | 测试场景 | 预期结果 |
|------|----------|----------|
| TC-017 | 查看费用列表 | 显示所有费用记录 |
| TC-018 | 创建其他费用 | 费用创建成功 |
| TC-019 | 查看费用详情 | 显示费用类型、金额等 |
| TC-020 | 确认支付 | 费用状态变更为已支付 |
| TC-021 | 删除费用 | 费用记录删除成功 |

#### 4.3 API 测试

```bash
# 获取费用列表
curl -X GET "http://localhost:8083/api/fee/owner" \
  -H "X-User-Id: 1"

# 获取费用详情
curl -X GET "http://localhost:8083/api/fee/{id}" \
  -H "X-User-Id: 1"

# 创建费用
curl -X POST "http://localhost:8083/api/fee" \
  -H "X-User-Id: 1" \
  -d '{
    "propertyId": 1,
    "tenantId": 1,
    "feeType": "property",
    "amount": 200,
    "month": "2026-03",
    "remark": "物业费"
  }'

# 支付费用
curl -X POST "http://localhost:8083/api/fee/{id}/pay" \
  -H "X-User-Id: 1"

# 删除费用
curl -X DELETE "http://localhost:8083/api/fee/{id}" \
  -H "X-User-Id: 1"
```

---

### 5. 报修管理

#### 5.1 功能描述
租客提交报修，房东处理和完成报修。

#### 5.2 测试用例

| 编号 | 测试场景 | 预期结果 |
|------|----------|----------|
| TC-022 | 查看报修列表 | 显示所有报修记录 |
| TC-023 | 查看报修详情 | 显示报修内容、处理状态等 |
| TC-024 | 开始处理 | 状态变更为处理中 |
| TC-025 | 完成处理 | 状态变更为已完成 |
| TC-026 | 取消报修 | 状态变更为已取消 |

#### 5.3 API 测试

```bash
# 获取报修列表
curl -X GET "http://localhost:8083/api/repair/owner" \
  -H "X-User-Id: 1"

# 获取报修详情
curl -X GET "http://localhost:8083/api/repair/{id}" \
  -H "X-User-Id: 1"

# 开始处理
curl -X POST "http://localhost:8083/api/repair/{id}/process" \
  -H "X-User-Id: 1"

# 完成处理
curl -X POST "http://localhost:8083/api/repair/{id}/complete?remark=已修好" \
  -H "X-User-Id: 1"

# 取消报修
curl -X POST "http://localhost:8083/api/repair/{id}/cancel" \
  -H "X-User-Id: 1"
```

---

## 通用接口测试

### 通知管理

```bash
# 获取通知列表
curl -X GET "http://localhost:8084/api/notify/list" \
  -H "X-User-Id: 1"

# 获取未读数量
curl -X GET "http://localhost:8084/api/notify/unread-count" \
  -H "X-User-Id: 1"

# 标记已读
curl -X PUT "http://localhost:8084/api/notify/{id}/read" \
  -H "X-User-Id: 1"

# 全部已读
curl -X PUT "http://localhost:8084/api/notify/read-all" \
  -H "X-User-Id: 1"
```

---

## 测试数据准备

### 准备测试房源
```sql
INSERT INTO property (owner_id, name, address, status) 
VALUES (1, '测试房源', '测试地址', 1);
```

### 准备测试租客
```sql
INSERT INTO tenant (property_id, name, phone, status) 
VALUES (1, '租客张三', '13800138000', 1);
```

### 准备测试账单
```sql
INSERT INTO rent_record (property_id, tenant_id, amount, status, due_date) 
VALUES (1, 1, 2000, 0, '2026-03-25');
```

---

## 验收标准

### 功能验收
- [ ] 所有 API 接口正常返回
- [ ] 前端页面可以正常访问和操作
- [ ] 数据正确保存到数据库

### 权限验收
- [ ] 未登录无法访问房东端接口
- [ ] X-User-Id 正确传递到后端
- [ ] 数据按 ownerId 正确隔离

---

## 附录

### 环境变量
```bash
# 数据库连接
DB_HOST=localhost
DB_PORT=3306
DB_NAME=house_rental
DB_USER=root
DB_PASSWORD=password

# JWT 配置
JWT_SECRET=your-secret-key
JWT_EXPIRE=86400
```

### 常见问题

1. **接口返回 401**: 检查 JWT token 是否正确传递
2. **数据查不到**: 检查 X-User-Id 是否正确
3. **支付失败**: 检查支付配置是否正确