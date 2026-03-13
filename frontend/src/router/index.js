import { createRouter, createWebHistory } from 'vue-router'
import routes, { findDefaultAppRoute } from './routes'
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

router.beforeEach(async (to) => {
  const sessionStore = useSessionStore()

  if (!sessionStore.initialized) {
    try {
      await sessionStore.initializeSession()
    } catch (error) {
      if (to.name !== 'login') {
        return {
          name: 'login'
        }
      }
    }
  }

  if (to.meta?.guestOnly && sessionStore.isAuthenticated) {
    return {
      name: findDefaultAppRoute(sessionStore)
    }
  }

  if (!to.meta?.public && !sessionStore.isAuthenticated) {
    return {
      name: 'login',
      query: {
        redirect: to.fullPath
      }
    }
  }

  if (to.meta?.permission && !sessionStore.hasPermission(to.meta.permission)) {
    return {
      name: findDefaultAppRoute(sessionStore)
    }
  }

  return true
})

export default router
