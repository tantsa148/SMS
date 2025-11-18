<template>
  <div class="form-container">
    <h2>Ajout Numéro</h2>

    <form @submit.prevent="handleSubmit">
      <!-- Champ Numéro + Indicatif -->
      <div class="form-group">
        <label for="numero">Numéro de téléphone :</label>
        <div class="phone-input-wrapper">
          <div class="country-select-wrapper">
            <select v-model="selectedCountryCode" class="country-select">
              <option v-for="country in countries" :key="country.code" :value="country.code">
                {{ country.flag }} {{ country.name }} ({{ country.code }})
              </option>
            </select>
          </div>

          <input
            type="text"
            id="numero"
            v-model="numeroLocal"
            placeholder="Ex: 612345678"
            required
            class="phone-input"
          />
        </div>
      </div>

      <!-- Sélecteur plateforme -->
      <div class="form-group">
        <label for="plateforme">Plateforme :</label>
        <select v-model="selectedPlateformeId" class="platform-select" required>
          <option value="">-- Sélectionnez une plateforme --</option>
          <option v-for="p in plateformes" :key="p.id" :value="p.id">
            {{ p.nomPlateform }}
          </option>
        </select>
      </div>

      <!-- Bouton -->
      <button
        type="submit"
        :disabled="!isFormValid"
        :class="{ 'btn-disabled': !isFormValid }"
        class="btn-submit"
      >
        Envoyer
      </button>
    </form>

    <!-- MODALE DE CONFIRMATION UNIQUEMENT -->
    <ConfirmAddNumeroModal
      :show="showConfirm"
      :numero="fullNumero"
      @confirm="confirmSubmit"
      @cancel="showConfirm = false"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { createNumeroAndAssign } from '../services/numeroService'
import type { NumeroAssignResponse } from '../services/numeroService'
import ConfirmAddNumeroModal from '../components/ConfirmAddNumeroModal.vue'
import { fetchPlateformes } from '../services/plateformeService'
import type { Plateforme } from '../types/Plateforme'
import '../assets/css/numero-form.css'
import countriesData from '../assets/countries.json' with { type: 'json' }

interface Country {
  name: string
  code: string
  flag: string
}

// Variables
const countries = ref<Country[]>([])
const selectedCountryCode = ref('+33')
const numeroLocal = ref('')

const plateformes = ref<Plateforme[]>([])
const selectedPlateformeId = ref<number | ''>('')

const showConfirm = ref(false)

// Validation
const isFormValid = computed(() =>
  numeroLocal.value.trim().length > 0 && selectedPlateformeId.value !== ''
)

// Numéro complet collé
const fullNumero = computed(() => {
  const local = numeroLocal.value.trim()
  return local ? `${selectedCountryCode.value}${local}` : ''
})

onMounted(async () => {
  countries.value = countriesData as Country[]
  countries.value.sort((a, b) => a.name.localeCompare(b.name))

  try {
    plateformes.value = await fetchPlateformes()
  } catch (err) {
    console.error('Erreur chargement plateformes:', err)
  }
})

const handleSubmit = () => {
  if (!isFormValid.value) {
    alert('Veuillez remplir tous les champs')
    return
  }
  showConfirm.value = true
}

const confirmSubmit = async () => {
  showConfirm.value = false

  try {
    const dto = {
      valeurNumero: fullNumero.value,
      idPlateforme: selectedPlateformeId.value as number
    }

    const data: NumeroAssignResponse = await createNumeroAndAssign(dto)

    // Affiche les messages du backend (succès)
    if (data.messages && data.messages.length > 0) {
      alert('Succès !\n\n' + data.messages.join('\n'))
    } else {
      alert('Numéro ajouté avec succès !\n' + data.valeurNumero)
    }

    // Reset du formulaire
    numeroLocal.value = ''
    selectedPlateformeId.value = ''

  } catch (err: any) {
    // Gestion des erreurs avec messages[]
    const messages = err.response?.data?.messages
    if (Array.isArray(messages) && messages.length > 0) {
      alert('Erreur :\n\n' + messages.join('\n'))
    } else {
      alert('Erreur : ' + (err.response?.data?.message || 'Problème lors de l’ajout'))
    }
  }
}
</script>

<style scoped>
.btn-submit {
  width: 100%;
  padding: 14px;
  background-color: #0d6efd;
  color: white;
  font-weight: 600;
  font-size: 1.1rem;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
  margin-top: 1rem;
}
.btn-submit:disabled,
.btn-disabled {
  background-color: #0d6efd !important;
  opacity: 0.5;
  cursor: not-allowed;
}
.btn-submit:hover:not(:disabled) {
  background-color: #0b5ed7;
}
.platform-select {
  width: 100%;
  padding: 10px;
  border-radius: 12px;
  border: 1px solid #ccc;
  margin-top: 5px;
  font-size: 1rem;
}
</style>