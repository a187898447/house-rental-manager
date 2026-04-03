# 租房小程序 - 集成测试方案

## 测试环境要求

### 基础设施
- **MySQL 8.0+** (端口 3306)
  - 数据库：rental_db
  - 用户：root / 密码：root123
  
- **Nacos 2.2+** (端口 8848)
  - 用户：nacos / 密码：nacos
  
- **Sentinel** (端口 8080) - 可选

### 微服务端口
| 服务 | 端口 | 说明 |
|------|------|------|
| rental-gateway | 8080 | API 网关 |
| rental-user | 8081 | 用户服务 |
| rental-property | 8082 | 房源服务 |
| rental-bill | 8083 | 账单服务 |

### 启动命令
```bash
# 1. 启动 MySQL
docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root123 mysql:8.0

# 2. 启动 Nacos
docker run -d --name nacos -p 8848:8848 -e MODE=standalone nacos/nacos-server:2.2.0

# 3. 初始化数据库
mysql -h localhost -u root -proot123 < db/schema.sql

# 4. 启动微服务（按顺序）
cd rental-common && mvn spring-boot:run &
cd rental-user && mvn spring-boot:run &
cd rental-property && mvn spring-boot:run &
cd rental-bill && mvn spring-boot:run &
cd rental-gateway && mvn spring-boot:run &
```

---

## API 集成测试用例

### 1. 用户认证模块

#### 1.1 微信登录
```bash
curl -X POST http://localhost:8080/api/user/wx-login \
  -H "Content-Type: application/json" \
  -d '{
    "code": "test_wx_code_123",
    "encryptedData": "test_encrypted_data",
    "iv": "test_iv"
  }'
```
**预期**：返回 JWT token 和用户信息

#### 1.2 手机号登录
```bash
curl -X POST http://localhost:8080/api/user/phone-login \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "13800138000",
    "smsCode": "123456"
  }'
```
**预期**：返回 JWT token

#### 1.3 发送验证码
```bash
curl -X POST http://localhost:8080/api/user/send-code \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "13800138000",
    "type": "login"
  }'
```
**预期**：返回成功，模拟发送验证码

---

### 2. 房源管理模块

#### 2.1 获取房源列表
```bash
curl -X GET "http://localhost:8080/api/property/list?page=1&size=10" \
  -H "Authorization: Bearer {token}"
```
**预期**：返回房源列表（分页）

#### 2.2 创建房源
```bash
curl -X POST http://localhost:8080/api/property/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "title": "测试房源",
    "address": "测试地址",
    "rent": 3000,
    "area": 80,
    "rooms": 2,
    "halls": 1,
    "bathrooms": 1
  }'
```
**预期**：返回创建的房源 ID

#### 2.3 更新房源
```bash
curl -X PUT http://localhost:8080/api/property/update/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "rent": 3500
  }'
```
**预期**：更新成功

#### 2.4 删除房源
```bash
curl -X DELETE http://localhost:8080/api/property/delete/1 \
  -H "Authorization: Bearer {token}"
```
**预期**：删除成功

---

### 3. 租客管理模块

#### 3.1 租客列表
```bash
curl -X GET "http://localhost:8080/api/tenant/list?propertyId=1" \
  -H "Authorization: Bearer {token}"
```
**预期**：返回该房源的租客列表

#### 3.2 入住登记
```bash
curl -X POST http://localhost:8080/api/tenant/checkin \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "propertyId": 1,
    "tenantName": "张三",
    "tenantPhone": "13800138000",
    "leaseStart": "2026-04-01",
    "leaseEnd": "2027-04-01",
    "deposit": 6000
  }'
```
**预期**：返回租客 ID

#### 3.3 退租办理
```bash
curl -X POST http://localhost:8080/api/tenant/checkout/1 \
  -H "Authorization: Bearer {token}"
```
**预期**：退租成功，更新状态

---

### 4. 租金管理模块

#### 4.1 租金记录列表
```bash
curl -X GET "http://localhost:8080/api/rent/list?tenantId=1" \
  -H "Authorization: Bearer {token}"
```
**预期**：返回租金记录

#### 4.2 创建租金记录
```bash
curl -X POST http://localhost:8080/api/rent/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "tenantId": 1,
    "amount": 3000,
    "dueDate": "2026-05-01",
    "type": "monthly"
  }'
```
**预期**：返回租金记录 ID

#### 4.3 确认收租
```bash
curl -X PUT http://localhost:8080/api/rent/pay/1 \
  -H "Authorization: Bearer {token}"
```
**预期**：状态更新为已支付

---

### 5. 押金管理模块

#### 5.1 押金记录列表
```bash
curl -X GET "http://localhost:8080/api/deposit/list?tenantId=1" \
  -H "Authorization: Bearer {token}"
```
**预期**：返回押金记录

