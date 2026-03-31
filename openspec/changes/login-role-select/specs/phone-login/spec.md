## ADDED Requirements

### Requirement: 手机号验证码登录

系统 SHALL 提供手机号+验证码登录接口，支持房东和住户角色。

#### Scenario: 首次登录创建虚拟账户
- **WHEN** 用户使用新手机号首次登录
- **THEN** 系统自动创建虚拟账户并返回登录成功

#### Scenario: 已有账户登录
- **WHEN** 用户使用已注册手机号登录
- **THEN** 验证成功后返回用户信息和 token

#### Scenario: 验证码错误
- **WHEN** 用户输入错误验证码
- **THEN** 返回错误提示"验证码错误"

#### Scenario: 角色设置
- **WHEN** 用户登录时传入角色参数 (landlord/tenant)
- **THEN** 系统根据角色设置用户身份