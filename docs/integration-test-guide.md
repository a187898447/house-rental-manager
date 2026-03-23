# 租房小程序集成测试操作手册

## 1. 本地启动步骤

### 1.1 后端启动

#### 方式一：Maven运行（开发调试）

```bash
# 1. 确保已安装 JDK 17+ 和 Maven 3.8+
java -version  # 应显示 17.x
mvn -version

# 2. 启动依赖服务（需提前安装）
# - Nacos: http://localhost:8848 (用户名: nacos, 密码: nacos)
# - Redis: localhost:6379
# - 达梦数据库: localhost:5236

# 3. 编译项目
cd house-rental-manager
mvn clean package -DskipTests

# 4. 按顺序启动各微服务（建议使用IDE或后台运行）
# 启动顺序：gateway -> user -> property -> bill -> notify

cd rental-gateway
mvn spring-boot:run  # 端口 8080

cd rental-user
mvn spring-boot:run  # 端口 8081

cd rental-property
mvn spring-boot:run  # 端口 8082

cd rental-bill
mvn spring-boot:run  # 端口 8083

cd rental-notify
mvn spring-boot:run  # 端口 8084
```

#### 方式二：Docker运行（推荐）

```bash
# 1. 确保已安装 Docker 和 Docker Compose

# 2. 克隆项目
git clone -b feature/phase1 https://github.com/a187898447/house-rental-manager.git
cd house-rental-manager

# 3. 启动所有服务
docker-compose -f deploy/docker-compose.yml up -d

# 4. 查看服务状态
docker-compose -f deploy/docker-compose.yml ps

# 5. 查看日志
docker-compose -f deploy/docker-compose.yml logs -f
```

### 1.2 前端启动

```bash
# 1. 确保已安装 Node.js 18+ 和 pnpm/npm
node -v  # 应显示 18.x 或更高
pnpm -v  # 或 npm -v

# 2. 安装依赖
cd house-rental-manager/frontend
pnpm install  # 或 npm install

# 3. 启动开发服务器
pnpm dev  # 或 npm run dev

# 4. 微信小程序开发
pnpm dev:mp-weixin
```

---

## 2. 环境要求

### 2.1 后端环境

| 组件 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 17+ | 必须，Spring Boot 3.x 要求 |
| Maven | 3.8+ | 编译工具 |
| Nacos | 2.2.x | 服务注册与配置中心 |
| Redis | 7.x | 缓存与Session存储 |
| 达梦数据库 DM8 | - | 业务数据库 |

### 2.2 前端环境

| 组件 | 版本要求 | 说明 |
|------|----------|------|
| Node.js | 18+ | 运行前端开发服务器 |
| pnpm | 8.x | 包管理器（推荐） |
| 微信开发者工具 | 最新版 | 预览小程序 |

---

## 3. 测试流程

### 3.1 后端API测试

#### 3.1.1 用户认证

```bash
# 微信登录
curl -X POST http://localhost:8081/user/login \
  -H "Content-Type: application/json" \
  -d '{"code":"wx_test_code"}'

# 响应示例
# {"code":200,"message":"success","data":{"token":"xxx","userId":1}}
```

#### 3.1.2 房源管理（需登录）

```bash
# 获取Token后设置
TOKEN="your_jwt_token"

# 获取房源列表
curl -X GET "http://localhost:8082/property/list?page=1&size=10" \
  -H "Authorization: Bearer $TOKEN"

# 新增房源
curl -X POST http://localhost:8082/property \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "roomNumber":"101",
    "rentAmount":2000,
    "area":80,
    "depositAmount":4000
  }'
```

#### 3.1.3 租客预约（无需登录）

```bash
# 公开房源列表
curl http://localhost:8082/tenant/property/list

# 预约看房
curl -X POST http://localhost:8083/tenant/appointment \
  -H "Content-Type: application/json" \
  -d '{
    "propertyId":1,
    "tenantName":"张三",
    "tenantPhone":"13800138000",
    "appointmentDate":"2024-01-15T10:00:00"
  }'
```

#### 3.1.4 合同管理

