<template>
  <div class="form-container">
    <h2>Envoyer un message</h2>
    <form @submit.prevent="showValidationModal">
      <!-- Plateforme -->
      <div class="form-group">
        <label for="platform">Plateforme</label>
        <select id="platform" v-model="platform" required>
          <option value="">Sélectionnez une option</option>
          <option value="sms">SMS</option>
          <option value="whatsapp">WhatsApp</option>
        </select>
      </div>

      <!-- Numéro expéditeur -->
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

      <!-- Numéro destinataire -->
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

      <!-- Message -->
      <div class="form-group">
        <label for="message">Message</label>
        <select id="message" v-model="selectedMessageId" required>
          <option value="">Sélectionnez un message</option>
          <option
            v-for="m in messages"
            :key="m.id"
            :value="m.id"
          >
            {{ m.texte }}
          </option>
        </select>
      </div>

      <div class="actions">
        <button type="submit">Envoyer</button>
      </div>
    </form>

    <!-- Utilisation des modals externes -->
    <ValidationModal
      :visible="showValidation"
      :platform="platform"
      :expediteur="selectedExpediteurValeur"
      :destinataire="fullNumber"
      :message="selectedMessageTexte"
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
import { smsService } from '../services/smsServiceDB'
import { messageService } from '../services/messageService'
import { getNumerosAssignes } from '../services/numeroAssignService'
import type { NumeroAssigne } from '../services/numeroAssignService'
import ValidationModal from '../components/ValidationModal.vue'
import ResponseModal from '../components/ResponseModal.vue'

// Variables du formulaire
const platform = ref('sms')
const selectedCountryCode = ref('')
const number = ref('')
const selectedMessageId = ref<number | null>(null)
const messages = ref<any[]>([])
const selectedExpediteurId = ref<number | null>(null)
const numerosAssignes = ref<NumeroAssigne[]>([])

// États pour les modals
const showValidation = ref(false)
const showResponseModal = ref(false)
const messageStatus = ref('')
const errorMessage = ref('')
const responseData = ref<any>(null)

// Computed properties
const fullNumber = computed(() => {
  if (!selectedCountryCode.value || !number.value) return ''
  const cleanNumber = number.value.replace(/\D/g, '')
  return selectedCountryCode.value + cleanNumber
})

const selectedMessageTexte = computed(() => {
  const msg = messages.value.find(m => m.id === selectedMessageId.value)
  return msg ? msg.texte : ''
})

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

  try {
    messages.value = await messageService.getAll()
  } catch (error) {
    console.error('Erreur chargement messages :', error)
  }
})

// Fonctions pour le modal de validation
const showValidationModal = () => {
  if (!selectedMessageId.value || !selectedExpediteurId.value) {
    alert('Veuillez sélectionner un message et un numéro expéditeur.')
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
  
  // Validation plus stricte pour TypeScript
  if (!selectedExpediteurId.value || !selectedMessageId.value) {
    messageStatus.value = 'failed'
    errorMessage.value = 'Numéro expéditeur ou message manquant'
    showResponseModal.value = true
    return
  }

  const payload = {
    idNumero: selectedExpediteurId.value,
    messageId: selectedMessageId.value,
    destinataire: fullNumber.value
  }

  try {
    const response = await smsService.sendMessage(payload, platform.value)
    
    messageStatus.value = response.status || 'success'
    errorMessage.value = response.errorMessage || response.message || ''
    responseData.value = response
    
    showResponseModal.value = true
    console.log('Réponse Twilio :', response)
  } catch (error: any) {
    console.error('Erreur envoi :', error)
    
    messageStatus.value = 'failed'
    
    if (error.response && error.response.data) {
      errorMessage.value =
        error.response.data.errorMessage || error.response.data.message || 'Erreur inconnue'
    } else {
      errorMessage.value = error.message || 'Erreur inconnue'
    }
    
    showResponseModal.value = true
  }
}
</script>
