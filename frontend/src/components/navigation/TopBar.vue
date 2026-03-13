<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { useAppStore } from '@/stores/app'
import { useSessionStore } from '@/stores/session'

const route = useRoute()
const { t } = useI18n()
const appStore = useAppStore()
const sessionStore = useSessionStore()

const localeOptions = [
  {
    code: 'zh-CN',
    short: 'ZH'
  },
  {
    code: 'en-US',
    short: 'EN'
  }
]

const pageTitle = computed(() =>
  route.meta?.titleKey ? t(route.meta.titleKey) : t('app.title')
)

const pageDescription = computed(() =>
  route.meta?.descriptionKey ? t(route.meta.descriptionKey) : t('app.subtitle')
)

function toggleSidebar() {
  appStore.toggleSidebar()
}

function selectLocale(locale) {
  appStore.updateLocale(locale)
}
</script>

<template>
  <header class="topbar">
    <div>
      <span class="eyebrow">{{ t('shell.workspaceEyebrow') }}</span>
      <h1>{{ pageTitle }}</h1>
      <p>{{ pageDescription }}</p>
    </div>

    <div class="topbar__actions">
      <button
        type="button"
        class="ghost-button"
        @click="toggleSidebar"
      >
        {{ t('shell.toggleSidebar') }}
      </button>

      <div class="locale-switch">
        <button
          v-for="option in localeOptions"
          :key="option.code"
          type="button"
          class="locale-switch__button"
          :class="{ 'is-active': appStore.locale === option.code }"
          @click="selectLocale(option.code)"
        >
          {{ option.short }}
        </button>
      </div>

      <div class="profile-chip">
        <span class="profile-chip__avatar">
          {{ t(sessionStore.displayNameKey).slice(0, 1) }}
        </span>
        <div>
          <strong>{{ t(sessionStore.displayNameKey) }}</strong>
          <span>{{ t(sessionStore.roleKey) }}</span>
        </div>
      </div>
    </div>
  </header>
</template>
