## ADDED Requirements

### Requirement: 租客账单查看

系统 SHALL 支持租客查看自己的账单明细。

#### Scenario: 查看租金账单
- **WHEN** 租客进入账单中心查看租金
- **THEN** 显示当前待支付/历史租金账单明细

#### Scenario: 查看水电账单
- **WHEN** 租客查看水电账单
- **THEN** 显示当前待支付/历史水电费明细

#### Scenario: 查看其他费用
- **WHEN** 租客查看其他费用
- **THEN** 显示当前待支付/历史其他费用明细

### Requirement: 租客在线缴费

系统 SHALL 支持租客通过微信支付在线缴费。

#### Scenario: 微信支付
- **WHEN** 租客选择账单并点击支付
- **THEN** 调用微信支付接口完成支付

#### Scenario: 支付记录
- **WHEN** 支付完成后
- **THEN** 保存支付记录，账单状态更新为已支付

### Requirement: 租客报修申请

系统 SHALL 支持租客提交报修申请。

#### Scenario: 提交报修
- **WHEN** 租客提交报修（拍照+描述问题）
- **THEN** 系统创建报修记录，状态为待处理

#### Scenario: 查看报修进度
- **WHEN** 租客查看报修进度
- **THEN** 显示报修处理状态和时间线