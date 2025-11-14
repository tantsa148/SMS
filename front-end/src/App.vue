<template>
  <div id="app">
    <!-- Sidebar affichée seulement si on n'est pas sur login -->
    <Sidebar v-if="!isLoginPage && !insertnumber" class="sidebar-fixed" />

    <!-- Contenu principal -->
    <div v-if="!isLoginPage && !insertnumber" class="main-content">
      <!-- Navbar ajoutée ici -->
      <NavBar />

      <!-- Contenu des routes -->
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
import NavBar from './components/HeaderNavbar.vue' // <-- importer ton NavBar

const route = useRoute()

// Détecte si on est sur la page login
const isLoginPage = computed(() => route.path === '/login')
const insertnumber = computed(() => route.path === '/insertnumero')
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
  margin-left: 150px; /* pour la sidebar */
  padding-top: 60px;  /* espace pour le header fixé */
  padding: 20px;
  flex: 1;
  width: calc(100% - 250px);
  min-height: 100vh;
}

</style>
