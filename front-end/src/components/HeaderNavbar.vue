<template>
  <nav class="navbar-header">
    <div class="container d-flex align-items-center justify-content-between">
      <!-- Barre de recherche avec bouton à côté et encadrée -->
      <form class="search-form d-flex align-items-center" @submit.prevent="onSearch">
        <input
          type="text"
          v-model="query"
          class="form-control search-input"
          placeholder="Search ..."
        />
        <button class="btn btn-outline-secondary search-btn" type="submit">
          <i class="fa fa-search"></i>
        </button>
      </form>

      <!-- Notification à droite -->
      <div class="notif-container ms-3">
        <button class="btn btn-outline-secondary btn-sm notif-btn">
          <i class="fa fa-bell"></i>
          <span class="badge">4</span>
        </button>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref } from 'vue'

const query = ref('')

function onSearch() {
  console.log('Recherche:', query.value)
  query.value = ''
}
</script>

<style scoped>
.navbar-header {
  position: fixed;
  top: 0;
  left: 250px;
  width: calc(100% - 250px);
  height: 60px;
  background-color: #fff;
  border-bottom: 1px solid #e5e7eb;
  z-index: 900;
  display: flex;
  align-items: center;
  padding: 0 1rem;
}

.search-form {
  display: flex;
  align-items: center;
  border: 1px solid #ced4da;
  border-radius: 4px;
  overflow: hidden;
}

.search-input {
  border: none;
  outline: none;
  padding: 0.375rem 0.75rem;
  width: 200px;
}

.search-btn {
  border-left: 1px solid #ced4da;
  border-radius: 0;
  padding: 0.375rem 0.75rem;
}

.notif-container {
  display: flex;
  align-items: center;
}

.notif-btn {
  position: relative;
  font-size: 0.9rem;
  padding: 0.25rem 0.5rem;
}

.badge {
  position: absolute;
  top: -5px;
  right: -5px;
  background: #ef4444;
  color: white;
  font-size: 0.6rem;
  padding: 0.1rem 0.25rem;
  border-radius: 999px;
}
</style>
