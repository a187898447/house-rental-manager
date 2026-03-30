## ADDED Requirements

### Requirement: 微信授权登录

系统 SHALL 支持用户通过微信一键登录。

#### Scenario: 微信授权获取OpenID
- **WHEN** 用户点击微信登录并授权
- **THEN** 系统获取微信OpenID，检查是否已注册

#### Scenario: 绑定手机号
- **WHEN** 新用户首次登录
- **THEN** 引导用户绑定手机号，完成注册

#### Scenario: JWT Token生成
- **WHEN** 登录成功后
- **THEN** 系统生成JWT Token，前端持久化存储

#### Scenario: 路由鉴权
- **WHEN** 用户访问需要登录的页面
- **THEN** 校验Token有效性，无效则跳转登录页

#### Scenario: 演示模式
- **WHEN** 用户点击演示模式
- **THEN** 无需登录，使用默认管理员账户（没有则创建），可以使用所有功能
