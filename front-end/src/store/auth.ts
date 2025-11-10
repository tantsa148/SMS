import { defineStore } from 'pinia'
// store/auth.ts ou LoginView.vue
import type { LoginData } from '../services/authService'
import { login as loginApi } from '../services/authService'

interface AuthState {
  token: string | null
  username: string | null
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    token: localStorage.getItem('token') || null,
    username: localStorage.getItem('username') || null
  }),
  actions: {
    async login(data: LoginData) {
      const result = await loginApi(data)
      this.token = result.token
      this.username = data.username
      // persistance
      localStorage.setItem('token', result.token)
      localStorage.setItem('username', data.username)
    },
    logout() {
      this.token = null
      this.username = null
      localStorage.removeItem('token')
      localStorage.removeItem('username')
    }
  }
})
