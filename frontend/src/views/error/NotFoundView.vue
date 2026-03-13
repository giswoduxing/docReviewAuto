<script setup>
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
</script>

<template>
  <section class="panel empty-state-panel">
    <span class="eyebrow">404</span>
    <h2>{{ t('notFound.title') }}</h2>
    <p>{{ t('notFound.description') }}</p>
    <RouterLink
      class="primary-button"
      :to="{ name: 'dashboard' }"
    >
      {{ t('notFound.action') }}
    </RouterLink>
  </section>
</template>
