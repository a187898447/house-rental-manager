## ADDED Requirements

### Requirement: 通用推送通知

系统 SHALL 实现不依赖微信公众号的系统内通知机制。

#### Scenario: 收租提醒推送
- **WHEN** 租金到期日
- **THEN** 系统推送收租提醒通知给租客

#### Scenario: 催租提醒推送
- **WHEN** 租金逾期（D+0/D+2/D+3）
- **THEN** 系统推送催租提醒给租客，逾期第3天同时推送通知给房东

#### Scenario: 退租通知推送
- **WHEN** 租客申请退租或退租完成
- **THEN** 系统推送退租通知给房东

#### Scenario: 水电账单推送
- **WHEN** 水电账单生成后
- **THEN** 系统推送账单通知给租客

#### Scenario: 报修通知推送
- **WHEN** 租客提交报修或报修状态变更
- **THEN** 系统推送报修进度通知给租客和房东