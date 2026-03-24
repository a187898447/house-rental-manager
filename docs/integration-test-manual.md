# 租房小程序集成测试操作手册

**版本**：v2.0  
**更新日期**：2026-03-24  
**数据库**：MySQL 8  
**架构**：Spring Cloud 微服务 + Nacos

---

## 1. 环境要求

### 1.1 后端环境

| 组件 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 17+ | Java 运行环境 |
| Maven | 3.8+ | 项目构建工具 |
| MySQL | 8.0+ | 关系型数据库 |
| Redis | 7.x | 缓存数据库 |
| Nacos | 2.2.x | 服务注册/配置中心 |

### 1.2 前端环境

| 组件 | 版本要求 | 说明 |
|------|----------|------|
| Node.js | 18+ | JavaScript 运行时 |
| npm / pnpm | 最新版 | 包管理工具 |
| HBuilderX | 3.x | uni-app 开发工具（可选） |

### 1.3 Docker 环境（推荐）

- Docker 20.x+
- Docker Compose 2.x+

---

## 2. 本地启动步骤

### 2.1 数据库初始化

```bash
# MySQL 客户端连接
mysql -u root -p

# 创建数据库（已存在则跳过）
CREATE DATABASE IF NOT EXISTS rental_db DEFAULT CHARACTER SET utf8mb4;

# 导入建表语句
USE rental_db;
SOURCE /path/to/mysql-init.sql;
```

> ⚠️ **注意**：如果数据库已存在，先删除重建：
> ```sql
> DROP DATABASE IF EXISTS rental_db;
> CREATE DATABASE rental_db DEFAULT CHARACTER SET utf8mb4;
> ```

### 2.2 方式一：Docker 一键启动（推荐）

```bash
# 1. 克隆项目
git clone https://github.com/a187898447/house-rental-manager.git
cd house-rental-manager

# 2. 启动所有服务（MySQL + Redis + Nacos）
docker-compose -f deploy/docker-compose.yml up -d

# 3. 查看服务状态
docker-compose -f deploy/docker-compose.yml ps

# 4. 查看日志
docker-compose -f deploy/docker-compose.yml logs -f
```

**启动的服务：**

| 服务 | 端口 | 说明 |
|------|------|------|
| MySQL | 3306 | 数据库 |
| Redis | 6379 | 缓存 |
| Nacos | 8848 | 注册/配置中心 |
| rental-gateway | 8080 | API 网关 |
| rental-user | 8081 | 用户服务 |
| rental-property | 8082 | 房源服务 |
| rental-bill | 8083 | 账单服务 |
| rental-notify | 8084 | 通知服务 |

### 2.3 方式二：本地手动启动

#### 2.3.1 启动中间件

```bash
# MySQL
docker run -d --name rental-mysql \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=rental123456 \
  -e MYSQL_DATABASE=rental_db \
  mysql:8.0 --character-set-server=utf8mb4

# Redis
docker run -d --name rental-redis \
  -p 6379:6379 \
  redis:7-alpine

# Nacos
docker run -d --name rental-nacos \
  -p 8848:8848 -p 9848:9848 \
  -e MODE=standalone \
  nacos/nacos-server:v2.2.3-slim
```

#### 2.3.2 编译后端

```bash
cd house-rental-manager
mvn clean package -DskipTests
```

#### 2.3.3 启动后端服务（按顺序）

```bash
java -jar rental-gateway/target/rental-gateway.jar
java -jar rental-user/target/rental-user.jar
java -jar rental-property/target/rental-property.jar
java -jar rental-bill/target/rental-bill.jar
java -jar rental-notify/target/rental-notify.jar
```

#### 2.3.4 启动前端

```bash
# 安装依赖
cd house-rental-manager/frontend
npm install

# H5 开发模式
npm run dev:h5

# 编译微信小程序
npm run build:mp-weixin
```

---

## 3. API 测试清单

> ⚠️ **所有接口通过网关访问**，网关端口 `8080`，会自动路由到对应服务。

### 3.1 用户/认证

| API 路径 | 方法 | 说明 | 认证 |
|---------|------|------|------|
| `/api/user/login` | POST | 微信登录 | 否 |
| `/api/user/send-code` | POST | 发送验证码 | 否 |
| `/api/user/bind-phone` | POST | 绑定手机号 | 是 |
| `/api/user/info` | GET | 获取当前用户信息 | 是 |

**登录请求示例：**
```json
POST /api/user/login
{"code": "微信授权code"}
```

### 3.2 房源管理

| API 路径 | 方法 | 说明 | 认证 |
|---------|------|------|------|
| `/api/property/list` | GET | 房源列表（分页） | 是 |
| `/api/property/{id}` | GET | 房源详情 | 是 |
| `/api/property` | POST | 新增房源 | 是 |
| `/api/property/{id}` | PUT | 更新房源 | 是 |
| `/api/property/{id}` | DELETE | 删除房源 | 是 |
| `/api/property/public/list` | GET | 公开房源列表（无需登录） | 否 |
| `/api/property/public/{id}` | GET | 公开房源详情（无需登录） | 否 |

