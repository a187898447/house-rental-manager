# 代码风格规范

## EditorConfig 规则（统一所有编辑器）

### 通用规则

| 规则 | 值 | 说明 |
|------|-----|------|
| charset | utf-8 | 字符编码 |
| end_of_line | lf | 换行符 |
| insert_final_newline | true | 文件末尾插入空行 |
| trim_trailing_whitespace | true | 移除行尾空格 |

### Java

```properties
[*.java]
indent_style = space
indent_size = 4
```

### 前端 (Vue, TS, JS)

```properties
[*.{vue,ts,js,json}]
indent_style = space
indent_size = 2
```

### CSS/SCSS

```properties
[*.{css,scss}]
indent_style = space
indent_size = 2
```

### XML

```properties
[*.xml]
indent_style = space
indent_size = 4
```

### Markdown

```properties
[*.md]
trim_trailing_whitespace = false
```

## IDE 配置建议

- VSCode: 安装 EditorConfig 插件
- IDEA: 内置 EditorConfig 支持
- 保存文件时自动应用格式