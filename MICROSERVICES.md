# 微服务架构说明

## 架构概述

本项目采用 **Spring Cloud Alibaba** 微服务架构，包含以下核心组件：

```
┌─────────────────────────────────────────────────────────┐
│                    UniApp 前端                           │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│              Spring Cloud Gateway (8080)                │
│              - JWT 认证                                  │
│              - 路由转发                                  │
│              - 限流熔断 (Sentinel)                       │
└────┬─────────────┬──────────────┬──────────────────────┘
     │             │              │
     ▼             ▼              ▼
┌─────────┐  ┌──────────┐  ┌──────────┐
│rental-  │  │rental-   │  │rental-   │
│user     │  │property  │  │bill      │
│(8081)   │  │(8082)    │  │(8083)    │
│         │  │          │  │          │
│- 用户   │  │- 房源    │  │- 租金    │
│- 租客   │  │- 楼栋    │  │- 押金    │
│         │  │          │  │- 水电    │
└────┬────┘  └────┬─────┘  └────┬─────┘
     │           │             │
     └───────────┼─────────────┘
                 ▼
     ┌───────────────────────┐
     │   Nacos (8848)        │
     │   - 服务注册发现       │
     │   - 配置中心           │
     └───────────────────────┘
                 │
     ┌───────────┴───────────┐
     ▼                       ▼
┌─────────┐           ┌──────────┐
│ Sentinel│           │  MySQL   │
│ (8080)  │           │  (3306)  │
│ 熔断限流 │           │  数据库  │
└─────────┘           └──────────┘
```

## 服务列表

| 服务名 | 端口 | 功能 |
|--------|------|------|
| rental-gateway | 8080 | API 网关（认证、路由、限流） |
| rental-user | 8081 | 用户服务（登录、租客管理） |
| rental-property | 8082 | 房源服务（房源 CRUD） |
| rental-bill | 8083 | 账单服务（租金、押金、水电） |
| rental-notify | 8084 | 通知服务（消息推送） |
| rental-pay | 8085 | 支付服务（微信支付） |

## 核心组件

### 1. Spring Cloud Gateway

**功能**:
- JWT 令牌认证
- 动态路由转发
- 请求限流（基于 IP）
- 熔断降级（Sentinel）

**配置**: `rental-gateway/src/main/resources/application.yml`

### 2. Nacos（服务注册与配置中心）

**功能**:
- 服务注册与发现
- 动态配置管理
- 服务健康检查

**启动命令**:
```bash
docker run -d \
  -p 8848:8848 \
  -e MODE=standalone \
  -e SPRING_DATASOURCE_PLATFORM=mysql \
  -e MYSQL_SERVICE_HOST=localhost \
  -e MYSQL_SERVICE_PORT=3306 \
  -e MYSQL_SERVICE_DB_NAME=nacos_config \
  -e MYSQL_SERVICE_USER=root \
  -e MYSQL_SERVICE_PASSWORD=root123 \
  nacos/nacos-server:2.2.0
```

### 3. Sentinel（熔断限流）

**功能**:
- 流量控制（QPS 限流）
- 熔断降级（错误率/慢调用）
- 系统自适应保护

**配置方式**:
- 通过 Nacos 配置中心动态下发规则
- Sentinel Dashboard 可视化配置

**限流规则示例**（Nacos 配置）:
```json
[
  {
    "resource": "/api/user/wx-login",
    "limitApp": "default",
    "grade": 1,
    "count": 100,
    "strategy": 0,
    "controlBehavior": 0
  }
]
```

### 4. OpenFeign（服务间调用）

**功能**:
- 声明式 HTTP 客户端
- 负载均衡
- 熔断降级

**使用示例**:
```java
@FeignClient(name = "rental-property", fallback = PropertyClientFallback.class)
public interface PropertyClient {
    @GetMapping("/api/property/{id}")
    Result<PropertyVO> getPropertyDetail(@PathVariable("id") Long id);
}
```

### 5. JWT 认证

**流程**:
1. 用户登录 → 用户服务生成 Token
2. 网关拦截请求 → 验证 Token 有效性
3. 验证通过 → 传递用户 ID 到下游服务
4. 验证失败 → 返回 401 未授权

**Token 格式**:
```
Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 快速开始

### 1. 启动基础设施

```bash
# MySQL
docker run -d -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root123 \
  -e MYSQL_DATABASE=rental_db \
  mysql:8.0

# Nacos
docker run -d -p 8848:8848 \
  -e MODE=standalone \
  nacos/nacos-server:2.2.0

# Sentinel Dashboard
java -jar sentinel-dashboard-1.8.6.jar \
  -Dserver.port=8080 \
  -Dcsp.sentinel.dashboard.server=localhost:8080
```

### 2. 初始化数据库

```bash
mysql -u root -p < db/schema.sql
```

### 3. 启动微服务

```bash
# 网关
cd rental-gateway && mvn spring-boot:run

