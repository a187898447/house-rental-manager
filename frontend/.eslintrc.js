module.exports = {
  root: true,
  env: {
    browser: true,
    node: true,
    es2021: true
  },
  extends: [
    'plugin:vue/vue3-essential'
  ],
  parserOptions: {
    ecmaVersion: 2021
  },
  rules: {
    'vue/no-setup-props-destructure': 'off',
    'vue/no-mutating-props': 'off',
    'no-console': 'warn',
    'no-debugger': 'error',
    'no-unused-vars': 'off',
    'no-empty': ['error', { allowEmptyCatch: true }]
  }
}