**新增房源请求示例：**
```json
POST /api/property
{
  "buildingId": 1,
  "name": "房源名称",
  "address": "详细地址",
  "building": "1栋",
  "unit": "1单元",
  "roomNumber": "101",
  "type": "2室1厅",
  "area": 80.5,
  "rentAmount": 3000,
  "depositAmount": 6000
}
```

### 3.3 租客管理

| API 路径 | 方法 | 说明 | 认证 |
|---------|------|------|------|
| `/api/tenant/checkin` | POST | 入住登记 | 是 |
| `/api/tenant/{id}` | GET | 租客详情 | 是 |
| `/api/tenant/property/{propertyId}` | GET | 房源下的租客列表 | 是 |
| `/api/tenant/owner/list` | GET | 房东所有租客 | 是 |
| `/api/tenant/{id}/checkout` | POST | 退租办理 | 是 |
| `/api/tenant/{id}` | DELETE | 删除租客 | 是 |

### 3.4 预约看房

| API 路径 | 方法 | 说明 | 认证 |
|---------|------|------|------|
| `/api/tenant/appointment` | POST | 创建预约（租客） | 否 |
| `/api/tenant/appointment/my` | GET | 我的预约（租客，按手机号） | 否 |
| `/api/tenant/appointment/owner` | GET | 收到的预约（房东） | 是 |
| `/api/tenant/appointment/{id}/status` | PUT | 更新预约状态 | 是 |

### 3.5 租金账单

| API 路径 | 方法 | 说明 | 认证 |
|---------|------|------|------|
| `/api/rent/bills` | POST | 创建租金账单 | 是 |
| `/api/rent/bills/{id}` | GET | 账单详情 | 是 |
| `/api/rent/bills/owner` | GET | 房东账单列表 | 是 |
| `/api/rent/bills/tenant` | GET | 租客账单列表 | 是 |
| `/api/rent/bills/{id}/pay` | POST | 标记已支付 | 是 |
| `/api/rent/bills/{id}/remind` | POST | 发送催租提醒 | 是 |
| `/api/rent/bills/{id}` | DELETE | 取消账单 | 是 |

### 3.6 水电账单

| API 路径 | 方法 | 说明 | 认证 |
|---------|------|------|------|
| `/api/utility/bills` | POST | 创建水电账单 | 是 |
| `/api/utility/{id}` | GET | 水电账单详情 | 是 |
| `/api/utility/tenant` | GET | 租客水电账单列表 | 是 |
| `/api/utility/owner` | GET | 房东水电账单列表 | 是 |
| `/api/utility/{id}/pay` | POST | 标记已支付 | 是 |

**创建水电账单请求示例：**
```json
POST /api/utility/bills
{
  "propertyId": 1,
  "tenantId": 1,
  "billMonth": "2026-03",
  "waterReading": 120.5,
  "waterReadingCurrent": 135.2,
  "waterAmount": 44.1,
  "electricityReading": 300,
  "electricityReadingCurrent": 450,
  "electricityAmount": 82.5,
  "source": 0
}
```

### 3.7 押金管理

| API 路径 | 方法 | 说明 | 认证 |
|---------|------|------|------|
| `/api/deposit/{id}` | GET | 押金详情 | 是 |
| `/api/deposit/tenant` | GET | 租客押金列表 | 是 |
| `/api/deposit/owner` | GET | 房东押金列表 | 是 |
| `/api/deposit/{id}/pay` | POST | 缴纳押金 | 是 |
| `/api/deposit/{id}/refund` | POST | 退还押金 | 是 |

### 3.8 其他费用

| API 路径 | 方法 | 说明 | 认证 |
|---------|------|------|------|
| `/api/fee` | POST | 创建其他费用 | 是 |
| `/api/fee/{id}` | GET | 费用详情 | 是 |
| `/api/fee/tenant` | GET | 租客费用列表 | 是 |
| `/api/fee/owner` | GET | 房东费用列表 | 是 |
| `/api/fee/{id}/pay` | POST | 标记已支付 | 是 |
| `/api/fee/{id}` | DELETE | 删除费用 | 是 |

### 3.9 报修管理

| API 路径 | 方法 | 说明 | 认证 |
|---------|------|------|------|
| `/api/repair` | POST | 提交报修 | 是 |
| `/api/repair/{id}` | GET | 报修详情 | 是 |
| `/api/repair/tenant` | GET | 租客报修列表 | 是 |
| `/api/repair/owner` | GET | 房东报修列表 | 是 |
| `/api/repair/{id}/process` | POST | 开始处理 | 是 |
| `/api/repair/{id}/complete` | POST | 完成处理 | 是 |
| `/api/repair/{id}/cancel` | POST | 取消报修 | 是 |

### 3.10 通知消息

| API 路径 | 方法 | 说明 | 认证 |
|---------|------|------|------|
| `/api/notify/list` | GET | 通知列表 | 是 |
| `/api/notify/unread-count` | GET | 未读数量 | 是 |
| `/api/notify/{id}/read` | PUT | 标记已读 | 是 |
| `/api/notify/read-all` | PUT | 全部已读 | 是 |
| `/api/notify/{id}` | DELETE | 删除通知 | 是 |

