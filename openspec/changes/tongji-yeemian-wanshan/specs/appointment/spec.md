## ADDED Requirements

### Requirement: 预约看房

系统 SHALL 支持潜在租客预约看房。

#### Scenario: 发起预约
- **WHEN** 访客提交看房预约（姓名、电话、日期）
- **THEN** 系统创建预约记录，状态为待确认

#### Scenario: 房东确认预约
- **WHEN** 房东确认看房预约
- **THEN** 预约状态更新为已确认

#### Scenario: 预约取消
- **WHEN** 访客或房东取消预约
- **THEN** 预约状态更新为已取消

#### Scenario: 看房完成
- **WHEN** 看房当天结束
- **THEN** 预约状态更新为已完成