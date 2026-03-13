<script setup>
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'
import KpiCard from '@/components/dashboard/KpiCard.vue'
import { appRoutes } from '@/router/routes'
import { useSessionStore } from '@/stores/session'

const { t } = useI18n()
const sessionStore = useSessionStore()

const quickLinks = computed(() =>
  appRoutes.filter((route) =>
    route.name !== 'dashboard' && (!route.meta?.permission || sessionStore.hasPermission(route.meta.permission))
  )
)

const metrics = computed(() => [
  {
    label: t('dashboard.metrics.identity.label'),
    value: sessionStore.displayName || '--',
    hint: t('dashboard.metrics.identity.hint'),
    tone: 'accent'
  },
  {
    label: t('dashboard.metrics.roles.label'),
    value: String(sessionStore.profile?.roleNames?.length || 0),
    hint: t('dashboard.metrics.roles.hint'),
    tone: 'sun'
  },
  {
    label: t('dashboard.metrics.permissions.label'),
    value: String(sessionStore.permissions.length),
    hint: t('dashboard.metrics.permissions.hint'),
    tone: 'neutral'
  },
  {
    label: t('dashboard.metrics.lockout.label'),
    value: t('dashboard.metrics.lockout.value'),
    hint: t('dashboard.metrics.lockout.hint'),
    tone: 'neutral'
  }
])

const checklist = computed(() => [
  t('dashboard.checklist.first'),
  t('dashboard.checklist.second'),
  t('dashboard.checklist.third')
])
</script>

<template>
  <section class="hero-card">
    <div class="hero-card__body">
      <span class="eyebrow">{{ t('dashboard.hero.eyebrow') }}</span>
      <h2>{{ t('dashboard.hero.title') }}</h2>
      <p>{{ t('dashboard.hero.description') }}</p>

      <div class="button-row">
        <RouterLink
          v-for="item in quickLinks"
          :key="item.name"
          class="primary-button"
          :to="{ name: item.name }"
        >
          {{ t(item.meta.titleKey) }}
        </RouterLink>
      </div>
    </div>

    <div class="hero-card__aside">
      <div class="hero-card__ring">
        <strong>{{ sessionStore.organizationName || t('dashboard.hero.emptyOrganization') }}</strong>
        <span>{{ sessionStore.roleLabel || t('shell.noRole') }}</span>
      </div>
      <div class="hero-card__stack">
        <span
          v-for="permission in sessionStore.permissions"
          :key="permission"
        >
          {{ permission }}
        </span>
      </div>
    </div>
  </section>

  <section class="panel-grid panel-grid--metrics">
    <KpiCard
      v-for="item in metrics"
      :key="item.label"
      :hint="item.hint"
      :label="item.label"
      :tone="item.tone"
      :value="item.value"
    />
  </section>

  <section class="panel-grid panel-grid--two-up">
    <article class="panel">
      <div class="panel__header">
        <h3>{{ t('dashboard.quickStart.title') }}</h3>
      </div>
      <div class="stack-list">
        <div
          v-for="item in checklist"
          :key="item"
          class="stage-card"
        >
          <strong>{{ item }}</strong>
        </div>
      </div>
    </article>

    <article class="panel">
      <div class="panel__header">
        <h3>{{ t('dashboard.session.title') }}</h3>
      </div>
      <div class="stack-list">
        <div class="timeline-item">
          <strong>{{ t('dashboard.session.username') }}</strong>
          <p>{{ sessionStore.profile?.username || '--' }}</p>
        </div>
        <div class="timeline-item">
          <strong>{{ t('dashboard.session.roles') }}</strong>
          <p>{{ sessionStore.roleLabel || t('shell.noRole') }}</p>
        </div>
        <div class="timeline-item">
          <strong>{{ t('dashboard.session.permissions') }}</strong>
          <p>{{ sessionStore.permissions.join(', ') || '--' }}</p>
        </div>
      </div>
    </article>
  </section>
</template>
