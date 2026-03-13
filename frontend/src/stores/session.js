import { defineStore } from 'pinia'

export const useSessionStore = defineStore('session', {
  state: () => ({
    accessToken: '',
    displayNameKey: 'shell.operatorName',
    roleKey: 'shell.role',
    permissions: [
      'reviews:approve',
      'reviews:reject',
      'settings:update'
    ]
  }),
  actions: {
    setAccessToken(token) {
      this.accessToken = token
    },
    hasPermission(permission) {
      return this.permissions.includes(permission)
    }
  }
})
