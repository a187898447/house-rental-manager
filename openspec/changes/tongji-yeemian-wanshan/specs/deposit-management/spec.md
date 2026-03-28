## ADDED Requirements

### Requirement: 押金收取记录

系统 SHALL 记录入住时的押金金额。

#### Scenario: 收取押金
- **WHEN** 租客入住时房东记录押金金额
- **THEN** 系统保存押金金额，状态为待退还

### Requirement: 押金退还记录

系统 SHALL 记录退租时押金退还金额（纯线下协商，系统只记录最终金额）。

#### Scenario: 退还押金
- **WHEN** 退租时房东与租客线下协商确定实退金额
- **THEN** 系统记录实退金额、退还日期，状态更新为已退还/部分退还

### Requirement: 退房照片上传

系统 SHALL 支持可选上传退房交接照片。

#### Scenario: 上传退房照片
- **WHEN** 退租时房东上传退房交接照片
- **THEN** 系统保存照片URL（JSON数组），仅作记录留存