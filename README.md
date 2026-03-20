# 租房小程序后端

基于 Spring Boot + Spring Cloud Alibaba 的微服务后端

## 技术栈

- Spring Boot 3.2.x + Java 17
- Spring Cloud Alibaba (Nacos)
- 达梦数据库 DM8
- MyBatis-Plus
- Redis

## 模块

- rental-gateway (8080) - API网关
- rental-user (8081) - 用户服务
- rental-property (8082) - 房源服务
- rental-bill (8083) - 账单服务
- rental-notify (8084) - 通知服务
- rental-pay (8085) - 支付服务(预留)

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- Nacos 2.2.x
- Redis 7.x
- 达梦数据库 DM8

### 编译

```bash
mvn clean package -DskipTests
```

### 启动

按顺序启动各服务：

```bash
# 启动网关
java -jar rental-gateway/target/rental-gateway.jar

# 启动用户服务
java -jar rental-user/target/rental-user.jar

# 启动房源服务
java -jar rental-property/target/rental-property.jar

# 启动账单服务
java -jar rental-bill/target/rental-bill.jar

# 启动通知服务
java -jar rental-notify/target/rental-notify.jar
```

## API 文档

各服务启动后访问：

- 网关: http://localhost:8080
- 用户服务: http://localhost:8081/doc.html
- 房源服务: http://localhost:8082/doc.html
- 账单服务: http://localhost:8083/doc.html
- 通知服务: http://localhost:8084/doc.html

## 开发

```bash
# 运行单个模块
cd rental-user
mvn spring-boot:run
```

## License

MIT
