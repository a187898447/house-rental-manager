## ADDED Requirements

### Requirement: 登录页角色选择

登录页面 SHALL 提供房东和住户两种角色选择，用户选择角色后调用对应的登录接口。

#### Scenario: 房东登录
- **WHEN** 用户选择"房东"角色并输入账号密码
- **THEN** 调用房东登录接口，验证 ownerId 权限

#### Scenario: 住户登录
- **WHEN** 用户选择"住户"角色并输入账号密码
- **THEN** 调用住户登录接口，验证 tenantId 权限

#### Scenario: 角色切换
- **WHEN** 用户切换角色
- **THEN** 清空已填写的表单数据