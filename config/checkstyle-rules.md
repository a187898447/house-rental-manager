# Java 代码风格规则（阿里开发手册风格）

## 1. 文件结构

| 规则 | 值 | 说明 |
|------|-----|------|
| 文件最大行数 | 2000 | 超过则拆分 |
| 换行符 | LF | Unix风格 |
| 行尾空格 | 禁止 | 自动移除 |

## 2. 命名规范

### 2.1 包名

```java
// ✅ 正确
package com.rental.property.service;

// ❌ 错误
package com.rental.Property.Service;
```

格式：`^[a-z]+(\.[a-z][a-z0-9]*)*$`

### 2.2 类名/接口名

- PascalCase: `UserService`, `PropertyController`
- 前缀约定：Controller/Service/Mapper/Entity/DTO/VO/Exception

### 2.3 方法名

- camelCase: `findById`, `getPropertyList`
- 布尔方法: `isActive`, `hasPermission`, `canEdit`

### 2.4 变量名

```java
// ✅ 正确
private final Long userId;
private String propertyName;
private int totalCount;

// ❌ 错误
private Long UserId;
private String m_name;
```

格式：`^[a-z][a-z0-9]*$`

### 2.5 常量

```java
// ✅ 正确
private static final int MAX_PAGE_SIZE = 100;
private static final String DEFAULT_STATUS = "active";

// ❌ 错误
private static final int maxPageSize = 100;
```

格式：`^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$`

## 3. 格式规则

### 3.1 行长度

- 最大 **120 字符**
- 超长时换行

### 3.2 缩进

- 基础缩进：**4 空格**
- case 缩进：4 空格

### 3.3 大括号

```java
// ✅ 推荐：同行
if (condition) {
    doSomething();
}

// ❌ 避免
if (condition)
{
    doSomething();
}
```

### 3.4 空格

- 运算符周围：`a + b`, `if (x > 0)`
- 关键词后：`if (condition)`, `for (int i = 0)`
- 禁止：`a+b`（无空格）

## 4. 注解规则

### 4.1 注解位置

- 单行注解：同行
- 多行注解：新行

```java
// ✅ 正确
@Override
public void method() { }

// ❌ 错误
@ Override
public void method() { }
```

### 4.2 使用@Override

所有重写方法**必须**添加 `@Override` 注解

## 5. 代码结构

### 5.1 修饰符顺序

```java
public static final String CONSTANT = "value";
private static final int MAX = 100;
private Long id;
```

顺序：public → protected → private → static → final

### 5.2 变量声明

```java
// ✅ 每个变量单独声明
int a = 1;
int b = 2;

// ❌ 避免
int a = 1, b = 2;
```

### 5.3 空块

```java
// ✅ 正确
if (condition) {
    doSomething();
}

// ❌ 禁止空块
if (condition) {
}
```

## 6. 控制结构

### 6.1 switch 必须有default

```java
switch (status) {
    case 1:
        doOne();
        break;
    case 2:
        doTwo();
        break;
    default:
        doDefault();
}
```

### 6.2 循环穿透禁止

```java
// ✅ 正确
switch (type) {
    case 1:
        handleType1();
        break;  // 有break
    case 2:
        handleType2();
        break;
}
```

## 7. 复杂度限制

| 指标 | 最大值 | 说明 |
|------|--------|------|
| 圈复杂度 | 15 | 方法内分支数 |
| NPath复杂度 | 200 | 路径数 |

### 降低复杂度的方法

- 提取方法
- 使用策略模式
- 简化条件表达式

## 8. 禁止的做法

| 禁止 | 替代方案 |
|------|----------|
| 魔法数字 | 使用常量 `MAX_RETRY = 3` |
| 空catch块 | 记录日志或重新抛出 |
| 深层嵌套 (>3层) | 早期返回 |
| 过长方法 (>80行) | 拆分方法 |
| 过长发参数 (>5个) | 使用DTO |
| 可变静态状态 | 依赖注入 |
| 内部赋值 | 返回值 |

## 9. 日志规范

```java
private static final Logger log = LoggerFactory.getLogger(ClassName.class);

// ✅ 正确
log.info("create_property id={}", propertyId);
log.error("failed id={}", id, ex);

// ❌ 错误
log.info("create property " + id);
log.info("create property " + id + ", name=" + name);
```

## 10. Maven/IDEA 集成

### 在 pom.xml 中添加 Checkstyle 插件

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.2.0</version>
    <configuration>
        <configLocation>config/checkstyle.xml</configLocation>
    </configuration>
</plugin>
```

### 运行检查

```bash
mvn checkstyle:check
```

### IDEA 配置

1. Settings → Editor → Code Style → Java
2. Import `config/checkstyle.xml` 作为_scheme_