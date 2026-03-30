# tongji-yeemian-wanshan 任务清单

## 任务检查流程

每个任务遵循以下检查步骤：
1. **代码检查** - 文件是否存在
2. **逻辑检查** - 业务逻辑是否正确
3. **API检查** - 前后端接口地址一致性
4. **字段检查** - 前后端字段一致性
5. **异常修复** / **新功能开发**

---

## 1. Property 字段修复

参考 Spec: `openspec/changes/tongji-yeemian-wanshan/specs/property-fields-fix/spec.md`

- [x] 1.1 代码检查：Property entity 包含 name/address/building/type 字段
- [x] 1.2 逻辑检查：字段数据类型正确（String）
- [x] 1.3 API检查：PropertyVO 包含对应字段
- [x] 1.4 字段检查：前端页面显示字段对应

## 2. 催租提醒逻辑

参考 Spec: `openspec/changes/tongji-yeemian-wanshan/specs/reminder-logic/spec.md`

- [x] 2.1 代码检查：BillTask 定时任务存在
- [x] 2.2 逻辑检查：BillTask 存在（但 D+0/D+2/D+3 逻辑未完整实现）
  - [x] 代码: BillTask.sendPaymentReminder() 存在
  - [ ] 逻辑: 缺少 D+0/D+2/D+3 精确判断
  - [ ] 逻辑: 缺少房东通知
- [x] 2.3 API检查：提醒接口存在
- [x] 2.4 字段检查：remindCount 字段存在
- [ ] 2.5 新功能开发：完善 D+0/D+2/D+3 逻辑
  - D+0: 租客第1次提醒
  - D+2: 租客第2次提醒
  - D+3: 租客第3次提醒 + 房东通知

## 3. 水电管理

参考 Spec: `openspec/changes/tongji-yeemian-wanshan/specs/utility-management/spec.md`

- [x] 3.1 代码检查：UtilityBill 实体存在
- [x] 3.2 逻辑检查：用量计算公式正确（current - last）
- [x] 3.3 API检查：UtilityBillController 存在
- [x] 3.4 字段检查：单价、读数、金额字段一致
- [x] 3.5 异常修复：无异常

## 4. 其他费用

参考 Spec: `openspec/changes/tongji-yeemian-wanshan/specs/other-fee/spec.md`

- [x] 4.1 代码检查：OtherFee 实体存在
- [x] 4.2 逻辑检查：费用计算逻辑正确
- [x] 4.3 API检查：OtherFeeController CRUD 接口存在
- [x] 4.4 字段检查：前后端字段一致

## 5. 押金管理

参考 Spec: `openspec/changes/tongji-yeemian-wanshan/specs/deposit-management/spec.md`

- [x] 5.1 代码检查：Deposit 实体存在
- [x] 5.2 逻辑检查：押金退还逻辑（纯记录）
- [x] 5.3 API检查：押金接口存在
- [x] 5.4 字段检查：refundAmount 字段存在

## 6. 报修管理

参考 Spec: `openspec/changes/tongji-yeemian-wanshan/specs/repair-management/spec.md`

- [x] 6.1 代码检查：Repair 实体存在
- [x] 6.2 逻辑检查：状态流转逻辑正确（status字段）
- [x] 6.3 API检查：RepairController 存在
- [x] 6.4 字段检查：状态字段正确

## 7. 历史账单

- [x] 7.1 代码检查：历史查询接口存在（RentRecord 支持 month 查询）
- [x] 7.2 逻辑检查：时间范围查询逻辑正确
- [x] 7.3 API检查：getOwnerBills 支持 month 参数

## 8. 通用推送

- [x] 8.1 代码检查：NotifyMessage 实体存在
- [x] 8.2 逻辑检查：推送逻辑正确
- [x] 8.3 API检查：PushController /push/send 存在
- [x] 8.4 字段检查：isRead 字段存在

## 9. 数据统计

- [x] 9.1 代码检查：StatisticsController 存在
- [x] 9.2 逻辑检查：统计计算公式正确
- [x] 9.3 API检查：/statistics/dashboard 接口存在
- [x] 9.4 字段检查：月度/年度字段一致

## 10. 租客端

- [x] 10.1 代码检查：租客 API 存在（getTenantBills）
- [x] 10.2 逻辑检查：账单查看/缴费/报修逻辑
- [x] 10.4 字段检查：租客可见字段正确

## 11. 电子合同

- [x] 11.1 代码检查：Contract 实体存在
- [x] 11.2 逻辑检查：合同生成/签名逻辑
- [x] 11.3 API检查：合同接口存在
- [x] 11.4 字段检查：signUrl 字段存在
- [ ] 11.5 前后端检查：Canvas 签名集成正确