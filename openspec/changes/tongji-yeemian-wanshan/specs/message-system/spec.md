## ADDED Requirements

### Requirement: 租客房东消息沟通

系统 SHALL 支持租客和房东之间的消息沟通。

#### Scenario: 租客发送消息
- **WHEN** 租客发送消息给房东
- **THEN** 系统保存消息内容，type=1，状态为未读

#### Scenario: 房东回复消息
- **WHEN** 房东回复租客消息
- **THEN** 系统保存回复内容，type=2

#### Scenario: 消息已读
- **WHEN** 对方查看消息
- **THEN** 消息状态更新为已读