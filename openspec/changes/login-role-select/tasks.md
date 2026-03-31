# login-role-select 任务清单

## 任务检查流程

1. **代码检查** - 文件是否存在
2. **逻辑检查** - 业务逻辑是否正确
3. **API检查** - 前后端接口地址一致性
4. **字段检查** - 前后端字段一致性
5. **异常修复** / **新功能开发**

---

## 基于新 tasks 实现

- [x] 4.1 创建 PhoneLoginRequest DTO
- [x] 4.2 在 UserController 添加 /phone-login 接口
- [x] 4.3 在 UserService 接口添加 phoneLogin 方法
- [x] 4.4 在 UserServiceImpl 实现 phoneLogin 方法
- [x] 4.5 实现虚拟账户自动创建逻辑
- [x] 4.6 添加角色选择 Tab 组件
- [x] 4.7 实现角色切换清空表单逻辑
- [x] 4.8 实现手机号+验证码登录
- [x] 4.9 实现根据角色跳转不同页面
- [x] 4.10 在 auth.ts 添加 phoneLogin 方法
- [x] 4.11 配置后端接口地址
