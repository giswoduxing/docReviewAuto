<script setup>
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useAppStore } from '@/stores/app'
import { useSessionStore } from '@/stores/session'

const { t } = useI18n()
const appStore = useAppStore()
const sessionStore = useSessionStore()

function getLocaleLabel(locale) {
  return locale === 'zh-CN' ? t('shell.locales.zhCN') : t('shell.locales.enUS')
}

const runtimeRows = computed(() => [
  {
    label: t('settings.runtime.apiBase'),
    value: import.meta.env.VITE_API_BASE_URL
  },
  {
    label: t('settings.runtime.appTitle'),
    value: import.meta.env.VITE_APP_TITLE
  },
  {
    label: t('settings.runtime.locale'),
    value: getLocaleLabel(appStore.locale)
  },
  {
    label: t('settings.runtime.sidebar'),
    value: appStore.sidebarCollapsed ? t('common.compact') : t('common.expanded')
  },
  {
    label: t('settings.runtime.theme'),
    value: t(`shell.theme.${appStore.accentMode}`)
  }
])

const modules = computed(() => [
  {
    label: t('settings.modules.router'),
    value: t('tech.vueRouter')
  },
  {
    label: t('settings.modules.state'),
    value: t('tech.pinia')
  },
  {
    label: t('settings.modules.request'),
    value: t('tech.axios')
  },
  {
    label: t('settings.modules.i18n'),
    value: t('tech.vueI18n')
  },
  {
    label: t('settings.modules.permission'),
    value: t('tech.permissionDirective')
  }
])

const conventions = computed(() => [
  t('settings.conventions.first'),
  t('settings.conventions.second'),
  t('settings.conventions.third')
])

const localeOptions = [
  {
    code: 'zh-CN',
    label: 'ZH'
  },
  {
    code: 'en-US',
    label: 'EN'
  }
]

const themeOptions = [
  {
    code: 'sunrise',
    labelKey: 'shell.theme.sunrise'
  },
  {
    code: 'slate',
    labelKey: 'shell.theme.slate'
  }
]

function toggleSidebar() {
  appStore.toggleSidebar()
}

function selectLocale(locale) {
  appStore.updateLocale(locale)
}

function selectTheme(theme) {
  appStore.setAccentMode(theme)
}
</script>

<template>
  <section class="panel-grid panel-grid--two-up">
    <article class="panel">
      <div class="panel__header">
        <div>
          <h2>{{ t('settings.title') }}</h2>
          <p>{{ t('settings.description') }}</p>
        </div>
      </div>

      <div class="settings-list">
        <div
          v-for="item in runtimeRows"
          :key="item.label"
          class="settings-list__row"
        >
          <span>{{ item.label }}</span>
          <strong>{{ item.value }}</strong>
        </div>
      </div>
    </article>

    <article class="panel">
      <div class="panel__header">
        <h3>{{ t('settings.modules.title') }}</h3>
      </div>

      <div class="settings-list">
        <div
          v-for="item in modules"
          :key="item.label"
          class="settings-list__row"
        >
          <span>{{ item.label }}</span>
          <strong>{{ item.value }}</strong>
        </div>
      </div>
    </article>
  </section>

  <section class="panel-grid panel-grid--two-up">
    <article class="panel">
      <div class="panel__header">
        <h3>{{ t('settings.controls.title') }}</h3>
      </div>

      <div class="stack-list">
        <div class="control-block">
          <strong>{{ t('settings.controls.compact') }}</strong>
          <button
            type="button"
            class="ghost-button"
            @click="toggleSidebar"
          >
            {{ appStore.sidebarCollapsed ? t('common.expanded') : t('common.compact') }}
          </button>
        </div>

        <div class="control-block">
          <strong>{{ t('settings.controls.locale') }}</strong>
          <div class="button-row">
            <button
              v-for="option in localeOptions"
              :key="option.code"
              type="button"
              class="ghost-button"
              :class="{ 'is-active': appStore.locale === option.code }"
              @click="selectLocale(option.code)"
            >
              {{ option.label }}
            </button>
          </div>
        </div>

        <div class="control-block">
          <strong>{{ t('settings.controls.theme') }}</strong>
          <div class="button-row">
            <button
              v-for="option in themeOptions"
              :key="option.code"
              type="button"
              class="ghost-button"
              :class="{ 'is-active': appStore.accentMode === option.code }"
              @click="selectTheme(option.code)"
            >
              {{ t(option.labelKey) }}
            </button>
          </div>
        </div>
      </div>
    </article>

    <article class="panel">
      <div class="panel__header">
        <h3>{{ t('settings.permissions.title') }}</h3>
      </div>

      <div class="tag-row">
        <span
          v-for="permission in sessionStore.permissions"
          :key="permission"
          class="tag"
        >
          {{ permission }}
        </span>
      </div>

      <div class="panel__header panel__header--flush">
        <h3>{{ t('settings.conventions.title') }}</h3>
      </div>
      <div class="checklist">
        <div
          v-for="item in conventions"
          :key="item"
          class="checklist__item"
        >
          <span class="checklist__mark">+</span>
          <span>{{ item }}</span>
        </div>
      </div>
    </article>
  </section>
</template>
