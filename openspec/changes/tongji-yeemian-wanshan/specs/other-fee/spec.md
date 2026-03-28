## ADDED Requirements

### Requirement: 其他费用类型定义

系统 SHALL 支持自定义费用类型（管理费、网络费、垃圾费、维修费等）。

#### Scenario: 添加费用类型
- **WHEN** 房东添加自定义费用类型
- **THEN** 系统保存费用类型和金额，可用于后续账单生成

### Requirement: 其他费用账单生成

系统 SHALL 每月随租金一起生成其他费用账单。

#### Scenario: 生成组合账单
- **WHEN** 每月账单生成时
- **THEN** 租客需支付：租金 + 水电 + 其他费用（组合账单）