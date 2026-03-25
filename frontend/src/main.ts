import { createSSRApp } from 'vue'
import { createPinia } from 'pinia'
import uviewPlus from 'uview-plus'
import App from './App.vue'

// 手动注册常用组件
import ULoadingIcon from 'uview-plus/components/u-loading-icon/u-loading-icon.vue'
import UEmpty from 'uview-plus/components/u-empty/u-empty.vue'
import UIcon from 'uview-plus/components/u-icon/u-icon.vue'
import UPicker from 'uview-plus/components/u-picker/u-picker.vue'
import UDatetimePicker from 'uview-plus/components/u-datetime-picker/u-datetime-picker.vue'
import UNumberBox from 'uview-plus/components/u-number-box/u-number-box.vue'
import UTabs from 'uview-plus/components/u-tabs/u-tabs.vue'
import UBadge from 'uview-plus/components/u-badge/u-badge.vue'

export function createApp() {
  const app = createSSRApp(App)
  const pinia = createPinia()
  
  app.use(pinia)
  app.use(uviewPlus)
  
  // 手动注册缺失的组件
  app.component('up-loading-icon', ULoadingIcon)
  app.component('u-loading-icon', ULoadingIcon)
  app.component('up-empty', UEmpty)
  app.component('u-empty', UEmpty)
  app.component('up-icon', UIcon)
  app.component('u-icon', UIcon)
  app.component('up-picker', UPicker)
  app.component('u-picker', UPicker)
  app.component('up-datetime-picker', UDatetimePicker)
  app.component('u-datetime-picker', UDatetimePicker)
  app.component('up-number-box', UNumberBox)
  app.component('u-number-box', UNumberBox)
  app.component('up-tabs', UTabs)
  app.component('u-tabs', UTabs)
  app.component('up-badge', UBadge)
  app.component('u-badge', UBadge)
  
  return {
    app
  }
}
