# Java 开发规范 (阿里规约)

## 命名规范

### 包命名
- 全部小写
-  com.rental.模块名

### 类命名
- UpperCamelCase
- 例：UserController, RentServiceImpl

### 方法命名
- lowerCamelCase
- 例：getUserInfo(), saveProperty()

### 常量命名
- UPPER_SNAKE_CASE
- 例：MAX_RETRY_COUNT

## 代码格式

### 缩进
- 4 空格缩进
- 不使用 Tab

### 行的长度
- 单行不超过 120 字符

### 空格
- 运算符前后空格
- 逗号后空格

## 注释规范

### 类注释
```java
/**
 * 类描述
 * @author 作者
 * @date 创建日期
 */
```

### 方法注释
```java
/**
 * 方法描述
 * @param 参数说明
 * @return 返回值说明
 */
```

## OOP 规范

### 1. 避免魔法值
```java
// 禁止
if (status == 1)

// 允许
private static final int STATUS_ACTIVE = 1;
if (status == STATUS_ACTIVE)
```

### 2. 类的单一职责
- 一个类只负责一项职责

### 3. 接口幂等性
- 多次调用结果一致

## 集合规范

### 1. 初始化集合
```java
// 推荐
List<String> list = new ArrayList<>();

// 不推荐
List<String> list = new LinkedList<>();
```

### 2. 遍历集合
```java
// 推荐
for (String item : list) { }

// 不推荐
for (int i = 0; i < list.size(); i++)
```

## 并发规范

### 1. 线程安全
- 多线程访问共享资源需同步
- 使用线程安全集合

### 2. 线程池
- 禁止使用 Executors 创建线程池
- 使用 ThreadPoolExecutor

## 日志规范

### 1. 日志级别
- DEBUG: 调试信息
- INFO: 正常流程
- WARN: 警告
- ERROR: 错误

### 2. 日志格式
```java
log.info("用户登录成功: userId={}", userId);
log.error("处理异常: error={}", e.getMessage(), e);
```

## 异常规范

### 1. 捕获具体异常
```java
// 推荐
try {
    // code
} catch (BusinessException e) {
    // 业务异常
} catch (Exception e) {
    // 其他异常
}

// 不推荐
try {
    // code
} catch (Exception e) {
    // 捕获所有异常
}
```

### 2. 不吞掉异常
- 至少记录日志
- 必要时抛出

## 数据库规范

### 1. 表命名
- 小写字母 + 下划线
- 例：rent_record, user_info

### 2. 字段命名
- 同表命名规范

### 3. 索引
- 区分度高字段建索引
- 避免全表扫描

## VO/DTO/BO 规范

### 命名
- VO: View Object (视图对象)
- DTO: Data Transfer Object (数据传输对象)
- BO: Business Object (业务对象)

### 命名格式
- XxxVO, XxxDTO, XxxBO

## RESTful API 规范

### 1. URL 命名
- 小写字母 + 下划线
- 例：/api/user/login

### 2. HTTP 方法
- GET: 查询
- POST: 创建
- PUT: 更新
- DELETE: 删除

### 3. 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

## 安全规范

### 1. 敏感信息
- 密码不返回前端
- 敏感数据加密存储

### 2. SQL 注入
- 使用预编译语句
- 禁止字符串拼接 SQL