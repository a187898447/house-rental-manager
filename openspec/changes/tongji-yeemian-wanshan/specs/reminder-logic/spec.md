## ADDED Requirements

### Requirement: 催租提醒三轮逻辑

系统 SHALL 实现固定到期日触发三轮催租提醒的逻辑。

#### Scenario: 第一轮提醒 D+0
- **WHEN** 租金到期日当天租客未支付
- **THEN** 租客收到第1次催租提醒，remind_count=1

#### Scenario: 第二轮提醒 D+2
- **WHEN** 租金逾期第2天租客仍未支付
- **THEN** 租客收到第2次催租提醒，remind_count=2

#### Scenario: 第三轮提醒 D+3
- **WHEN** 租金逾期第3天租客仍未支付
- **THEN** 租客收到第3次催租提醒，remind_count=3，同时房东收到逾期通知