## ADDED Requirements

### Requirement: 获取完整统计数据

系统 SHALL 提供完整的房东统计数据查询功能，包括房源统计、租金统计、押金统计和其他费用统计。

#### Scenario: 成功获取统计数据
- **WHEN** 房东已登录并调用 GET /api/statistics/dashboard 接口
- **THEN** 返回 totalProperties, rentedCount, vacantCount, monthlyRent, pendingAmount, pendingCount, rentIncome, depositIncome, otherIncome

#### Scenario: 空数据返回
- **WHEN** 房东无任何数据
- **THEN** 返回所有字段为 0