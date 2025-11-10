<template>
  <div class="login-page">
    <h1>Connexion</h1>
    <form @submit.prevent="handleLogin">
      <div>
        <label>Nom d'utilisateur</label>
        <input type="text" v-model="username" required />
      </div>
      <div>
        <label>Mot de passe</label>
        <input type="password" v-model="password" required />
      </div>
      <button type="submit" :disabled="loading">
        {{ loading ? 'Connexion...' : 'Se connecter' }}
      </button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'

const username = ref<string>('')
const password = ref<string>('')
const error = ref<string>('')
const loading = ref<boolean>(false)

const router = useRouter()
const authStore = useAuthStore()

const handleLogin = async (): Promise<void> => {
  error.value = ''
  loading.value = true

  try {
    await authStore.login({ username: username.value, password: password.value })
    router.push('/dashboard')
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Erreur lors de la connexion'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
}
.error {
  color: red;
  margin-top: 10px;
}
</style>
