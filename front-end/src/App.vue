<template>
  <div id="app">
    <!-- Le router affichera ici la vue correspondante à l'URL -->
    <router-view />
  </div>
</template>

<script lang="ts" setup>
// Pas besoin d'importer router ici si tu l'as déjà monté dans main.ts
</script>

<style>
/* Styles globaux */
body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
}
#app {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
