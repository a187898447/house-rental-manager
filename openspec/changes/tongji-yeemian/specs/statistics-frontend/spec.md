## ADDED Requirements

### Requirement: 前端页面自动加载

统计页面 MUST 提供自动加载和显示统计数据的功能。

#### Scenario: 页面自动加载数据
- **WHEN** 用户进入统计页面
- **THEN** 自动调用 getStatistics() 并显示数据

#### Scenario: 加载状态显示
- **WHEN** 正在加载数据
- **THEN** 显示加载指示器

#### Scenario: 数据显示
- **WHEN** 数据加载完成
- **THEN** 显示统计卡片和图表