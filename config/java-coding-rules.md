# Java 开发规范 (阿里规约)

## 命名规范

### 包命名
- 全部小写
- com.rental.模块名

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

### 行的长度
- 单行不超过 120 字符

## 注释规范

### 类注释
```java
/**
 * 类描述
 * @author 作者
 * @date 创建日期
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

## 集合规范

### 1. 初始化集合
```java
List<String> list = new ArrayList<>();
```

### 2. 遍历集合
```java
for (String item : list) { }
```

## 日志规范

### 1. 日志格式
```java
log.info("用户登录成功: userId={}", userId);
log.error("处理异常: error={}", e.getMessage(), e);
```

## 异常规范

### 1. 捕获具体异常
```java
try {
    // code
} catch (BusinessException e) {
    // 业务异常
} catch (Exception e) {
    // 其他异常
}
```

## RESTful API 规范

### 1. URL 命名
- 小写字母 + 下划线
- /api/user/login

### 2. 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```