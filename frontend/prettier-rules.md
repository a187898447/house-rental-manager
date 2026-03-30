# Prettier 格式化规则文档

## 配置说明

配置文件：`frontend/.prettierrc`

```json
{
  "semi": true,
  "singleQuote": true,
  "printWidth": 100,
  "tabWidth": 2,
  "trailingComma": "es5",
  "endOfLine": "lf"
}
```

## 规则说明

| 规则 | 值 | 说明 |
|------|-----|------|
| `semi` | true | 语句末尾加分号 |
| `singleQuote` | true | 使用单引号 |
| `printWidth` | 100 | 行最大宽度 |
| `tabWidth` | 2 | 缩进宽度 |
| `trailingComma` | es5 | 尾随逗号（ES5兼容） |
| `endOfLine` | lf | 换行符（Unix） |

## 格式化示例

### JavaScript/TypeScript

```javascript
// ✅ 格式化后
const greeting = 'hello';

// ❌ 格式化前
const greeting="hello";
```

### Vue 模板

```vue
<!-- ✅ 格式化后 -->
<template>
  <div class="container">
    <h1>Title</h1>
  </div>
</template>

<!-- ❌ 格式化前 -->
<template>
<div class="container">
<h1>Title</h1>
</div>
</template>
```

## 运行格式化

```bash
# 格式化所有文件
npx prettier --write src/

# 检查（不修改）
npx prettier --check src/

# 格式化单个文件
npx prettier --write src/App.vue
```

## 与 ESLint 配合

Prettier 格式化代码风格，ESLint 检查代码质量。

```bash
# 先 Prettier 格式化
npx prettier --write src/

# 再 ESLint 检查
npx eslint src/ --fix
```

## VSCode 配置

```json
{
  "editor.defaultFormatter": "esbenp.prettier-vscode",
  "[vue]": {
    "editor.defaultFormatter": "esbenp.prettier-vscode"
  },
  "[javascript]": {
    "editor.defaultFormatter": "esbenp.prettier-vscode"
  },
  "[typescript]": {
    "editor.defaultFormatter": "esbenp.prettier-vscode"
  }
}
```

## 忽略文件

创建 `.prettierignore`:

```
node_modules/
dist/
*.min.js
```