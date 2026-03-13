import { useSessionStore } from '@/stores/session'

function normalizePermissions(value) {
  if (Array.isArray(value)) {
    return value
  }

  if (typeof value === 'string' && value.length > 0) {
    return [value]
  }

  return []
}

function applyPermission(el, binding) {
  const sessionStore = useSessionStore()
  const requiredPermissions = normalizePermissions(binding.value)
  const isAllowed =
    requiredPermissions.length === 0 ||
    requiredPermissions.some((permission) => sessionStore.hasPermission(permission))

  el.style.display = isAllowed ? '' : 'none'
}

export function registerDirectives(app) {
  app.directive('permission', {
    mounted(el, binding) {
      applyPermission(el, binding)
    },
    updated(el, binding) {
      applyPermission(el, binding)
    }
  })
}
