## ADDED Requirements

### Requirement: 入住登记

系统 SHALL 支持房东登记租客入住信息。

#### Scenario: 入住登记
- **WHEN** 房东填写租客信息（姓名、电话、租期、租金）并提交
- **THEN** 系统创建租客记录并关联房源

### Requirement: 租客列表

系统 SHALL 支持房东查看租客列表。

#### Scenario: 查看租客列表
- **WHEN** 房东进入租客列表页面
- **THEN** 显示当前/历史租客列表

### Requirement: 退租办理

系统 SHALL 支持房东办理退租。

#### Scenario: 退租办理
- **WHEN** 房东确认退租，填写退租信息
- **THEN** 系统更新租客状态，记录退租信息