## ADDED Requirements

### Requirement: 入住登记

系统 SHALL 支持房东登记租客入住信息。

#### Scenario: 登记租客
- **WHEN** 房东录入租客信息（姓名、电话、起止租期、租金支付方式）并关联房源
- **THEN** 系统创建租客记录，与房源一对一绑定，房源状态更新为已出租

### Requirement: 租客列表

系统 SHALL 支持查看当前和历史租客。

#### Scenario: 查看当前租客
- **WHEN** 房东查看租客列表选择"当前租客"
- **THEN** 显示状态为租住中的租客

#### Scenario: 查看历史租客
- **WHEN** 房东查看租客列表选择"历史租客"
- **THEN** 显示状态为已退租的租客

### Requirement: 退租办理

系统 SHALL 支持记录租客退租信息。

#### Scenario: 办理退租
- **WHEN** 房东记录租客退租日期
- **THEN** 系统更新租客状态为已退租，房源状态更新为空置