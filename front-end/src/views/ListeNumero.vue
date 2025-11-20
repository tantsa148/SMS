<template>
  <div class="container mt-5">
    <div v-if="loading" class="text-center">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Chargement...</span>
      </div>
      <p class="mt-2 text-muted">Chargement de vos numéros...</p>
    </div>

    <div v-else class="card shadow-sm">
      <div class="card-header bg-light">
        <div class="d-flex justify-content-between align-items-center">
          <h5 class="card-title mb-0 fw-bold text-dark">Mes Numéros</h5>
          <button 
            class="btn btn-primary btn-sm"
            @click="ajouterNumero"
          >
            Ajouter
          </button>
        </div>
      </div>
      <div class="card-body">
        <div v-if="tableData.length === 0" class="text-center py-5">
          <div class="text-muted mb-3">📱</div>
          <p class="text-muted mb-2">Aucun numéro n'est associé à votre compte</p>
          <button 
            class="btn btn-primary btn-sm tiny-btn"
            @click="ajouterNumero"
            >
            Ajouter
          </button>

        </div>

        <div v-else class="table-responsive">
          <table class="table table-hover">
            <thead>
              <tr>
                <th>#</th>
                <th>Numéro</th>
                <th>Propriétaire</th>
                <th>Plateformes disponibles</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(row, index) in tableData" :key="row.id">
                <td class="fw-bold text-muted">{{ index + 1 }}</td>
                <td>
                  <code class="fs-6">{{ row.valeurNumero }}</code>
                </td>
                <td>{{ row.proprietaire }}</td>
                <td>
                  {{ row.plateformes }}
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div v-if="tableData.length > 0" class="card-footer bg-light">
        <small class="text-muted">
          Total : {{ tableData.length }} numéro(s)
        </small>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import numeroOverviewService from '../services/listNumeroService'

interface Numero {
  id: number
  valeurNumero: string
  proprietaire: string
  plateformes: string[]
}

const loading = ref(true)
const tableData = ref<Numero[]>([])

const fetchData = async () => {
  try {
    tableData.value = await numeroOverviewService.getMesNumeros()
  } catch (error) {
    console.error('Erreur lors du chargement des numéros:', error)
  } finally {
    loading.value = false
  }
}

const ajouterNumero = () => {
  console.log('Ajouter un nouveau numéro')
  alert('Fonctionnalité d\'ajout à implémenter')
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.card {
  border: 1px solid #e3f2fd;
}

.table th {
  background-color: #f8f9fa;
  border-bottom: 2px solid #dee2e6;
}
.tiny-btn {
  font-size: 0.7rem;   /* taille du texte plus petite */
  padding: 0.25rem 0.5rem; /* réduit le padding du bouton */
}

</style>