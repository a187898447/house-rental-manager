# Phase 1 任务清单

## 任务检查流程

每个任务遵循以下检查步骤：
1. **代码检查** - 文件是否存在
2. **逻辑检查** - 业务逻辑是否正确
3. **API检查** - 前后端接口地址一致性
4. **字段检查** - 前后端字段一致性
5. **异常修复** / **新功能开发**

---

## 1. 房源管理

参考 Spec: `openspec/changes/phase1-xuqiu/specs/property-management/spec.md`

- [x] 1.1 代码检查：Property 实体存在
- [x] 1.2 逻辑检查：CRUD 逻辑正确（对比 spec.md 的 Requirement）
  - [x] create: PropertyServiceImpl.create()
  - [x] update: PropertyServiceImpl.updateProperty()
  - [x] delete: PropertyServiceImpl.deleteProperty() - 逻辑删除
- [x] 1.3 API检查：房源 API 接口正确
- [x] 1.4 字段检查：PropertyVO/DTO 字段一致

## 2. 租金管理

参考 Spec: `openspec/changes/phase1-xuqiu/specs/rent-management/spec.md`

- [x] 2.1 代码检查：RentRecord 实体存在
- [x] 2.2 逻辑检查：账单生成/计算逻辑正确（对比 spec.md）
  - [x] 记录租金: RentRecordServiceImpl.create()
  - [x] 收租提醒: RentRecordServiceImpl.sendReminder()
  - [x] 租金统计: RentRecordServiceImpl.getOwnerStats()
- [x] 2.3 API检查：租金 API 接口正确
- [x] 2.4 字段检查：金额/状态字段一致

## 3. 租客管理

参考 Spec: `openspec/changes/phase1-xuqiu/specs/tenant-management/spec.md`

- [x] 3.1 代码检查：Tenant 实体存在
- [x] 3.2 逻辑检查：入住/退租逻辑正确（对比 spec.md）
  - [x] 入住登记: TenantServiceImpl.checkIn()
  - [x] 租客列表: TenantServiceImpl.getOwnerTenants()
  - [x] 退租办理: TenantServiceImpl.checkOut()
- [x] 3.3 API检查：租客 API 接口正确
- [x] 3.4 字段检查：租客信息字段一致

## 4. 微信登录

参考 Spec: `openspec/changes/phase1-xuqiu/specs/wechat-login/spec.md`

- [x] 4.1 代码检查：WxServiceImpl 存在
- [x] 4.2 逻辑检查：登录流程（已实现）
  - [x] 微信授权: WxServiceImpl.jscode2session
  - [x] 绑定手机号: /user/bind-phone
  - [x] JWT Token: JwtAuthenticationFilter
  - [x] 路由鉴权: SecurityConfig 白名单
- [x] 4.3 状态：已实现（通过账号密码登录）