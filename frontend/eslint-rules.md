# ESLint 规则文档

## 配置说明

配置文件：`frontend/.eslintrc.js`

```javascript
module.exports = {
  root: true,
  env: {
    browser: true,
    node: true,
    es2021: true
  },
  extends: ['plugin:vue/vue3-essential'],
  rules: {
    // Vue
    'vue/no-setup-props-destructure': 'off',
    'vue/no-mutating-props': 'off',
    // Common
    'no-console': 'warn',
    'no-debugger': 'error',
    'no-unused-vars': 'off',
    'no-empty': ['error', { allowEmptyCatch: true }]
  }
};
```

## 规则说明

### Vue 3 规则

| 规则 | 级别 | 说明 |
|------|------|------|
| `vue/no-setup-props-destructure` | off | 允许解构 props |
| `vue/no-mutating-props` | off | 允许修改 props（需谨慎） |

### 通用规则

| 规则 | 级别 | 说明 |
|------|------|------|
| `no-console` | warn | 控制台输出警告 |
| `no-debugger` | error | debugger 语句报错 |
| `no-unused-vars` | off | 未使用变量不报错 |
| `no-empty` | error | 空代码块报错（允许空catch） |

## 运行检查

```bash
# 检查
npx eslint src/

# 修复
npx eslint src/ --fix
```

## VSCode 配置

```json
{
  "editor.codeActionsOnSave": {
    "source.fixAll.eslint": true
  }
}
```

## IDE 集成

- VSCode: 安装 ESLint 插件
- WebStorm: 内置 ESLint 支持