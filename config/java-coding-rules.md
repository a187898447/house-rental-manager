# Java 编码规范

## 1. 命名规范

### 类和接口
- PascalCase: `UserService`, `PropertyController`

### 方法和变量
- camelCase: `findById`, `totalAmount`
- 参数: `userId`, `propertyName`

### 常量
- UPPER_SNAKE_CASE: `MAX_PAGE_SIZE`

## 2. 不可变性

```java
// 推荐 record
public record UserDTO(Long id, String name) {}

// 字段 final
private final Long id;
```

## 3. Optional

```java
Optional<Property> findById(Long id);
return propertyRepo.findById(id)
    .map(PropertyVO::from)
    .orElseThrow(() -> new PropertyNotFoundException(id));
```

## 4. Stream

```java
List<String> names = properties.stream()
    .map(Property::getName)
    .filter(Objects::nonNull)
    .toList();
```

## 5. 异常

```java
throw new PropertyNotFoundException(propertyId);
```

## 6. 日志

```java
log.info("create_property id={}", propertyId);
log.error("failed id={}", id, ex);
```

## 7. 项目结构

```
controller/ service/impl/ mapper/ entity/ dto/ vo/
```

## 8. 禁止

- ❌ 长参数列表 → 使用 DTO
- ❌ 深嵌套 → 早期返回
- ❌ 魔法数字 → 常量
- ❌ 空catch块