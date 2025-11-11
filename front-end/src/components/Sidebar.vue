<template>
  <div class="sidebar-custom">
    <!-- En-tête -->
    <div class="d-flex justify-center items-center mb-4" style="height: 80px;">
      <h1>ERP Next</h1>
    </div>

    <!-- Section Statistique -->
    <ul class="nav flex-column mb-4">
      <li class="nav-item">
        <RouterLink
          to="/dashboard"
          class="nav-link"
          :class="{ active: route.path === '/dashboard' }"
        >
          <i class="fas fa-home"></i>
          Dash
        </RouterLink>
      </li>
    </ul>


    <!-- Bouton Déconnexion -->
    <ul class="nav flex-column mt-auto">
      <li class="nav-item">
        <button class="btn btn-danger w-100" @click="handleLogout">
          Déconnexion
        </button>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { useRouter, useRoute, RouterLink } from 'vue-router'
import { defineProps } from 'vue'
import '../assets/sidebar.css'


const props = defineProps<{
  usersCount?: number
  customerCount?: number
}>()

const router = useRouter()
const route = useRoute()

const usersCount = props.usersCount ?? 0
const customerCount = props.customerCount ?? 0

const handleLogout = () => {
  localStorage.removeItem('jwtToken')
  router.push('/')
}
</script>
