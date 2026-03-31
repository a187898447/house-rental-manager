## ADDED Requirements

### Requirement: 水电单价配置

系统 SHALL 支持按楼栋配置水电单价。

#### Scenario: 配置单价
- **WHEN** 房东设置水电单价
- **THEN** 系统保存单价配置

### Requirement: 抄表录入

系统 SHALL 支持每月录入水电表读数。

#### Scenario: 录入读数
- **WHEN** 房东录入当月水电表读数
- **THEN** 系统记录读数

### Requirement: 账单生成

系统 SHALL 支持自动计算水电费用。

#### Scenario: 生成账单
- **WHEN** 录入读数后
- **THEN** 系统根据单价和用量计算费用，生成账单
