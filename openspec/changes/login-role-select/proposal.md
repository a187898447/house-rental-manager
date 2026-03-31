## Why

当前登录页面缺少角色选择功能，无法区分房东和住户身份。系统需要支持两种角色的独立登录流程，并提供虚拟账户供测试使用。

## What Changes

- 登录页增加房东/住户角色选择 Tab
- 手机号+验证码登录方式
- 虚拟账户自动创建（首次登录自动创建）
- 根据角色区分登录后跳转页面

## Capabilities

### New Capabilities

- `login-role-select`: 登录页面角色选择（房东/住户）
- `phone-login`: 手机号验证码登录接口
- `virtual-account`: 虚拟账户自动创建机制

### Modified Capabilities

- (无)

## Impact

- 前端：`frontend/src/pages/login/index.vue`
- 后端：`rental-user` 模块 UserController、UserService