```bash
# 创建合同（房东）
curl -X POST http://localhost:8083/contract \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "tenantId":1,
    "propertyId":1,
    "rentAmount":2000,
    "depositAmount":4000,
    "startDate":"2024-01-01",
    "endDate":"2024-12-31"
  }'

# 签署合同（租客）
curl -X POST http://localhost:8083/contract/1/sign?signUrl=https://xxx.com/sign.png \
  -H "Authorization: Bearer $TENANT_TOKEN"
```

#### 3.1.5 账单管理

```bash
# 创建账单（房东）
curl -X POST http://localhost:8083/rent/bills \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "tenantId":1,
    "propertyId":1,
    "amount":2000,
    "payMonth":"2024-01"
  }'

# 标记支付（房东确认）
curl -X POST http://localhost:8083/rent/bills/1/pay \
  -H "Authorization: Bearer $TOKEN"

# 发送催租提醒
curl -X POST http://localhost:8083/rent/bills/1/remind \
  -H "Authorization: Bearer $TOKEN"
```

#### 3.1.6 报修管理

```bash
# 提交报修（租客）
curl -X POST http://localhost:8083/repair \
  -H "Content-Type: application/json" \
  -d '{
    "propertyId":1,
    "title":"水龙头漏水",
    "description":"厨房水龙头漏水严重",
    "contactPhone":"13800138000"
  }'

# 开始处理（房东）
curl -X POST http://localhost:8083/repair/1/process \
  -H "Authorization: Bearer $TOKEN"

# 完成处理（房东）
curl -X POST "http://localhost:8083/repair/1/complete?remark=已修好" \
  -H "Authorization: Bearer $TOKEN"
```

#### 3.1.7 通知消息

```bash
# 通知列表
curl -X GET "http://localhost:8084/notify/list?page=1&size=10" \
  -H "Authorization: Bearer $TOKEN"

# 未读数量
curl http://localhost:8084/notify/unread-count \
  -H "Authorization: Bearer $TOKEN"

# 标记已读
curl -X PUT http://localhost:8084/notify/1/read \
  -H "Authorization: Bearer $TOKEN"
```

### 3.2 前端测试

1. 启动前端开发服务器
2. 使用微信开发者工具扫描二维码
3. 测试以下流程：
   - [ ] 微信登录
   - [ ] 房东：房源发布、编辑、删除
   - [ ] 租客：浏览房源、预约看房
   - [ ] 合同：创建、签署、查看
   - [ ] 账单：查看、支付（模拟）
   - [ ] 报修：提交、处理、查看
   - [ ] 通知：接收、查看

---

## 4. 注意事项

### 4.1 常见问题

| 问题 | 原因 | 解决方案 |
|------|------|----------|
| 服务启动失败端口被占用 | 端口冲突 | 检查并释放8080-8085端口 |
| 数据库连接失败 | Nacos/数据库未启动 | 确保依赖服务已启动 |
| Token验证失败 | Token过期或格式错误 | 重新登录获取Token |
| 前端请求失败 | 跨域问题/网关未启动 | 检查网关8080端口 |

### 4.2 数据库初始化

首次启动需要执行SQL初始化脚本：

```bash
# 连接到达梦数据库
./dmdb/bin/disql SYSdba/SYSDBA@localhost:5236

# 执行初始化SQL
SQL> start init.sql
```

### 4.3 Nacos配置

Nacos需要配置以下配置项：

```yaml
# 公共配置
spring:
  redis:
    host: localhost
    port: 6379
  datasource:
    url: jdbc:dm://localhost:5236/rental
    driver: dm.jdbc.driver.DmDriver
```

### 4.4 定时任务说明

以下定时任务已配置：

| 任务 | 表达式 | 功能 |
|------|--------|------|
| generateMonthlyBills | 0 0 1 * * ? | 每月1日生成账单 |
| checkOverdueBills | 0 0 2 * * ? | 每日检查逾期账单 |
| sendPaymentReminder | 0 0 3 * * ? | 每周发送催租提醒 |

---

## 5. API文档

各服务启动后可访问Swagger文档：

- 网关: http://localhost:8080
- 用户服务: http://localhost:8081/doc.html
- 房源服务: http://localhost:8082/doc.html
- 账单服务: http://localhost:8083/doc.html
- 通知服务: http://localhost:8084/doc.html

---

## 6. 联系与支持

测试过程中如有问题，请提交Issue到GitHub仓库。

---

*文档版本: 1.0*  
*最后更新: 2024-03-22*
