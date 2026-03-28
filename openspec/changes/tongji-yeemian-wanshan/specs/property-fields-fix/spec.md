## ADDED Requirements

### Requirement: 房源字段修复

系统 MUST 在 Property entity 中添加缺失的四个字段：name、address、building、type。

#### Scenario: Property实体字段完整
- **WHEN** 查询 Property 实体
- **THEN** 返回 name（房源名称）、address（详细地址）、building（楼栋）、type（户型）字段

#### Scenario: 数据库字段映射
- **WHEN** Property 实体与数据库表映射
- **THEN** @TableField 注解正确映射 name、address、building、type 字段