<script setup>
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'
import KpiCard from '@/components/dashboard/KpiCard.vue'

const { t } = useI18n()

const metrics = computed(() => [
  {
    label: t('dashboard.metrics.pending.label'),
    value: '18',
    hint: t('dashboard.metrics.pending.hint'),
    tone: 'accent'
  },
  {
    label: t('dashboard.metrics.velocity.label'),
    value: '6 h',
    hint: t('dashboard.metrics.velocity.hint'),
    tone: 'sun'
  },
  {
    label: t('dashboard.metrics.coverage.label'),
    value: '100%',
    hint: t('dashboard.metrics.coverage.hint'),
    tone: 'neutral'
  },
  {
    label: t('dashboard.metrics.latency.label'),
    value: '12 s',
    hint: t('dashboard.metrics.latency.hint'),
    tone: 'neutral'
  }
])

const stages = computed(() => [
  {
    title: t('dashboard.stages.intake.title'),
    description: t('dashboard.stages.intake.description'),
    badge: t('common.active')
  },
  {
    title: t('dashboard.stages.workspace.title'),
    description: t('dashboard.stages.workspace.description'),
    badge: t('common.stable')
  },
  {
    title: t('dashboard.stages.service.title'),
    description: t('dashboard.stages.service.description'),
    badge: t('common.planned')
  }
])

const activities = computed(() => [
  {
    title: t('dashboard.activity.first.title'),
    meta: t('dashboard.activity.first.meta')
  },
  {
    title: t('dashboard.activity.second.title'),
    meta: t('dashboard.activity.second.meta')
  },
  {
    title: t('dashboard.activity.third.title'),
    meta: t('dashboard.activity.third.meta')
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
          class="primary-button"
          :to="{ name: 'reviews' }"
        >
          {{ t('dashboard.hero.primaryAction') }}
        </RouterLink>
        <RouterLink
          class="ghost-button"
          :to="{ name: 'settings' }"
        >
          {{ t('dashboard.hero.secondaryAction') }}
        </RouterLink>
      </div>
    </div>

    <div class="hero-card__aside">
      <div class="hero-card__ring">
        <strong>{{ t('tech.vue3') }}</strong>
        <span>{{ t('tech.viteJs') }}</span>
      </div>
      <div class="hero-card__stack">
        <span>{{ t('tech.pinia') }}</span>
        <span>{{ t('tech.router') }}</span>
        <span>{{ t('tech.i18n') }}</span>
        <span>{{ t('tech.axios') }}</span>
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
        <h3>{{ t('dashboard.stages.title') }}</h3>
      </div>
      <div class="stack-list">
        <div
          v-for="stage in stages"
          :key="stage.title"
          class="stage-card"
        >
          <div class="stage-card__header">
            <strong>{{ stage.title }}</strong>
            <span class="status-pill">{{ stage.badge }}</span>
          </div>
          <p>{{ stage.description }}</p>
        </div>
      </div>
    </article>

    <article class="panel">
      <div class="panel__header">
        <h3>{{ t('dashboard.activity.title') }}</h3>
      </div>
      <div class="stack-list">
        <div
          v-for="item in activities"
          :key="item.title"
          class="timeline-item"
        >
          <strong>{{ item.title }}</strong>
          <p>{{ item.meta }}</p>
        </div>
      </div>
    </article>
  </section>

  <section class="panel">
    <div class="panel__header">
      <h3>{{ t('dashboard.checklist.title') }}</h3>
    </div>
    <div class="checklist">
      <div
        v-for="item in checklist"
        :key="item"
        class="checklist__item"
      >
        <span class="checklist__mark">+</span>
        <span>{{ item }}</span>
      </div>
    </div>
  </section>
</template>
