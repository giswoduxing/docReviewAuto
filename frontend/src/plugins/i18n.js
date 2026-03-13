import { createI18n } from 'vue-i18n'
import zhCN from '@/locales/zh-CN'
import enUS from '@/locales/en-US'

export const supportedLocales = ['zh-CN', 'en-US']

export const i18n = createI18n({
  legacy: false,
  locale: supportedLocales[0],
  fallbackLocale: supportedLocales[1],
  messages: {
    'zh-CN': zhCN,
    'en-US': enUS
  }
})

export function setLocale(nextLocale) {
  i18n.global.locale.value = nextLocale
}
