import axios from 'axios'
import { i18n } from '@/plugins/i18n'
import { useSessionStore } from '@/stores/session'

function normalizeError(error) {
  return {
    status: error.response?.status || 500,
    message: error.response?.data?.message || error.message || i18n.global.t('request.error.unknown'),
    details: error.response?.data || null
  }
}

const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 12000,
  withCredentials: true
})

http.interceptors.request.use((config) => {
  const sessionStore = useSessionStore()

  if (sessionStore.accessToken) {
    config.headers.Authorization = `Bearer ${sessionStore.accessToken}`
  }

  return config
})

http.interceptors.response.use(
  (response) => response.data,
  (error) => Promise.reject(normalizeError(error))
)

export default http
