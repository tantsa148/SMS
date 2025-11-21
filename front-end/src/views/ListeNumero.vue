<template>
  <div class="container mt-5">
    <div v-if="loading" class="text-center">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Chargement...</span>
      </div>
      <p class="mt-2 text-muted">Chargement de vos numéros...</p>
    </div>

    <div v-else class="card">
      <div class="card-header d-flex justify-content-between align-items-center">
        <div class="card-title mb-0">Mes Numéros</div>
        <button 
          class="btn btn-primary btn-sm"
          style="width: 100px"
          @click="showAddModal = true"
        >
          Ajouter
        </button>
      </div>
      
      <div class="card-body">
        <!-- Aucun numéro -->
        <div v-if="tableData.length === 0" class="text-center py-4">
          <div class="text-muted mb-3">📱</div>
          <p class="text-muted mb-2">Aucun numéro n'est associé à votre compte</p>

          <button 
            class="btn btn-primary btn-sm tiny-btn"
            @click="showAddModal = true"
          >
            Ajouter
          </button>
        </div>

        <!-- TABLEAU -->
        <div v-else>
          <table class="table table-hover">
            <thead>
              <tr>
                <th scope="col">#</th>
                <th scope="col">Numéro</th>
                <th scope="col">Propriétaire</th>
                <th scope="col">Plateformes</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(row, index) in tableData" :key="row.id">
                <td>{{ index + 1 }}</td>
                <td>{{ row.valeurNumero }}</td>
                <td>{{ row.proprietaire }}</td>
                <td>{{ row.plateformes.join(', ') }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- FOOTER -->
      <div v-if="tableData.length > 0" class="card-footer">
        <small class="text-muted">Total : {{ tableData.length }} numéro(s)</small>
      </div>
    </div>

    <!-- MODAL D'AJOUT -->
    <AddNumeroModal
      :show="showAddModal"
      @update:show="showAddModal = $event"
      @numero-added="handleNumeroAdded"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import numeroOverviewService from '../services/listNumeroService'
import AddNumeroModal from '../components/Numero.vue' // Chemin à ajuster
import '../assets/css/numero-table.css'


interface Numero {
  id: number
  valeurNumero: string
  proprietaire: string
  plateformes: string[]
}

const loading = ref(true)
const tableData = ref<Numero[]>([])
const showAddModal = ref(false)

const fetchData = async () => {
  try {
    tableData.value = await numeroOverviewService.getMesNumeros()
  } catch (error) {
    console.error('Erreur lors du chargement des numéros:', error)
  } finally {
    loading.value = false
  }
}

const handleNumeroAdded = () => {
  // Rafraîchir la liste après l'ajout
  fetchData()
}

onMounted(() => {
  fetchData()
})
</script>
