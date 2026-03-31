## ADDED Requirements

### Requirement: 微信支付

系统 SHALL 支持微信支付缴费。

#### Scenario: 微信支付
- **WHEN** 租客选择微信支付
- **THEN** 调用微信支付接口完成支付

#### Scenario: 支付结果
- **WHEN** 支付完成后
- **THEN** 系统更新账单状态，保存支付记录
