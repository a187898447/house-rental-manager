## ADDED Requirements

### Requirement: 房东生成合同

系统 SHALL 支持房东生成电子合同。

#### Scenario: 生成合同
- **WHEN** 房东创建合同（包含租金、押金、水电单价、其他费用）
- **THEN** 系统生成合同，记录房源/租客/起止日期

### Requirement: 租客手写签名

系统 SHALL 支持租客通过前端 Canvas 手绘签名。

#### Scenario: 手写签名
- **WHEN** 租客在合同页面进行手写电子签名
- **THEN** 签名图片上传保存，签署状态更新为已签署

### Requirement: 合同状态管理

系统 SHALL 管理合同签署状态。

#### Scenario: 合同状态变更
- **WHEN** 合同签署后
- **THEN** 状态流转：待签署 → 已签署 → 已生效 → 已终止
