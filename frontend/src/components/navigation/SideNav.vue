<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { appRoutes } from '@/router/routes'
import { useAppStore } from '@/stores/app'
import { useSessionStore } from '@/stores/session'

const route = useRoute()
const { t } = useI18n()
const appStore = useAppStore()
const sessionStore = useSessionStore()

const items = computed(() =>
  appRoutes
    .filter((item) => !item.meta?.permission || sessionStore.hasPermission(item.meta.permission))
    .map((item) => ({
      name: item.name,
      icon: item.meta.icon,
      title: t(item.meta.titleKey),
      description: t(item.meta.descriptionKey)
    }))
)
</script>

<template>
  <aside
    class="sidebar"
    :class="{ 'sidebar--collapsed': appStore.sidebarCollapsed }"
  >
    <div class="sidebar__brand">
      <span class="eyebrow">{{ t('shell.brandEyebrow') }}</span>
      <strong>{{ t('app.title') }}</strong>
      <p>{{ t('app.subtitle') }}</p>
    </div>

    <nav class="sidebar__nav">
      <RouterLink
        v-for="item in items"
        :key="item.name"
        :to="{ name: item.name }"
        class="sidebar__link"
        :class="{ 'is-active': route.name === item.name }"
      >
        <span class="sidebar__icon">{{ item.icon }}</span>
        <span class="sidebar__meta">
          <span class="sidebar__label">{{ item.title }}</span>
          <span class="sidebar__caption">{{ item.description }}</span>
        </span>
      </RouterLink>
    </nav>

    <div class="sidebar__footer">
      <strong>{{ t(`shell.theme.${appStore.accentMode}`) }}</strong>
      <span>{{ t('shell.securityNote') }}</span>
    </div>
  </aside>
</template>
