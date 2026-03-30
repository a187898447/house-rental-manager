# Phase 1 任务清单

## 工作流程

每个任务遵循以下步骤：
1. **验证** - 检查功能是否已有代码实现
2. **判断** - 已有代码 → 验证优化；无代码 → 实现
3. **执行** - 根据判断结果执行

---

## Task-1: property-management - 房源管理

### Step 1: 验证

```bash
# 检查房源实体
ls rental-property/src/main/java/com/rental/property/entity/Property.java

# 检查房源服务
ls rental-property/src/main/java/com/rental/property/service/PropertyService.java

# 检查房源控制器
ls rental-property/src/main/java/com/rental/property/controller/PropertyController.java
```

### Step 2: 判断

| 检查项 | 预期文件 | 状态 |
|--------|----------|------|
| Property实体 | Property.java | ✅ |
| PropertyService | PropertyService.java | ✅ |
| PropertyController | PropertyController.java | ✅ |

### Step 3: 结果

**状态**: ✅ 已实现 → 验证通过

---

## Task-2: rent-management - 租金管理

### Step 1: 验证

```bash
# 检查租金记录实体
ls rental-bill/src/main/java/com/rental/bill/entity/RentRecord.java

# 检查租金服务
ls rental-bill/src/main/java/com/rental/bill/service/RentRecordService.java

# 检查租金控制器
ls rental-bill/src/main/java/com/rental/bill/controller/RentRecordController.java
```

### Step 2: 判断

| 检查项 | 预期文件 | 状态 |
|--------|----------|------|
| RentRecord实体 | RentRecord.java | ✅ |
| RentRecordService | RentRecordService.java | ✅ |
| RentRecordController | RentRecordController.java | ✅ |

### Step 3: 结果

**状态**: ✅ 已实现 → 验证通过

---

## Task-3: tenant-management - 租客管理

### Step 1: 验证

```bash
# 检查租客实体
ls rental-bill/src/main/java/com/rental/bill/entity/Tenant.java

# 检查租客服务
ls rental-bill/src/main/java/com/rental/bill/service/TenantService.java

# 检查租客控制器
ls rental-bill/src/main/java/com/rental/bill/controller/TenantController.java
```

### Step 2: 判断

| 检查项 | 预期文件 | 状态 |
|--------|----------|------|
| Tenant实体 | Tenant.java | ✅ |
| TenantService | TenantService.java | ✅ |
| TenantController | TenantController.java | ✅ |

### Step 3: 结果

**状态**: ✅ 已实现 → 验证通过

---

## Task-4: wechat-login - 微信登录

### Step 1: 验证

```bash
# 检查微信登录相关代码
find . -name "*.java" -exec grep -l "wechat\|weixin" {} \; | head -10
```

### Step 2: 判断

| 检查项 | 预期 | 状态 |
|--------|------|------|
| 微信登录 | 无 | ⚠️ 暂未实现 |

### Step 3: 结果

**状态**: ⚠️ 暂不实现（Phase 1 使用账号密码登录）

---

## 验证命令汇总

```bash
# Phase 1 完整验证
cd /root/hiclaw-fs/shared/house-rental-manager

# 房源管理
ls rental-property/src/main/java/com/rental/property/entity/
ls rental-property/src/main/java/com/rental/property/service/
ls rental-property/src/main/java/com/rental/property/controller/

# 租金管理
ls rental-bill/src/main/java/com/rental/bill/entity/RentRecord.java
ls rental-bill/src/main/java/com/rental/bill/service/RentRecordService.java
ls rental-bill/src/main/java/com/rental/bill/controller/RentRecordController.java

# 租客管理
ls rental-bill/src/main/java/com/rental/bill/entity/Tenant.java
ls rental-bill/src/main/java/com/rental/bill/service/TenantService.java
ls rental-bill/src/main/java/com/rental/bill/controller/TenantController.java
```