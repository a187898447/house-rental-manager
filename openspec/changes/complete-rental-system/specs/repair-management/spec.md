## ADDED Requirements

### Requirement: 报修提交

系统 SHALL 支持租客提交报修申请。

#### Scenario: 提交报修
- **WHEN** 租客拍照+描述问题提交报修
- **THEN** 系统创建报修记录，状态为待处理

### Requirement: 报修处理

系统 SHALL 支持房东处理报修。

#### Scenario: 处理报修
- **WHEN** 房东处理报修并更新状态
- **THEN** 系统记录处理结果

### Requirement: 报修进度查看

系统 SHALL 支持查看报修进度。

#### Scenario: 查看进度
- **WHEN** 查看报修处理进度
- **THEN** 显示处理状态和时间线
