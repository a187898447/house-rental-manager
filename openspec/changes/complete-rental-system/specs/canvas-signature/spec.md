## ADDED Requirements

### Requirement: Canvas手写签名

系统 SHALL 支持手写签名功能。

#### Scenario: 手写签名
- **WHEN** 租客在签名区域手写签名
- **THEN** 系统将签名保存为图片

#### Scenario: 签名上传
- **WHEN** 签名完成后提交
- **THEN** 系统上传签名图片并关联合同
