## ADDED Requirements

### Requirement: 登录页角色选择

登录页面 SHALL 提供房东和住户两种角色选择，用户选择角色后调用对应的登录接口。

#### Scenario: 房东登录
- **WHEN** 用户选择"房东"角色并点击登录
- **THEN** 调用手机号登录接口，角色参数为 landlord

#### Scenario: 住户登录
- **WHEN** 用户选择"住户"角色并点击登录
- **THEN** 调用手机号登录接口，角色参数为 tenant

#### Scenario: 角色切换
- **WHEN** 用户点击切换角色
- **THEN** 清空已填写的表单数据，重置验证码倒计时

#### Scenario: 角色对应页面
- **WHEN** 房东登录成功
- **THEN** 跳转到房东端首页 /pages/landlord/index/index

#### Scenario: 角色对应页面
- **WHEN** 住户登录成功
- **THEN** 跳转到住户端首页 /pages/tenant/index/index