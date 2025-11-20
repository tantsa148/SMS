<template>
  <div class="form-container">
    <h2>Envoyer un message</h2>
    <form @submit.prevent="showValidationModal">
      <!-- Sélecteur plateforme -->
      <div class="form-group">
        <label for="platform">Plateforme</label>
        <select id="platform" v-model="platform">
          <option value="sms">SMS</option>
          <option value="whatsapp">WhatsApp</option>
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

    <!-- Modals -->
    <ValidationModal
      :visible="showValidation"
      :platform="platform"
      :expediteur="selectedExpediteurValeur"
      :destinataire="fullNumber"
      :message="message"
      @close="closeValidationModal"
      @confirm="confirmSend"
    />

    <ResponseModal
      :visible="showResponseModal"
      :message-status="messageStatus"
      :error-message="errorMessage"
      :destinataire="fullNumber"
      :response-data="responseData"
      @close="closeResponseModal"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import countries from '../assets/countries.json'
import { sendMessage } from '../services/smsService'
import type { NumeroAssigne } from '../services/numeroAssignService'
import { getNumerosAssignes } from '../services/numeroAssignService'
import ValidationModal from '../components/ValidationModal.vue'
import ResponseModal from '../components/ResponseModal.vue'
import '../assets/css/message-form.css'

// Variables du formulaire
const platform = ref('sms')
const selectedExpediteurId = ref<number | null>(null)
const numerosAssignes = ref<NumeroAssigne[]>([])
const selectedCountryCode = ref('')
const number = ref('')
const message = ref('')
const submitted = ref(false)
const messageStatus = ref('')
const errorMessage = ref('')

// États pour les modals
const showValidation = ref(false)
const showResponseModal = ref(false)
const responseData = ref<any>(null)

// Computed properties
const fullNumber = computed(() => {
  if (!selectedCountryCode.value || !number.value) return ''
  const cleanNumber = number.value.replace(/\D/g, '')
  return selectedCountryCode.value + cleanNumber
})

const selectedExpediteur = computed(() =>
  numerosAssignes.value.find(n => n.idNumero === selectedExpediteurId.value)
)

const selectedExpediteurValeur = computed(() => {
  const expediteur = numerosAssignes.value.find(n => n.idNumero === selectedExpediteurId.value)
  return expediteur ? expediteur.valeurNumero : ''
})

// Lifecycle
onMounted(async () => {
  try {
    numerosAssignes.value = await getNumerosAssignes()
    selectedExpediteurId.value = numerosAssignes.value[0]?.idNumero ?? null
  } catch (e) {
    console.error('Erreur chargement numéros assignés :', e)
  }
})

// Fonctions pour les modals
const showValidationModal = () => {
  if (!selectedExpediteurId.value) {
    alert('Veuillez choisir un numéro expéditeur.')
    return
  }
  if (!fullNumber.value) {
    alert('Veuillez saisir un numéro destinataire valide.')
    return
  }
  if (!message.value) {
    alert('Veuillez écrire un message.')
    return
  }
  showValidation.value = true
}

const closeValidationModal = () => {
  showValidation.value = false
}

const closeResponseModal = () => {
  showResponseModal.value = false
}

const confirmSend = async () => {
  showValidation.value = false
  
  // Validation finale
  if (!selectedExpediteurId.value) {
    messageStatus.value = 'failed'
    errorMessage.value = 'Numéro expéditeur manquant'
    showResponseModal.value = true
    return
  }

  submitted.value = true

  // Payload
  const payload = {
    destinataire: fullNumber.value,
    messageTexte: { contenu: message.value },
    idNumero: selectedExpediteurId.value,
    platform: platform.value as 'sms' | 'whatsapp'
  }

  try {
    const response = await sendMessage(payload)

    messageStatus.value = response.status
    errorMessage.value = response.errorMessage || ''
    responseData.value = response

    showResponseModal.value = true
    console.log('Réponse API :', response)
  } catch (error: any) {
    messageStatus.value = 'failed'
    errorMessage.value = error.message || 'Erreur inconnue'
    showResponseModal.value = true
  }
}
</script>
