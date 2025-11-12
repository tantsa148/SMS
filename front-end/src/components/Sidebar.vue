<template>
  <div class="sidebar-custom">
    <!-- En-tête -->
    <div class="d-flex justify-center items-center mb-4" style="height: 80px;">
      <h1>SMS</h1>
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
          Dashboard
        </RouterLink>
      </li>
    </ul>

    <!-- Section Message -->
    <ul class="nav flex-column">
      <li class="nav-item">
        <RouterLink
          to="/message"
          class="nav-link"
          :class="{ active: route.path === '/message' }"
        >
          <i class="fas fa-envelope"></i>
        Envoyer message
        </RouterLink>
      </li>
    </ul>
    
    <!-- Section Programmation message -->
    <ul class="nav flex-column">
      <li class="nav-item">
        <RouterLink
          to="/progammeEnvoie"
          class="nav-link"
          :class="{ active: route.path === '/message' }"
        >
          <i class="fa-solid fa-paper-plane"></i>
        Programmer message
        </RouterLink>
      </li>
    </ul>
    <ul class="nav flex-column">
      <li class="nav-item">
        <RouterLink
          to="/historique"
          class="nav-link"
          :class="{ active: route.path === '/historique' }"
        >
          <i class="fas fa-envelope"></i>
        historique
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
import '../assets/css/sidebar.css'



const router = useRouter()
const route = useRoute()


const handleLogout = () => {
  localStorage.removeItem('jwtToken')
  router.push('/')
}
</script>
