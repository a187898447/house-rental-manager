# 租房小程序集成测试操作手册

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

### 2.1 方式一：Docker 一键启动（推荐）

```bash
# 1. 克隆项目
git clone https://github.com/a187898447/house-rental-manager.git
cd house-rental-manager

# 2. 启动所有服务
docker-compose -f deploy/docker-compose.yml up -d

# 3. 查看服务状态
docker-compose -f deploy/docker-compose.yml ps

# 4. 查看日志
docker-compose -f deploy/docker-compose.yml logs -f
```

**启动的服务：**
- MySQL (3306)
- Redis (6379)
- Nacos (8848)
- rental-gateway (8080)
- rental-user (8081)
- rental-property (8082)
- rental-bill (8083)
- rental-notify (8084)

### 2.2 方式二：本地手动启动

#### 2.2.1 启动中间件

```bash
# MySQL
docker run -d --name mysql \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root123 \
  -e MYSQL_DATABASE=rental \
  mysql:8.0

# Redis
docker run -d --name redis \
  -p 6379:6379 \
  redis:7-alpine

# Nacos
docker run -d --name nacos \
  -p 8848:8848 \
  -p 9848:9848 \
  -e MODE=standalone \
  nacos/nacos-server:v2.2.3
```

#### 2.2.2 编译后端

```bash
cd house-rental-manager
mvn clean package -DskipTests
```

#### 2.2.3 启动后端服务

```bash
# 按顺序启动
java -jar rental-gateway/target/rental-gateway.jar
java -jar rental-user/target/rental-user.jar
java -jar rental-property/target/rental-property.jar
java -jar rental-bill/target/rental-bill.jar
java -jar rental-notify/target/rental-notify.jar
```

#### 2.2.4 启动前端

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

## 3. 测试流程

### 3.1 API 测试清单

| 模块 | API 路径 | 方法 | 说明 |
|------|----------|------|------|
| 用户 | /api/user/login | POST | 微信登录 |
| 用户 | /api/user/bind-phone | POST | 绑定手机号 |
| 用户 | /api/user/info | GET | 获取用户信息 |
| 房源 | /property/list | GET | 房源列表 |
| 房源 | /property/{id} | GET | 房源详情 |
| 房源 | /property | POST | 新增房源 |
| 房源 | /property/{id} | PUT | 更新房源 |
| 房源 | /property/{id} | DELETE | 删除房源 |
| 预约 | /api/appointment | POST | 创建预约 |
| 预约 | /api/appointment/my | GET | 我的预约 |
| 预约 | /api/appointment/{id}/confirm | PUT | 确认预约 |
| 合同 | /api/contract | POST | 创建合同 |
| 合同 | /api/contract/{id}/sign | PUT | 签署合同 |
| 账单 | /api/rent/bills | GET | 账单列表 |
| 账单 | /api/rent/bills/{id}/pay | POST | 支付账单 |
| 报修 | /api/repairs | POST | 提交报修 |
| 报修 | /api/repairs/{id}/status | PUT | 更新状态 |
| 通知 | /api/notification/list | GET | 通知列表 |

### 3.2 测试用例

#### 3.2.1 用户认证流程
1. 调用微信登录 API 获取 token
2. 使用 token 调用用户信息 API
3. 验证 token 有效性

#### 3.2.2 房源管理流程
1. 房东新增房源
2. 查询房源列表
3. 查看房源详情
4. 编辑房源信息
5. 删除房源

#### 3.2.3 预约看房流程
1. 租客浏览公开房源
2. 租客创建预约
3. 房东查看预约列表
4. 房东确认/拒绝预约

#### 3.2.4 账单支付流程
1. 房东生成月度账单
2. 租客查看账单
3. 租客发起支付
4. 房东确认收款

#### 3.2.5 报修流程
1. 租客提交报修
2. 房东查看报修列表
3. 房东处理报修
4. 租客查看进度

### 3.3 Swagger API 文档

服务启动后访问：
- 网关: http://localhost:8080
- 用户服务: http://localhost:8081/doc.html
- 房源服务: http://localhost:8082/doc.html
- 账单服务: http://localhost:8083/doc.html
- 通知服务: http://localhost:8084/doc.html

---

## 4. 注意事项

### 4.1 常见问题

#### Q1: MySQL 连接失败
**解决：** 检查 MySQL 是否启动，确认端口 3306 是否被占用

#### Q2: Nacos 无法启动
**解决：** 确保 8848 端口未被占用，查看 Nacos 日志排查问题

#### Q3: 前端请求后端失败
**解决：** 
1. 检查后端服务是否全部启动
2. 检查网关配置是否正确
3. 确认前端 API baseURL 配置

#### Q4: Token 过期
**解决：** 重新调用登录 API 获取新 token

### 4.2 配置说明

#### 4.2.1 Nacos 配置
- 访问地址: http://localhost:8848/nacos
- 默认账号: nacos / nacos

#### 4.2.2 数据库配置
在 Nacos 中配置以下数据源：
- MySQL 连接信息
- Redis 连接信息

#### 4.2.3 前端 API 配置
修改 `frontend/src/services/index.ts` 中的 baseURL：
```typescript
const baseURL = 'http://localhost:8080'
```

---

## 5. 测试检查清单

- [ ] 后端服务全部启动成功
- [ ] MySQL/Redis/Nacos 连接正常
- [ ] 用户登录功能正常
- [ ] 房源 CRUD 功能正常
- [ ] 预约看房功能正常
- [ ] 合同管理功能正常
- [ ] 账单支付功能正常
- [ ] 报修管理功能正常
- [ ] 通知推送功能正常
- [ ] 前端页面展示正常

---

## 6. 联系支持

如有疑问，请联系开发团队：
- 后端: @backend-dev
- 前端: @frontend-dev
- 项目管理: @manager
