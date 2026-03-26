# 租房小程序 Docker 部署配置

## 快速启动

```bash
cd deploy
docker-compose up -d
```

## 服务端口

| 服务 | 端口 |
|------|------|
| Gateway | 8080 |
| User Service | 8081 |
| Property Service | 8082 |
| Bill Service | 8083 |
| Notify Service | 8084 |
| Pay Service | 8085 |
| Nacos | 8848 |
| Redis | 6379 |
| MySQL | 3306 |

## 环境要求

- Docker 20.10+
- Docker Compose 2.0+

## 首次启动

1. 启动基础服务
```bash
docker-compose up -d mysql nacos redis
```

2. 等待 Nacos 就绪（约30秒）
```bash
curl http://localhost:8848/nacos
```

3. 启动微服务
```bash
docker-compose up -d rental-gateway rental-user rental-property rental-bill rental-notify rental-pay
```

## 验证服务

```bash
# 检查容器运行状态
docker ps

# 测试 Gateway
curl http://localhost:8080/actuator/health

# 测试房源服务
curl http://localhost:8082/actuator/health
```

## 停止服务

```bash
docker-compose down
```

## 数据持久化

数据存储在 Docker volumes 中：
- `mysql-data` - MySQL 数据
- `redis-data` - Redis 数据

## 注意事项

- 演示模式：使用 `demo-token` 作为 Authorization Header 即可访问所有 API
- 默认房东 ID 为 1，用于数据隔离测试
