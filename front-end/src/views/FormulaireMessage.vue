<template>
  <div class="form-container">
    <h2>Envoyer un message</h2>
    <form @submit.prevent="handleSubmit">
      <!-- Sélecteur plateforme -->
      <div class="form-group">
        <label for="platform">Plateforme</label>
        <select id="platform" v-model="platform" disabled>
          <!-- Désactivé car l'API ne l'utilise plus -->
          <option value="sms">SMS</option>
        </select>
      </div>

      <!-- Sélecteur numéro expéditeur -->
      <div class="form-group">
        <label for="expediteur">Numéro expéditeur</label>
        <select id="expediteur" v-model="selectedExpediteurId" required>
          <option value="">Sélectionnez un numéro</option>
          <option
            v-for="n in numerosAssignes"
            :key="n.idNumero"
            :value="n.idNumero"
          >
            {{ n.valeurNumero }}
          </option>
        </select>
      </div>

      <!-- Sélecteur pays + numéro destinataire -->
      <div class="form-group">
        <label for="numero">Numéro destinataire</label>
        <div class="row">
          <div class="col">
            <select v-model="selectedCountryCode" required>
              <option value="">Pays</option>
              <option v-for="c in countries" :key="c.code" :value="c.code">
                {{ c.name }} ({{ c.code }})
              </option>
            </select>
          </div>
          <div class="col">
            <input
              id="numero"
              type="text"
              v-model="number"
              placeholder="Numéro local"
              required
            />
          </div>
        </div>
      </div>

      <!-- Zone de message -->
      <div class="form-group">
        <label for="message">Message</label>
        <textarea
          id="message"
          v-model="message"
          rows="5"
          placeholder="Écrivez votre message ici..."
          required
        ></textarea>
      </div>

      <button type="submit">Envoyer</button>
    </form>

    <!-- Prévisualisation -->
      <div
        v-if="submitted"
        :class="['preview', messageStatus !== 'failed' ? 'success' : 'error']"
      >

      <h3>Données soumises :</h3>
      <p><strong>Numéro expéditeur :</strong> {{ selectedExpediteur?.valeurNumero || selectedExpediteurId }}</p>
      <p><strong>Numéro destinataire :</strong> {{ fullNumber }}</p>
      <p><strong>Message :</strong> {{ message }}</p>
      <p><strong>Status :</strong> {{ messageStatus }}</p>
      <p v-if="errorMessage"><strong>Erreur :</strong> {{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import countries from '../assets/countries.json'
import { sendMessage } from '../services/smsService'
import type { NumeroAssigne } from '../services/numeroAssignService'
import { getNumerosAssignes } from '../services/numeroAssignService'
import '../assets/css/message-form.css'

// Plateforme (désactivée, API SMS uniquement)
const platform = ref('sms')

// Sélecteur expéditeur (idNumero)
const selectedExpediteurId = ref<number | null>(null)
const numerosAssignes = ref<NumeroAssigne[]>([])

// Pays + numéro destinataire
const selectedCountryCode = ref('')
const number = ref('')

// Message
const message = ref('')

// Prévisualisation
const submitted = ref(false)
const messageStatus = ref('')
const errorMessage = ref('')

// Numéro complet destinataire
const fullNumber = computed(() => {
  if (!selectedCountryCode.value || !number.value) return ''
  const cleanNumber = number.value.replace(/\D/g, '')
  return selectedCountryCode.value + cleanNumber
})

// Numéro expéditeur sélectionné (objet complet)
const selectedExpediteur = computed(() =>
  numerosAssignes.value.find(n => n.idNumero === selectedExpediteurId.value)
)

// Charger les numéros assignés
onMounted(async () => {
  try {
    numerosAssignes.value = await getNumerosAssignes()
    selectedExpediteurId.value = numerosAssignes.value[0]?.idNumero ?? null
  } catch (e) {
    console.error('Erreur chargement numéros assignés :', e)
  }
})

// Soumission
const handleSubmit = async () => {
  if (!selectedExpediteurId.value) {
    alert('Veuillez choisir un numéro expéditeur.')
    return
  }

  submitted.value = true

  const payload = {
    destinataire: fullNumber.value,
    messageTexte: { contenu: message.value },
    idNumero: selectedExpediteurId.value
  }

  try {
    const response = await sendMessage(payload)

    messageStatus.value = response.status
    errorMessage.value = response.errorMessage || ''

    console.log('Réponse API :', response)
  } catch (error: any) {
    messageStatus.value = 'failed'
    errorMessage.value = error.message || 'Erreur inconnue'
  }
}
</script>
