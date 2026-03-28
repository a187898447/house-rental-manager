## ADDED Requirements

### Requirement: 租客邀请码

系统 SHALL 支持房东生成邀请码邀请租客入住。

#### Scenario: 生成邀请码
- **WHEN** 房东为房源生成邀请码
- **THEN** 系统生成唯一邀请码，有效期7天，可设置

#### Scenario: 租客使用邀请码
- **WHEN** 租客使用邀请码绑定房源
- **THEN** 系统将租客与房源关联，邀请码状态更新为已使用

#### Scenario: 邀请码过期
- **WHEN** 邀请码过期
- **THEN** 状态更新为已过期，无法再使用