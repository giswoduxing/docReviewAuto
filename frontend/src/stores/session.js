import { defineStore } from 'pinia'
import http from '@/services/http'

function normalizeSession(payload) {
  return {
    userId: payload?.userId || null,
    username: payload?.username || '',
    displayName: payload?.displayName || payload?.username || '',
    organizationName: payload?.organizationName || '',
    roleCodes: Array.isArray(payload?.roleCodes) ? payload.roleCodes : [],
    roleNames: Array.isArray(payload?.roleNames) ? payload.roleNames : [],
    permissionCodes: Array.isArray(payload?.permissionCodes) ? payload.permissionCodes : []
  }
}

export const useSessionStore = defineStore('session', {
  state: () => ({
    initialized: false,
    loadingSession: false,
    submittingLogin: false,
    profile: null,
    lastError: ''
  }),
  getters: {
    isAuthenticated: (state) => Boolean(state.profile),
    displayName: (state) => state.profile?.displayName || '',
    roleLabel: (state) => state.profile?.roleNames?.join(' / ') || '',
    permissions: (state) => state.profile?.permissionCodes || [],
    organizationName: (state) => state.profile?.organizationName || ''
  },
  actions: {
    applySession(payload) {
      this.profile = normalizeSession(payload)
      this.initialized = true
      this.lastError = ''
    },
    clearSession() {
      this.profile = null
      this.initialized = true
      this.lastError = ''
    },
    hasPermission(permission) {
      return !permission || this.permissions.includes(permission)
    },
    async initializeSession(force = false) {
      if (this.initialized && !force) {
        return this.profile
      }

      if (this.loadingSession) {
        return this.profile
      }

      this.loadingSession = true

      try {
        const response = await http.get('/internal/auth/session')
        this.applySession(response.data)
        return this.profile
      } catch (error) {
        if (error.status === 401) {
          this.clearSession()
          return null
        }

        this.lastError = error.message
        this.initialized = true
        throw error
      } finally {
        this.loadingSession = false
      }
    },
    async login(credentials) {
      this.submittingLogin = true
      this.lastError = ''

      try {
        const response = await http.post('/public/auth/login', credentials)
        this.applySession(response.data)
        return this.profile
      } catch (error) {
        this.profile = null
        this.initialized = true
        this.lastError = error.message
        throw error
      } finally {
        this.submittingLogin = false
      }
    },
    async logout() {
      try {
        if (this.isAuthenticated) {
          await http.post('/internal/auth/logout')
        }
      } finally {
        this.clearSession()
      }
    }
  }
})
