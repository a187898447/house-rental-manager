## ADDED Requirements

### Requirement: 租客首页

系统 SHALL 为租客显示首页信息。

#### Scenario: 租客首页
- **WHEN** 租客登录后进入首页
- **THEN** 显示房源信息、待缴费账单、功能菜单

### Requirement: 租客账单查看

系统 SHALL 支持租客查看账单明细。

#### Scenario: 查看账单
- **WHEN** 租客进入账单页面
- **THEN** 显示租金、水电、其他费用明细

### Requirement: 租客在线缴费

系统 SHALL 支持租客在线支付。

#### Scenario: 在线支付
- **WHEN** 租客选择账单并支付
- **THEN** 调用微信支付完成缴费

### Requirement: 租客报修申请

系统 SHALL 支持租客提交报修。

#### Scenario: 提交报修
- **WHEN** 租客拍照+描述提交报修
- **THEN** 系统创建报修记录
