## ADDED Requirements

### Requirement: 获取完整统计数据

#### Scenario: 获取统计数据
- **WHEN** 房东已登录并调用 `/api/statistics/dashboard` 接口
- **THEN** 返回 totalProperties, rentedCount, vacantCount, monthlyRent, pendingAmount, pendingCount, rentIncome, depositIncome, otherIncome

#### Scenario: 空数据返回
- **WHEN** 房东无任何数据
- **THEN** 返回所有字段为 0

### Requirement: 前端页面加载

#### Scenario: 自动加载
- **WHEN** 用户进入统计页面
- **THEN** 自动调用 getStatistics() 并显示数据

#### Scenario: 加载状态
- **WHEN** 正在加载数据
- **THEN** 显示加载指示器