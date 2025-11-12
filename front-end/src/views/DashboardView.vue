<template>
  <div class="page-container">
    <!-- Sidebar fixe à gauche -->

    <!-- Contenu principal défilant -->
    <div class="main-content">
      <h1>👋 Bonjour !</h1>
      <p>Bienvenue sur ta première page Vue.js 🎉</p>

      <!-- Exemple de contenu long pour tester le scroll -->
      <div v-for="i in 50" :key="i" style="margin-top: 20px;">
        Ligne {{ i }}
      </div>
    </div>
  </div>
</template>


