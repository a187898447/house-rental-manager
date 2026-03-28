## ADDED Requirements

### Requirement: 楼栋水电单价配置

系统 SHALL 支持按楼栋设置水费和电费单价。

#### Scenario: 配置楼栋单价
- **WHEN** 房东设置楼栋水费单价和电费单价
- **THEN** 系统保存单价到 building 表，生成账单时使用该单价

### Requirement: 水电抄表录入

系统 SHALL 支持每月录入水表和电表读数。

#### Scenario: 录入水表读数
- **WHEN** 房东录入本期水表读数
- **THEN** 系统计算用水量 = 本期读数 - 上期读数，水费 = 用水量 × 单价

#### Scenario: 录入电表读数
- **WHEN** 房东录入本期电表读数
- **THEN** 系统计算用电量 = 本期读数 - 上期读数，电费 = 用电量 × 单价

### Requirement: 水电账单生成

系统 SHALL 根据抄表数据自动生成水电账单。

#### Scenario: 生成月度账单
- **WHEN** 每月账单周期结束
- **THEN** 系统自动生成包含水费、电费、状态为待支付的账单

#### Scenario: 账单推送
- **WHEN** 水电账单生成后
- **THEN** 通过通用推送通知租客（微信公众号暂缓）