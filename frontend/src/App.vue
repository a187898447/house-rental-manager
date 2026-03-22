<script setup lang="ts">
import { onLaunch, onShow, onHide } from '@dcloudio/uni-app'
import { useUserStore } from '@/stores/user'

onLaunch(() => {
  console.log('App Launch')
  // 初始化用户状态
  const userStore = useUserStore()
  userStore.init()
})

onShow(() => {
  console.log('App Show')
  // 检查登录状态
  checkLoginStatus()
})

onHide(() => {
  console.log('App Hide')
})

// 检查登录状态
const checkLoginStatus = () => {
  const userStore = useUserStore()
  const token = uni.getStorageSync('token')
  const currentPage = getCurrentPages()
  const current = currentPage[currentPage.length - 1]
  const pagePath = current?.$page?.fullPath || ''
  
  // 如果没有token且不在登录页，跳转登录
  if (!token && !pagePath.includes('login')) {
    uni.reLaunch({
      url: '/pages/login/index'
    })
  }
}
</script>

<style lang="scss">
@import 'uview-plus/index.scss';
</style>