#### 5.2 收取押金
```bash
curl -X POST http://localhost:8080/api/deposit/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "tenantId": 1,
    "amount": 6000,
    "type": "checkin"
  }'
```
**预期**：返回押金记录 ID

#### 5.3 退还押金
```bash
curl -X PUT http://localhost:8080/api/deposit/refund/1 \
  -H "Authorization: Bearer {token}"
```
**预期**：状态更新为已退还

---

### 6. 水电账单模块

#### 6.1 水电账单列表
```bash
curl -X GET "http://localhost:8080/api/utility/list?tenantId=1" \
  -H "Authorization: Bearer {token}"
```
**预期**：返回水电账单

#### 6.2 创建水电账单
```bash
curl -X POST http://localhost:8080/api/utility/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "tenantId": 1,
    "type": "electricity",
    "previousReading": 1000,
    "currentReading": 1200,
    "unitPrice": 0.6,
    "month": "2026-04"
  }'
```
**预期**：返回账单 ID，自动计算金额

#### 6.3 水电气单价配置
```bash
curl -X GET http://localhost:8080/api/utility/price?propertyId=1 \
  -H "Authorization: Bearer {token}"
```
**预期**：返回单价配置

---

### 7. 合同管理模块

#### 7.1 创建合同
```bash
curl -X POST http://localhost:8080/api/contract/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "tenantId": 1,
    "propertyId": 1,
    "leaseStart": "2026-04-01",
    "leaseEnd": "2027-04-01",
    "rent": 3000,
    "deposit": 6000
  }'
```
**预期**：返回合同 ID

#### 7.2 签署合同
```bash
curl -X PUT http://localhost:8080/api/contract/sign/1 \
  -H "Authorization: Bearer {token}"
```
**预期**：状态更新为已签署

#### 7.3 解除合同
```bash
curl -X PUT http://localhost:8080/api/contract/terminate/1 \
  -H "Authorization: Bearer {token}"
```
**预期**：状态更新为已解除

---

### 8. 报修管理模块

#### 8.1 提交报修
```bash
curl -X POST http://localhost:8080/api/repair/submit \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "tenantId": 1,
    "title": "空调不制冷",
    "description": "卧室空调无法制冷",
    "images": ["image1.jpg", "image2.jpg"]
  }'
```
**预期**：返回报修 ID

#### 8.2 处理报修
```bash
curl -X PUT http://localhost:8080/api/repair/process/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "status": "processing",
    "remark": "已安排师傅上门"
  }'
```
**预期**：状态更新为处理中

#### 8.3 完成报修
```bash
curl -X PUT http://localhost:8080/api/repair/complete/1 \
  -H "Authorization: Bearer {token}"
```
**预期**：状态更新为已完成

---

## 测试执行脚本

### 运行所有测试
```bash
cd integration-test
bash run-all-tests.sh
```

### 运行单个模块测试
```bash
bash test-auth.sh
bash test-property.sh
bash test-tenant.sh
```

---

## 测试报告模板

```markdown
# 集成测试报告

**测试日期**: 2026-04-03
**测试环境**: Docker (MySQL + Nacos + 微服务)
**测试人**: Manager Agent

## 测试结果汇总

| 模块 | 测试用例数 | 通过 | 失败 | 通过率 |
|------|-----------|------|------|--------|
| 用户认证 | 3 | 3 | 0 | 100% |
| 房源管理 | 4 | 4 | 0 | 100% |
| 租客管理 | 3 | 3 | 0 | 100% |
| 租金管理 | 3 | 3 | 0 | 100% |
| 押金管理 | 3 | 3 | 0 | 100% |
| 水电账单 | 3 | 3 | 0 | 100% |
| 合同管理 | 3 | 3 | 0 | 100% |
| 报修管理 | 3 | 3 | 0 | 100% |
| **总计** | **25** | **25** | **0** | **100%** |

## 失败用例详情

(如有失败，记录详细信息)

## 问题与建议

(记录发现的问题和改进建议)
```

---

## 注意事项

1. **测试数据隔离**: 每次测试前清空测试数据
2. **Token 有效期**: JWT token 默认 24 小时有效
3. **并发测试**: 避免同时修改同一资源
4. **错误处理**: 记录所有 4xx/5xx 响应

---

## 附录：测试数据初始化 SQL

```sql
-- 清空测试数据
DELETE FROM t_tenant;
DELETE FROM t_property;
DELETE FROM t_user;
DELETE FROM t_rent_record;
DELETE FROM t_deposit;
DELETE FROM t_utility_bill;
DELETE FROM t_contract;
DELETE FROM t_repair;

-- 插入测试用户
INSERT INTO t_user (id, openid, phone, nickname, avatar, created_at)
VALUES (1, 'test_openid_001', '13800138000', '测试用户', 'avatar.jpg', NOW());
```
