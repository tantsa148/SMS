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
import '../assets/css/login.css'

const username = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)

const router = useRouter()
const authStore = useAuthStore()

const handleLogin = async (): Promise<void> => {
  error.value = ''
  loading.value = true

  try {
    await authStore.login({ username: username.value, password: password.value })
    router.push('/dashboard')
  } catch (err: any) {
    // ✅ Correction ici :
    if (err.response && err.response.data) {
      // Le backend renvoie une chaîne simple (ex: "Le mot de passe ne peut pas être vide")
      error.value = typeof err.response.data === 'string'
        ? err.response.data
        : err.response.data.message || 'Erreur lors de la connexion'
    } else {
      error.value = 'Impossible de contacter le serveur'
    }
  } finally {
    loading.value = false
  }
}
</script>

