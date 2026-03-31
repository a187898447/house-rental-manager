## ADDED Requirements

### Requirement: 虚拟账户自动创建

系统 SHALL 在用户首次登录时自动创建虚拟账户，供测试使用。

#### Scenario: 首次登录创建虚拟账户
- **WHEN** 用户使用新手机号首次登录
- **THEN** 自动创建对应角色（房东/住户）的虚拟账户

#### Scenario: 已有账户登录
- **WHEN** 用户使用已有账户登录
- **THEN** 正常登录，返回用户信息

#### Scenario: 虚拟账户测试
- **WHEN** 使用虚拟账户登录测试
- **THEN** 账户具有对应角色权限，可正常操作系统