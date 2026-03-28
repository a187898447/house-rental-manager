## ADDED Requirements

### Requirement: 楼栋管理

系统 SHALL 支持房东管理楼栋信息。

#### Scenario: 添加楼栋
- **WHEN** 房东添加楼栋（名称、地址）
- **THEN** 系统保存楼栋信息到 building 表

#### Scenario: 设置水电单价
- **WHEN** 房东设置楼栋的水费和电费单价
- **THEN** 系统保存单价，生成账单时使用该单价计算

#### Scenario: 编辑楼栋
- **WHEN** 房东修改楼栋信息
- **THEN** 系统更新楼栋信息