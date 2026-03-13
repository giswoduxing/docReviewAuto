<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import http from '@/services/http'

const { t } = useI18n()

const users = ref([])
const organizations = ref([])
const roles = ref([])
const total = ref(0)
const loading = ref(false)
const saving = ref(false)
const errorMessage = ref('')
const successMessage = ref('')
const form = reactive({
  username: '',
  password: '',
  displayName: '',
  email: '',
  organizationId: '',
  roleIds: []
})

async function loadData() {
  loading.value = true
  errorMessage.value = ''

  try {
    const [usersResponse, organizationsResponse, rolesResponse] = await Promise.all([
      http.get('/internal/users', {
        params: {
          pageNumber: 1,
          pageSize: 20
        }
      }),
      http.get('/internal/organizations'),
      http.get('/internal/roles')
    ])

    users.value = usersResponse.data?.items || []
    total.value = usersResponse.data?.total || 0
    organizations.value = organizationsResponse.data || []
    roles.value = rolesResponse.data || []
  } catch (error) {
    errorMessage.value = error.message
  } finally {
    loading.value = false
  }
}

function resetForm() {
  form.username = ''
  form.password = ''
  form.displayName = ''
  form.email = ''
  form.organizationId = ''
  form.roleIds = []
}

async function submitForm() {
  saving.value = true
  errorMessage.value = ''
  successMessage.value = ''

  try {
    const response = await http.post('/internal/users', {
      username: form.username.trim(),
      password: form.password,
      displayName: form.displayName.trim(),
      email: form.email.trim(),
      organizationId: form.organizationId,
      roleIds: form.roleIds
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
        <h3>{{ t('users.form.title') }}</h3>
      </div>

      <div class="stack-list">
        <p>{{ t('users.form.description') }}</p>

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
            <span class="field__label">{{ t('users.form.username') }}</span>
            <input
              v-model="form.username"
              class="field__control"
              type="text"
              :placeholder="t('users.form.usernamePlaceholder')"
            >
          </label>

          <label class="field">
            <span class="field__label">{{ t('users.form.password') }}</span>
            <input
              v-model="form.password"
              class="field__control"
              type="password"
              :placeholder="t('users.form.passwordPlaceholder')"
            >
          </label>

          <label class="field">
            <span class="field__label">{{ t('users.form.displayName') }}</span>
            <input
              v-model="form.displayName"
              class="field__control"
              type="text"
              :placeholder="t('users.form.displayNamePlaceholder')"
            >
          </label>

          <label class="field">
            <span class="field__label">{{ t('users.form.email') }}</span>
            <input
              v-model="form.email"
              class="field__control"
              type="email"
              :placeholder="t('users.form.emailPlaceholder')"
            >
          </label>

          <label class="field">
            <span class="field__label">{{ t('users.form.organization') }}</span>
            <select
              v-model="form.organizationId"
              class="field__control"
            >
              <option value="">
                {{ t('users.form.organizationPlaceholder') }}
              </option>
              <option
                v-for="item in organizations"
                :key="item.id"
                :value="item.id"
              >
                {{ item.name }}
              </option>
            </select>
          </label>

          <div class="field field--full">
            <span class="field__label">{{ t('users.form.roles') }}</span>
            <div class="checkbox-grid">
              <label
                v-for="role in roles"
                :key="role.id"
                class="checkbox-card"
                :class="{ 'is-active': form.roleIds.includes(role.id) }"
              >
                <input
                  v-model="form.roleIds"
                  type="checkbox"
                  :value="role.id"
                >
                <strong>{{ role.name }}</strong>
                <span>{{ role.code }}</span>
              </label>
            </div>
          </div>

          <div class="button-row">
            <button
              type="submit"
              class="primary-button"
              :disabled="saving"
            >
              {{ saving ? t('common.saving') : t('users.form.submit') }}
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
        <h3>{{ t('users.list.title', { total }) }}</h3>
      </div>

      <div
        v-if="loading"
        class="notice notice--soft"
      >
        {{ t('common.loading') }}
      </div>

      <div
        v-else-if="!users.length"
        class="empty-state-panel"
      >
        <strong>{{ t('users.list.emptyTitle') }}</strong>
        <span>{{ t('users.list.emptyDescription') }}</span>
      </div>

      <div
        v-else
        class="table-shell"
      >
        <table class="data-table">
          <thead>
            <tr>
              <th>{{ t('users.table.username') }}</th>
              <th>{{ t('users.table.displayName') }}</th>
              <th>{{ t('users.table.organization') }}</th>
              <th>{{ t('users.table.roles') }}</th>
              <th>{{ t('users.table.status') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="user in users"
              :key="user.id"
            >
              <td>{{ user.username }}</td>
              <td>{{ user.displayName }}</td>
              <td>{{ user.organizationName || '--' }}</td>
              <td>{{ user.roleNames.join(', ') || '--' }}</td>
              <td>{{ user.status }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </article>
  </section>
</template>
