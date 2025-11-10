import axios from 'axios'
import { useAuthStore } from '../store/auth'

// Création d'une instance axios personnalisée
const api = axios.create({
  baseURL: 'http://localhost:8080',
})

// Ajout du token avant chaque requête
api.interceptors.request.use((config) => {
  const auth = useAuthStore()
  const token = auth.token || localStorage.getItem('token')

  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }

  return config
})

export default api
