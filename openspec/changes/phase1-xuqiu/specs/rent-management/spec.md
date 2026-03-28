## ADDED Requirements

### Requirement: 租金记录

系统 SHALL 支持记录每笔租金收入。

#### Scenario: 记录租金
- **WHEN** 房东记录租金（房源、租客、金额、支付日期、支付方式）
- **THEN** 系统保存租金记录，状态为已支付

#### Scenario: 按天计算租金
- **WHEN** 租客月中入住或退租
- **THEN** 系统按租住天数比例计算租金 = 月租金 × (租住天数 / 30)

### Requirement: 收租提醒

系统 SHALL 支持设置每月固定日期提醒收租。

#### Scenario: 设置提醒日期
- **WHEN** 房东设置每月提醒日期
- **THEN** 系统在该日期触发收租提醒通知

#### Scenario: 推送通知
- **WHEN** 到达提醒日期
- **THEN** 系统通过通用推送通知租客

### Requirement: 租金统计

系统 SHALL 支持按月/按年统计收租情况。

#### Scenario: 月度统计
- **WHEN** 房东查看月度收租统计
- **THEN** 显示当月租金收入总额和明细

#### Scenario: 年度统计
- **WHEN** 房东查看年度收租统计
- **THEN** 显示当年各月租金收入趋势和年度总收入