# 用户服务
cd rental-user && mvn spring-boot:run

# 房源服务
cd rental-property && mvn spring-boot:run

# 账单服务
cd rental-bill && mvn spring-boot:run
```

### 4. 验证服务注册

访问 Nacos 控制台：http://localhost:8848/nacos

确认所有服务状态为 **健康**。

### 5. 测试 API

```bash
# 微信登录
curl -X POST http://localhost:8080/api/user/wx-login \
  -H "Content-Type: application/json" \
  -d '{"openid":"test123"}'

# 获取房源列表（需要 Token）
curl -X GET "http://localhost:8080/api/property/list?landlordId=1000000001" \
  -H "Authorization: Bearer <token>"
```

## 配置说明

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| NACOS_SERVER_ADDR | Nacos 服务器地址 | localhost:8848 |
| NACOS_USERNAME | Nacos 用户名 | nacos |
| NACOS_PASSWORD | Nacos 密码 | nacos |
| SENTINEL_DASHBOARD | Sentinel 控制台地址 | localhost:8080 |

### 应用配置

每个微服务的 `application.yml` 包含：
- 服务端口
- Nacos 注册中心配置
- Sentinel 熔断配置
- 数据库连接
- MyBatis-Plus 配置

## 服务间调用

### Feign 远程调用

```java
// 1. 定义 Feign 客户端
@FeignClient(name = "rental-property")
public interface PropertyClient {
    @GetMapping("/api/property/{id}")
    Result<PropertyVO> getPropertyDetail(@PathVariable Long id);
}

// 2. 注入并使用
@Service
public class TenantServiceImpl {
    @Autowired
    private PropertyClient propertyClient;
    
    public void checkIn(TenantDTO dto) {
        // 调用房源服务
        Result<PropertyVO> result = propertyClient.getPropertyDetail(dto.getPropertyId());
        // ...
    }
}
```

### 负载均衡

通过 Spring Cloud LoadBalancer 自动实现：
- 轮询策略
- 随机策略
- 权重策略

## 熔断降级

### Sentinel 规则配置

通过 Nacos 配置中心下发规则：

**DataId**: `rental-user-sentinel`  
**Group**: `DEFAULT_GROUP`  
**配置类型**: `flow`（流控规则）

```json
[
  {
    "resource": "GET:/api/user/info",
    "limitApp": "default",
    "grade": 1,
    "count": 50,
    "strategy": 0,
    "controlBehavior": 0,
    "clusterMode": false
  }
]
```

### 降级处理

```java
@Component
public class PropertyClientFallback implements PropertyClient {
    @Override
    public Result<PropertyVO> getPropertyDetail(Long id) {
        // 降级逻辑：返回缓存数据或友好提示
        return Result.error(503, "房源服务暂时不可用");
    }
}
```

## 链路追踪（可选）

集成 SkyWalking 实现分布式链路追踪：

```bash
# 启动 SkyWalking OAP
docker run -d apache/skywalking-oap:9.0.0

# 启动 SkyWalking UI
docker run -d -p 8088:8080 apache/skywalking-ui:9.0.0
```

在微服务中添加探针：
```bash
java -javaagent:/path/to/skywalking-agent.jar \
     -Dskywalking.agent.service_name=rental-user \
     -Dskywalking.collector.backend_service=localhost:11800 \
     -jar rental-user.jar
```

## 部署建议

### 开发环境
- 单机部署所有服务
- 使用 Docker Compose 编排

### 生产环境
- Kubernetes 容器编排
- Nacos 集群（3 节点）
- Sentinel 集群
- MySQL 主从复制
- Redis 集群（限流用）

## 监控告警

### Prometheus + Grafana

1. 添加 Prometheus 依赖
2. 暴露 `/actuator/prometheus` 端点
3. Grafana 配置数据源
4. 配置告警规则

### 健康检查

```bash
# 检查服务健康状态
curl http://localhost:8081/actuator/health

# 检查数据库连接
curl http://localhost:8081/actuator/health/db
```

## 安全加固

1. **JWT Token 过期时间**: 2 小时
2. **API 限流**: 单 IP 100 QPS
3. **敏感接口**: 短信验证码、支付接口单独限流
4. **SQL 注入**: MyBatis-Plus 预编译
5. **XSS 防护**: 网关层过滤特殊字符

## 参考文档

- [Spring Cloud Alibaba 官方文档](https://spring-cloud-alibaba-group.github.io/github-pages/hoxton/zh-cn/index.html)
- [Nacos 官方文档](https://nacos.io/zh-cn/docs/quick-start.html)
- [Sentinel 官方文档](https://sentinelguard.io/zh-cn/docs/basic-implementation.html)
- [Spring Cloud Gateway 官方文档](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)
