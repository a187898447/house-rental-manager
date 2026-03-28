## ADDED Requirements

### Requirement: 租客提交报修

系统 SHALL 支持租客提交报修申请。

#### Scenario: 提交报修申请
- **WHEN** 租客提交报修（描述问题、照片选填、日期）
- **THEN** 系统创建报修记录，状态为待处理

### Requirement: 房东处理报修

系统 SHALL 支持房东查看并处理租客提交的报修。

#### Scenario: 处理报修
- **WHEN** 房东查看报修详情并更新处理状态
- **THEN** 状态更新为处理中/已完成/已取消，保存处理备注

### Requirement: 报修状态跟踪

系统 SHALL 跟踪报修处理进度。

#### Scenario: 报修状态变更
- **WHEN** 报修状态变更时
- **THEN** 记录时间戳，历史报修记录可查看