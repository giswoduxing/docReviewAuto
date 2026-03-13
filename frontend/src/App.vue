<script setup>
import { watchEffect } from 'vue'
import { useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'

const route = useRoute()
const { locale, t } = useI18n()

watchEffect(() => {
  const currentLocale = locale.value
  const titleKey = route.meta?.titleKey
  const pageTitle = titleKey ? t(titleKey) : t('app.title')
  const appTitle = import.meta.env.VITE_APP_TITLE || t('app.title')

  document.documentElement.lang = currentLocale
  document.title = `${pageTitle} | ${appTitle}`
})
</script>

<template>
  <RouterView />
</template>
