## ADDED Requirements

### Requirement: 房源添加

系统 SHALL 支持房东添加房源信息。

#### Scenario: 添加房源
- **WHEN** 房东填写房源信息（名称、地址、楼栋、户型、面积、租金）并提交
- **THEN** 系统创建房源记录，返回成功提示

### Requirement: 房源列表

系统 SHALL 支持房东查看房源列表。

#### Scenario: 查看房源列表
- **WHEN** 房东进入房源列表页面
- **THEN** 显示所有房源，支持按状态筛选（未出租/已出租）

### Requirement: 房源编辑

系统 SHALL 支持房东编辑房源信息。

#### Scenario: 编辑房源
- **WHEN** 房东修改房源信息并提交
- **THEN** 系统更新房源记录

### Requirement: 房源删除

系统 SHALL 支持房东删除房源（逻辑删除）。

#### Scenario: 删除房源
- **WHEN** 房东点击删除房源
- **THEN** 系统标记房源为已删除状态，保留历史记录