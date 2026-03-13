<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import http from '@/services/http'

const { t } = useI18n()

const roles = ref([])
const permissionOptions = ref([])
const loading = ref(false)
const saving = ref(false)
const errorMessage = ref('')
const successMessage = ref('')
const form = reactive({
  code: '',
  name: '',
  description: '',
  permissionCodes: []
})

async function loadData() {
  loading.value = true
  errorMessage.value = ''

  try {
    const [rolesResponse, permissionsResponse] = await Promise.all([
      http.get('/internal/roles'),
      http.get('/internal/roles/permissions')
    ])
    roles.value = rolesResponse.data || []
    permissionOptions.value = permissionsResponse.data || []
  } catch (error) {
    errorMessage.value = error.message
  } finally {
    loading.value = false
  }
}

function resetForm() {
  form.code = ''
  form.name = ''
  form.description = ''
  form.permissionCodes = []
}

async function submitForm() {
  saving.value = true
  errorMessage.value = ''
  successMessage.value = ''

  try {
    const response = await http.post('/internal/roles', {
      code: form.code.trim().toUpperCase(),
      name: form.name.trim(),
      description: form.description.trim(),
      permissionCodes: form.permissionCodes
    })
    successMessage.value = response.message
    resetForm()
    await loadData()
  } catch (error) {
    errorMessage.value = error.message
  } finally {
    saving.value = false
  }
}

onMounted(loadData)
</script>

<template>
  <section class="panel-grid panel-grid--two-up">
    <article class="panel">
      <div class="panel__header">
        <h3>{{ t('roles.form.title') }}</h3>
      </div>

      <div class="stack-list">
        <p>{{ t('roles.form.description') }}</p>

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
            <span class="field__label">{{ t('roles.form.code') }}</span>
            <input
              v-model="form.code"
              class="field__control"
              type="text"
              :placeholder="t('roles.form.codePlaceholder')"
            >
          </label>

          <label class="field">
            <span class="field__label">{{ t('roles.form.name') }}</span>
            <input
              v-model="form.name"
              class="field__control"
              type="text"
              :placeholder="t('roles.form.namePlaceholder')"
            >
          </label>

          <label class="field field--full">
            <span class="field__label">{{ t('roles.form.descriptionLabel') }}</span>
            <textarea
              v-model="form.description"
              class="field__control field__control--textarea"
              :placeholder="t('roles.form.descriptionPlaceholder')"
            />
          </label>

          <div class="field field--full">
            <span class="field__label">{{ t('roles.form.permissions') }}</span>
            <div class="checkbox-grid">
              <label
                v-for="option in permissionOptions"
                :key="option.code"
                class="checkbox-card"
                :class="{ 'is-active': form.permissionCodes.includes(option.code) }"
              >
                <input
                  v-model="form.permissionCodes"
                  type="checkbox"
                  :value="option.code"
                >
                <strong>{{ t(option.titleKey) }}</strong>
                <span>{{ t(option.descriptionKey) }}</span>
              </label>
            </div>
          </div>

          <div class="button-row">
            <button
              type="submit"
              class="primary-button"
              :disabled="saving"
            >
              {{ saving ? t('common.saving') : t('roles.form.submit') }}
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
        <h3>{{ t('roles.list.title') }}</h3>
      </div>

      <div
        v-if="loading"
        class="notice notice--soft"
      >
        {{ t('common.loading') }}
      </div>

      <div
        v-else-if="!roles.length"
        class="empty-state-panel"
      >
        <strong>{{ t('roles.list.emptyTitle') }}</strong>
        <span>{{ t('roles.list.emptyDescription') }}</span>
      </div>

      <div
        v-else
        class="stack-list"
      >
        <article
          v-for="role in roles"
          :key="role.id"
          class="stage-card"
        >
          <div class="stage-card__header">
            <strong>{{ role.name }} ({{ role.code }})</strong>
            <span class="status-pill">{{ role.builtIn ? t('roles.list.builtIn') : role.status }}</span>
          </div>
          <p>{{ role.description || t('roles.list.noDescription') }}</p>
          <div class="hero-card__stack">
            <span
              v-for="permission in role.permissionCodes"
              :key="permission"
            >
              {{ permission }}
            </span>
          </div>
        </article>
      </div>
    </article>
  </section>
</template>
