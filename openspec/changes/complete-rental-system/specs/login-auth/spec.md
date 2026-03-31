## ADDED Requirements

### Requirement: 微信一键登录

系统 SHALL 支持用户通过微信授权一键登录。

#### Scenario: 微信授权登录
- **WHEN** 用户点击微信登录按钮
- **THEN** 弹出微信授权页面，用户授权后系统创建/获取用户信息并返回 Token

### Requirement: 手机号登录

系统 SHALL 支持用户通过手机号+验证码登录。

#### Scenario: 手机号验证码登录
- **WHEN** 用户输入手机号和验证码点击登录
- **THEN** 系统验证验证码后返回 Token（开发环境验证码固定为 123456）

### Requirement: 角色选择

系统 SHALL 支持房东和住户两种角色登录。

#### Scenario: 房东登录
- **WHEN** 用户选择房东角色登录
- **THEN** 系统设置用户角色为 landlord，登录后跳转到房东端首页

#### Scenario: 住户登录
- **WHEN** 用户选择住户角色登录
- **THEN** 系统设置用户角色为 tenant，登录后跳转到租客端首页

#### Scenario: 角色切换清空表单
- **WHEN** 用户切换登录角色
- **THEN** 清空已填写的表单数据