<script setup>
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
const activeFilter = ref('all')
const lastActionKey = ref('')

const queue = ref([
  {
    id: 'RV-1001',
    titleKey: 'reviews.items.first.title',
    ownerKey: 'reviews.items.first.owner',
    type: 'PDF',
    submittedAt: '09:30',
    sla: '45m',
    risk: 'high',
    status: 'pending',
    expiringSoon: true,
    tags: ['reviews.tags.policy', 'reviews.tags.urgent']
  },
  {
    id: 'RV-1002',
    titleKey: 'reviews.items.second.title',
    ownerKey: 'reviews.items.second.owner',
    type: 'DOCX',
    submittedAt: '11:15',
    sla: '3h',
    risk: 'medium',
    status: 'pending',
    expiringSoon: false,
    tags: ['reviews.tags.contract', 'reviews.tags.external']
  },
  {
    id: 'RV-1003',
    titleKey: 'reviews.items.third.title',
    ownerKey: 'reviews.items.third.owner',
    type: 'Markdown',
    submittedAt: '14:05',
    sla: '6h',
    risk: 'low',
    status: 'pending',
    expiringSoon: false,
    tags: ['reviews.tags.privacy']
  }
])

const filters = computed(() => [
  {
    id: 'all',
    label: t('reviews.filters.all')
  },
  {
    id: 'high',
    label: t('reviews.filters.highRisk')
  },
  {
    id: 'expiring',
    label: t('reviews.filters.expiring')
  }
])

const pendingCount = computed(() =>
  queue.value.filter((item) => item.status === 'pending').length
)

const urgentCount = computed(() =>
  queue.value.filter((item) => item.expiringSoon && item.status === 'pending').length
)

const visibleItems = computed(() =>
  queue.value.filter((item) => {
    if (activeFilter.value === 'high') {
      return item.risk === 'high' && item.status === 'pending'
    }

    if (activeFilter.value === 'expiring') {
      return item.expiringSoon && item.status === 'pending'
    }

    return item.status === 'pending'
  })
)

function resolveItem(id, nextStatus) {
  const target = queue.value.find((item) => item.id === id)

  if (!target) {
    return
  }

  target.status = nextStatus
  lastActionKey.value = `reviews.feedback.${nextStatus}`
}
</script>

<template>
  <section class="panel">
    <div class="panel__header panel__header--spread">
      <div>
        <h2>{{ t('reviews.title') }}</h2>
        <p>{{ t('reviews.summary') }}</p>
      </div>
      <div class="review-stats">
        <div class="stat-chip">
          <span>{{ t('reviews.stats.total') }}</span>
          <strong>{{ pendingCount }}</strong>
        </div>
        <div class="stat-chip">
          <span>{{ t('reviews.stats.urgent') }}</span>
          <strong>{{ urgentCount }}</strong>
        </div>
        <div class="stat-chip">
          <span>{{ t('reviews.stats.permissions') }}</span>
          <strong>3</strong>
        </div>
      </div>
    </div>

    <div class="filter-row">
      <button
        v-for="filter in filters"
        :key="filter.id"
        type="button"
        class="filter-pill"
        :class="{ 'is-active': activeFilter === filter.id }"
        @click="activeFilter = filter.id"
      >
        {{ filter.label }}
      </button>
    </div>

    <p
      v-if="lastActionKey"
      class="inline-banner"
    >
      {{ t(lastActionKey) }}
    </p>

    <div
      v-if="visibleItems.length"
      class="queue-list"
    >
      <article
        v-for="item in visibleItems"
        :key="item.id"
        class="queue-card"
      >
        <div class="queue-card__header">
          <div>
            <span class="eyebrow">{{ item.id }}</span>
            <h3>{{ t(item.titleKey) }}</h3>
          </div>
          <div class="queue-card__pills">
            <span :class="['chip', `chip--${item.risk}`]">
              {{ t(`reviews.risk.${item.risk}`) }}
            </span>
            <span :class="['chip', `chip--${item.status}`]">
              {{ t(`reviews.status.${item.status}`) }}
            </span>
          </div>
        </div>

        <div class="queue-card__meta">
          <div>
            <span>{{ t('reviews.card.owner') }}</span>
            <strong>{{ t(item.ownerKey) }}</strong>
          </div>
          <div>
            <span>{{ t('reviews.card.type') }}</span>
            <strong>{{ item.type }}</strong>
          </div>
          <div>
            <span>{{ t('reviews.card.submitted') }}</span>
            <strong>{{ item.submittedAt }}</strong>
          </div>
          <div>
            <span>{{ t('reviews.card.sla') }}</span>
            <strong>{{ item.sla }}</strong>
          </div>
        </div>

        <div class="tag-row">
          <span
            v-for="tag in item.tags"
            :key="tag"
            class="tag"
          >
            {{ t(tag) }}
          </span>
        </div>

        <div class="button-row">
          <button
            v-permission="'reviews:approve'"
            type="button"
            class="primary-button"
            @click="resolveItem(item.id, 'approved')"
          >
            {{ t('common.approve') }}
          </button>
          <button
            v-permission="'reviews:reject'"
            type="button"
            class="ghost-button"
            @click="resolveItem(item.id, 'rejected')"
          >
            {{ t('common.reject') }}
          </button>
          <button
            type="button"
            class="ghost-button"
            @click="resolveItem(item.id, 'escalated')"
          >
            {{ t('common.escalate') }}
          </button>
        </div>
      </article>
    </div>

    <p
      v-else
      class="empty-state"
    >
      {{ t('reviews.empty') }}
    </p>
  </section>
</template>
