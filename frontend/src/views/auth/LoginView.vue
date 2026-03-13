<script setup>
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { useAppStore } from '@/stores/app'
import { useSessionStore } from '@/stores/session'

const router = useRouter()
const route = useRoute()
const { t } = useI18n()
const appStore = useAppStore()
const sessionStore = useSessionStore()

const credentials = reactive({
  username: 'admin',
  password: ''
})
const errorMessage = ref('')

const localeOptions = [
  { code: 'zh-CN', short: 'ZH' },
  { code: 'en-US', short: 'EN' }
]

async function submitLogin() {
  errorMessage.value = ''

  try {
    await sessionStore.login(credentials)
    await router.replace(typeof route.query.redirect === 'string' ? route.query.redirect : { name: 'dashboard' })
  } catch (error) {
    errorMessage.value = error.message
  }
}
</script>

<template>
  <section class="auth-page">
    <div class="auth-card">
      <div class="auth-card__intro">
        <span class="eyebrow">{{ t('login.eyebrow') }}</span>
        <h1>{{ t('login.title') }}</h1>
        <p>{{ t('login.subtitle') }}</p>

        <div class="locale-switch">
          <button
            v-for="option in localeOptions"
            :key="option.code"
            type="button"
            class="locale-switch__button"
            :class="{ 'is-active': appStore.locale === option.code }"
            @click="appStore.updateLocale(option.code)"
          >
            {{ option.short }}
          </button>
        </div>
      </div>

      <form
        class="auth-form"
        @submit.prevent="submitLogin"
      >
        <div
          v-if="errorMessage"
          class="notice notice--danger"
        >
          {{ errorMessage }}
        </div>

        <label class="field">
          <span class="field__label">{{ t('login.form.username') }}</span>
          <input
            v-model="credentials.username"
            class="field__control"
            type="text"
            autocomplete="username"
            :placeholder="t('login.form.usernamePlaceholder')"
          >
        </label>

        <label class="field">
          <span class="field__label">{{ t('login.form.password') }}</span>
          <input
            v-model="credentials.password"
            class="field__control"
            type="password"
            autocomplete="current-password"
            :placeholder="t('login.form.passwordPlaceholder')"
          >
        </label>

        <button
          type="submit"
          class="primary-button auth-form__submit"
          :disabled="sessionStore.submittingLogin"
        >
          {{ sessionStore.submittingLogin ? t('common.loading') : t('login.form.submit') }}
        </button>

        <div class="notice notice--soft">
          <strong>{{ t('login.defaultAccount.title') }}</strong>
          <span>{{ t('login.defaultAccount.description') }}</span>
        </div>

        <div class="notice notice--soft">
          <strong>{{ t('login.lockout.title') }}</strong>
          <span>{{ t('login.lockout.description') }}</span>
        </div>
      </form>
    </div>
  </section>
</template>