### 3.11 合同管理

| API 路径 | 方法 | 说明 | 认证 |
|---------|------|------|------|
| `/api/contract` | POST | 创建合同 | 是 |
| `/api/contract/{id}` | GET | 合同详情 | 是 |
| `/api/contract/owner/list` | GET | 房东合同列表 | 是 |
| `/api/contract/tenant/list` | GET | 租客合同列表 | 是 |
| `/api/contract/{id}` | PUT | 更新合同 | 是 |
| `/api/contract/{id}/sign` | POST | 签署合同 | 是 |
| `/api/contract/{id}/sign-url` | GET | 获取签署URL | 是 |
| `/api/contract/{id}/status` | PUT | 更新合同状态 | 是 |
| `/api/contract/{id}/terminate` | POST | 终止合同 | 是 |

---

## 4. 测试流程

### 4.1 用户认证流程

1. `POST /api/user/login` — 微信登录，获取 token
2. `GET /api/user/info` — 用 token 获取用户信息
3. 验证返回的 `role`（landlord/tenant）

### 4.2 房源管理流程（房东）

1. `POST /api/property` — 新增房源（填写 name/address/building/type 等）
2. `GET /api/property/list` — 查询房源列表
3. `GET /api/property/{id}` — 查看房源详情
4. `PUT /api/property/{id}` — 编辑房源
5. `DELETE /api/property/{id}` — 删除房源

### 4.3 租客管理流程（房东）

1. `POST /api/tenant/checkin` — 入住登记
2. `GET /api/tenant/property/{propertyId}` — 查看房源下租客
3. `POST /api/tenant/{id}/checkout` — 退租办理

### 4.4 预约看房流程（租客）

1. `GET /api/property/public/list` — 浏览公开房源
2. `POST /api/tenant/appointment` — 创建预约
3. `GET /api/tenant/appointment/my?phone=xxx` — 租客查看我的预约

### 4.5 租金账单流程（房东）

1. `POST /api/rent/bills` — 创建月度账单
2. `GET /api/rent/bills/owner` — 查看账单列表
3. `POST /api/rent/bills/{id}/remind` — 发送催租提醒
4. `POST /api/rent/bills/{id}/pay` — 标记已支付

### 4.6 水电账单流程（房东）

1. `POST /api/utility/bills` — 录入水电读数（上期+本期自动算用量）
2. `GET /api/utility/owner` — 查看水电账单列表
3. `POST /api/utility/{id}/pay` — 标记已支付

### 4.7 押金管理流程

1. `POST /api/deposit/{id}/pay` — 缴纳押金
2. `POST /api/deposit/{id}/refund?refundAmount=5000` — 退还押金

### 4.8 报修流程

1. `POST /api/repair` — 租客提交报修
2. `GET /api/repair/owner` — 房东查看报修列表
3. `POST /api/repair/{id}/process` — 房东开始处理
4. `POST /api/repair/{id}/complete` — 完成处理

---

## 5. Swagger API 文档

服务启动后访问各服务的 Knife4j 文档：

| 服务 | 地址 |
|------|------|
| 网关 | http://localhost:8080 |
| 用户服务 | http://localhost:8081/doc.html |
| 房源服务 | http://localhost:8082/doc.html |
| 账单服务 | http://localhost:8083/doc.html |
| 通知服务 | http://localhost:8084/doc.html |

---

## 6. 常见问题

### Q1: MySQL 连接失败
**解决**：检查 MySQL 是否启动，确认端口 3306 是否被占用

### Q2: Nacos 无法启动
**解决**：确保 8848 端口未被占用，查看 Nacos 日志

### Q3: 前端请求后端失败
**解决**：
1. 检查后端服务是否全部启动
2. 检查网关配置是否正确
3. 确认前端 API baseURL 配置

### Q4: Token 过期
**解决**：重新调用登录 API 获取新 token

### Q5: 编译报错 "cannot find symbol"
**解决**：执行 `mvn clean package -DskipTests` 重新编译

### Q6: 数据库表不存在
**解决**：重新执行 `mysql-init.sql` 导入建表语句

---

## 7. 测试检查清单

- [ ] 后端服务全部启动成功
- [ ] MySQL / Redis / Nacos 连接正常
- [ ] 用户登录功能正常
- [ ] 房源 CRUD 功能正常（含 name/address/building/type 字段）
- [ ] 租客入住/退租功能正常
- [ ] 预约看房功能正常
- [ ] 租金账单创建+支付正常
- [ ] 水电账单录入+支付正常（含上期/本期读数）
- [ ] 押金缴纳/退还功能正常
- [ ] 报修提交+处理功能正常
- [ ] 前端页面展示正常

---

## 8. Nacos 配置

- 访问地址：http://localhost:8848/nacos
- 默认账号：nacos / nacos
- 配置数据源、Redis 连接信息等
