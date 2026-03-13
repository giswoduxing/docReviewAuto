import { defineStore } from 'pinia'
import { setLocale, supportedLocales } from '@/plugins/i18n'

const defaultLocale = supportedLocales[0]

export const useAppStore = defineStore('app', {
  state: () => ({
    locale: defaultLocale,
    sidebarCollapsed: false,
    accentMode: 'sunrise'
  }),
  actions: {
    updateLocale(nextLocale) {
      if (!supportedLocales.includes(nextLocale)) {
        return
      }

      this.locale = nextLocale
      setLocale(nextLocale)
    },
    toggleSidebar() {
      this.sidebarCollapsed = !this.sidebarCollapsed
    },
    setAccentMode(nextMode) {
      this.accentMode = nextMode
    }
  }
})
