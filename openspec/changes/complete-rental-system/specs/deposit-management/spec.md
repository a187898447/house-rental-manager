## ADDED Requirements

### Requirement: 押金收取

系统 SHALL 支持房东收取押金。

#### Scenario: 收取押金
- **WHEN** 租客入住时房东收取押金
- **THEN** 系统记录押金金额

### Requirement: 押金退还

系统 SHALL 支持房东退还押金。

#### Scenario: 退还押金
- **WHEN** 租客退租时房东确认退还押金
- **THEN** 系统记录实际退还金额

### Requirement: 押金记录

系统 SHALL 支持查看押金记录。

#### Scenario: 查看押金记录
- **WHEN** 查看押金历史
- **THEN** 显示押金缴纳和退还记录
