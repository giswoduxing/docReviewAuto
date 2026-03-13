<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { useAppStore } from '@/stores/app'
import { useSessionStore } from '@/stores/session'

const route = useRoute()
const router = useRouter()
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

const avatarLabel = computed(() =>
  (sessionStore.displayName || t('shell.anonymous')).slice(0, 1).toUpperCase()
)

const roleLabel = computed(() =>
  sessionStore.roleLabel || t('shell.noRole')
)

function toggleSidebar() {
  appStore.toggleSidebar()
}

function selectLocale(locale) {
  appStore.updateLocale(locale)
}

async function logout() {
  await sessionStore.logout()
  await router.push({ name: 'login' })
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

      <button
        type="button"
        class="ghost-button"
        @click="logout"
      >
        {{ t('shell.logout') }}
      </button>

      <div class="profile-chip">
        <span class="profile-chip__avatar">
          {{ avatarLabel }}
        </span>
        <div>
          <strong>{{ sessionStore.displayName || t('shell.anonymous') }}</strong>
          <span>{{ roleLabel }}</span>
        </div>
      </div>
    </div>
  </header>
</template>
