# 租房小程序 Phase 2 完整需求

## 为什么

Phase 1 完成了基础功能（登录、房源管理、租客管理、租金管理），Phase 2 需要完善水电管理、押金、其他费用、报修、电子合同等核心功能，实现完整的租赁管理流程。

## 需求变更

### 核心功能模块

| 编号 | 功能 | 说明 |
|------|------|------|
| P2-01 | **Bug修复** | Property entity 缺少 name/address/building/type 四个字段 |
| P2-02 | 催租提醒 | D+0租客→D+2租客→D+3租客+房东通知 |
| P2-03 | 水电管理 | 楼栋单价配置、抄表录入、用量计算、账单生成 |
| P2-04 | 其他费用 | 管理费/网络费/垃圾费等自定义收费 |
| P2-05 | 押金管理 | 入住收取→退租退还→记录实退金额（纯线下协商） |
| P2-06 | 报修管理 | 租客提交→房东处理→状态跟踪 |
| P2-07 | 历史账单 | 历年租金/水电/其他费用可追溯 |
| P2-08 | 通用推送 | 收租/催租/退租/水电/报修通知（暂缓微信公众号） |
| P2-09 | 数据统计 | 月度/年度收入报表 |
| P2-10 | 租客端 | 账单查看 + 在线缴费 + 报修申请 |
| P2-11 | 电子合同 | 房东生成 + 租客手写签名（Canvas） |

## Capabilities

- **New Capabilities**:
  - property-fields-fix: 修复 Property entity 字段
  - reminder-logic: 催租提醒逻辑
  - utility-management: 水电管理（楼栋单价+抄表+账单）
  - other-fee: 其他费用管理
  - deposit-management: 押金收取/退还记录
  - repair-management: 报修提交/处理
  - bill-history: 历史账单查询
  - push-notification: 通用推送通知
  - statistics: 数据统计报表
  - tenant-portal: 租客端功能
  - e-contract: 电子合同（含手写签名）

## 影响范围

- **后端**: 新增UtilityBill、OtherFee、RepairRequest等模块
- **前端**: 新增水电录入、费用管理、报修处理、合同签署等页面
- **数据库**: 新增building表，扩展deposit/repair表字段