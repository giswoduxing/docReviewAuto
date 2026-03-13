<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import http from '@/services/http'

const { t } = useI18n()

const organizations = ref([])
const loading = ref(false)
const saving = ref(false)
const errorMessage = ref('')
const successMessage = ref('')
const form = reactive({
  name: '',
  code: '',
  parentId: '',
  leaderName: ''
})

const parentOptions = computed(() => organizations.value)

async function loadOrganizations() {
  loading.value = true
  errorMessage.value = ''

  try {
    const response = await http.get('/internal/organizations')
    organizations.value = response.data || []
  } catch (error) {
    errorMessage.value = error.message
  } finally {
    loading.value = false
  }
}

function resetForm() {
  form.name = ''
  form.code = ''
  form.parentId = ''
  form.leaderName = ''
}

async function submitForm() {
  saving.value = true
  errorMessage.value = ''
  successMessage.value = ''

  try {
    const response = await http.post('/internal/organizations', {
      name: form.name.trim(),
      code: form.code.trim().toUpperCase(),
      parentId: form.parentId || null,
      leaderName: form.leaderName.trim()
    })
    successMessage.value = response.message
    resetForm()
    await loadOrganizations()
  } catch (error) {
    errorMessage.value = error.message
  } finally {
    saving.value = false
  }
}

onMounted(loadOrganizations)
</script>

<template>
  <section class="panel-grid panel-grid--two-up">
    <article class="panel">
      <div class="panel__header">
        <h3>{{ t('organizations.form.title') }}</h3>
      </div>

      <div class="stack-list">
        <p>{{ t('organizations.form.description') }}</p>

        <div
          v-if="errorMessage"
          class="notice notice--danger"
        >
          {{ errorMessage }}
        </div>

        <div
          v-if="successMessage"
          class="notice notice--success"
        >
          {{ successMessage }}
        </div>

        <form
          class="form-grid"
          @submit.prevent="submitForm"
        >
          <label class="field">
            <span class="field__label">{{ t('organizations.form.name') }}</span>
            <input
              v-model="form.name"
              class="field__control"
              type="text"
              :placeholder="t('organizations.form.namePlaceholder')"
            >
          </label>

          <label class="field">
            <span class="field__label">{{ t('organizations.form.code') }}</span>
            <input
              v-model="form.code"
              class="field__control"
              type="text"
              :placeholder="t('organizations.form.codePlaceholder')"
            >
          </label>

          <label class="field">
            <span class="field__label">{{ t('organizations.form.parent') }}</span>
            <select
              v-model="form.parentId"
              class="field__control"
            >
              <option value="">
                {{ t('organizations.form.parentPlaceholder') }}
              </option>
              <option
                v-for="item in parentOptions"
                :key="item.id"
                :value="item.id"
              >
                {{ item.name }} ({{ item.code }})
              </option>
            </select>
          </label>

          <label class="field">
            <span class="field__label">{{ t('organizations.form.leader') }}</span>
            <input
              v-model="form.leaderName"
              class="field__control"
              type="text"
              :placeholder="t('organizations.form.leaderPlaceholder')"
            >
          </label>

          <div class="button-row">
            <button
              type="submit"
              class="primary-button"
              :disabled="saving"
            >
              {{ saving ? t('common.saving') : t('organizations.form.submit') }}
            </button>
            <button
              type="button"
              class="ghost-button"
              @click="resetForm"
            >
              {{ t('common.reset') }}
            </button>
          </div>
        </form>
      </div>
    </article>

    <article class="panel">
      <div class="panel__header">
        <h3>{{ t('organizations.list.title') }}</h3>
      </div>

      <div
        v-if="loading"
        class="notice notice--soft"
      >
        {{ t('common.loading') }}
      </div>

      <div
        v-else-if="!organizations.length"
        class="empty-state-panel"
      >
        <strong>{{ t('organizations.list.emptyTitle') }}</strong>
        <span>{{ t('organizations.list.emptyDescription') }}</span>
      </div>

      <div
        v-else
        class="table-shell"
      >
        <table class="data-table">
          <thead>
            <tr>
              <th>{{ t('organizations.table.name') }}</th>
              <th>{{ t('organizations.table.code') }}</th>
              <th>{{ t('organizations.table.parent') }}</th>
              <th>{{ t('organizations.table.leader') }}</th>
              <th>{{ t('organizations.table.status') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="item in organizations"
              :key="item.id"
            >
              <td>{{ item.name }}</td>
              <td>{{ item.code }}</td>
              <td>{{ item.parentName || '--' }}</td>
              <td>{{ item.leaderName || '--' }}</td>
              <td>{{ item.status }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </article>
  </section>
</template>
