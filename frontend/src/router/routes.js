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
    path: '/organizations',
    name: 'organizations',
    component: () => import('@/views/organizations/OrganizationManagementView.vue'),
    meta: {
      icon: '02',
      titleKey: 'nav.organizations',
      descriptionKey: 'nav.organizationsDescription',
      permission: 'organization:manage'
    }
  },
  {
    path: '/roles',
    name: 'roles',
    component: () => import('@/views/roles/RoleManagementView.vue'),
    meta: {
      icon: '03',
      titleKey: 'nav.roles',
      descriptionKey: 'nav.rolesDescription',
      permission: 'role:manage'
    }
  },
  {
    path: '/users',
    name: 'users',
    component: () => import('@/views/users/UserManagementView.vue'),
    meta: {
      icon: '04',
      titleKey: 'nav.users',
      descriptionKey: 'nav.usersDescription',
      permission: 'user:manage'
    }
  }
]

export function findDefaultAppRoute(sessionStore) {
  const accessibleRoute = appRoutes.find((route) =>
    !route.meta?.permission || sessionStore.hasPermission(route.meta.permission)
  )

  return accessibleRoute?.name || 'dashboard'
}

const routes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/auth/LoginView.vue'),
    meta: {
      public: true,
      guestOnly: true,
      titleKey: 'login.title',
      descriptionKey: 'login.subtitle'
    }
  },
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
      hidden: true,
      public: true
    }
  }
]

export default routes
