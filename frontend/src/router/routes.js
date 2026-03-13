export const appRoutes = [
  {
    path: '/dashboard',
    name: 'dashboard',
    component: () => import('@/views/dashboard/DashboardView.vue'),
    meta: {
      icon: '01',
      titleKey: 'nav.dashboard',
      descriptionKey: 'nav.dashboardDescription'
    }
  },
  {
    path: '/reviews',
    name: 'reviews',
    component: () => import('@/views/reviews/ReviewQueueView.vue'),
    meta: {
      icon: '02',
      titleKey: 'nav.reviews',
      descriptionKey: 'nav.reviewsDescription',
      permission: 'reviews:approve'
    }
  },
  {
    path: '/settings',
    name: 'settings',
    component: () => import('@/views/settings/WorkspaceSettingsView.vue'),
    meta: {
      icon: '03',
      titleKey: 'nav.settings',
      descriptionKey: 'nav.settingsDescription',
      permission: 'settings:update'
    }
  }
]

const routes = [
  {
    path: '/',
    component: () => import('@/layouts/AppShell.vue'),
    redirect: '/dashboard',
    children: appRoutes
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'not-found',
    component: () => import('@/views/error/NotFoundView.vue'),
    meta: {
      hidden: true
    }
  }
]

export default routes
