## ADDED Requirements

### Requirement: 租金记录

系统 SHALL 支持记录租金收入。

#### Scenario: 记录租金
- **WHEN** 房东手动记录租金或系统自动生成月度账单
- **THEN** 系统创建租金记录

### Requirement: 收租提醒

系统 SHALL 支持每月固定日期提醒收租。

#### Scenario: 收租提醒
- **WHEN** 到达预设收租日期
- **THEN** 系统发送收租通知

### Requirement: 催租提醒

系统 SHALL 支持逾期未付自动提醒。

#### Scenario: D+0提醒
- **WHEN** 租金到期日当天
- **THEN** 系统发送第1次提醒给租客

#### Scenario: D+2提醒
- **WHEN** 租金逾期第2天
- **THEN** 系统发送第2次提醒给租客

#### Scenario: D+3提醒
- **WHEN** 租金逾期第3天
- **THEN** 系统发送第3次提醒给租客，并通知房东
