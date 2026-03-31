# 登录角色选择功能

## 为什么

当前登录页面缺少角色选择逻辑，无法区分房东和住户身份。需要增加角色选择并对应不同权限的登录方式。

## 需求变更

- 登录页增加房东/住户角色选择
- 根据角色调用对应登录接口
- 虚拟账户机制：测试时自动创建虚拟账户

## Capabilities

- **New Capabilities**:
  - login-role-select: 登录角色选择组件
  - virtual-account: 虚拟账户自动创建

## 影响

- 前端: login/index.vue, user store
- 后端: AuthController, User entity, 权限验证

## 实现方式

1. 前端登录页增加角色切换 Tab（房东/住户）
2. 后端虚拟账户：登录时检查用户是否存在，不存在则自动创建
3. 角色权限：通过 ownerId 区分房东，通过 tenantId 区分住户