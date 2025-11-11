<template>
  <div id="app">
    <!-- Sidebar affichée seulement si on n'est pas sur login -->
    <Sidebar v-if="!isLoginPage" class="sidebar-fixed" />

    <!-- Contenu principal -->
    <div v-if="!isLoginPage" class="main-content">
      <router-view />
    </div>

    <!-- Pour la page login, on affiche seulement router-view (elle gère son CSS) -->
    <router-view v-else />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import Sidebar from './components/Sidebar.vue'

const route = useRoute()

// Détecte si on est sur la page login
const isLoginPage = computed(() => route.path === '/login')
</script>

<style>

/* Sidebar fixe */
.sidebar-fixed {
  position: fixed;
  left: 0;
  top: 0;
  height: 100vh;
  width: 250px;
  z-index: 1000;
}

/* Contenu principal pour les pages normales */
.main-content {
  margin-left: 150px; /* espace pour la sidebar */
  padding: 20px;
  flex: 1;
  width: calc(100% - 250px); /* pour être sûr qu'il ne déborde pas */
  min-height: 100vh;
}
</style>
