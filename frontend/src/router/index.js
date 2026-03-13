import { createRouter, createWebHistory } from 'vue-router'
import routes from './routes'
import { useSessionStore } from '@/stores/session'

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return {
      top: 0
    }
  }
})

router.beforeEach((to) => {
  const sessionStore = useSessionStore()

  if (to.meta?.permission && !sessionStore.hasPermission(to.meta.permission)) {
    return {
      name: 'dashboard'
    }
  }

  return true
})

export default router
