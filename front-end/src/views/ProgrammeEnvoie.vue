<template>
  <div class="form-container">
    <h2>Envoyer un message</h2>
    <form @submit.prevent="handleSubmit">
      <!-- Sélecteur de plateforme -->
      <div class="form-group">
        <label for="platform">Plateforme</label>
        <select id="platform" v-model="platform" required>
          <option value="">Sélectionnez une option</option>
          <option value="sms">SMS</option>
          <option value="whatsapp">WhatsApp</option>
        </select>
      </div>

      <!-- Sélecteur pays + numéro -->
      <div class="form-group">
        <label for="numero">Numéro</label>
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

      <!-- Sélecteur de message -->
      <div class="form-group">
        <label for="message">Message</label>
        <select id="message" v-model="selectedMessageId" required>
          <option value=""> Sélectionnez un message </option>
          <option
            v-for="m in messages"
            :key="m.id"
            :value="m.id"
          >
            {{ m.texte }}
          </option>
        </select>
      </div>

      <button type="submit">Envoyer</button>
    </form>

    <!-- Prévisualisation -->
    <div
      v-if="submitted"
      :class="['preview', messageStatus === 'success' ? 'success' : 'error']"
    >
      <h3>Données soumises :</h3>
      <p><strong>Plateforme :</strong> {{ platform }}</p>
      <p><strong>Numéro complet :</strong> {{ fullNumber }}</p>
      <p><strong>Message :</strong> {{ selectedMessageTexte }}</p>
      <p><strong>Status :</strong> {{ messageStatus }}</p>
      <p v-if="errorMessage"><strong>Erreur :</strong> {{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import countries from '../assets/countries.json'
import { smsService } from '../services/smsServiceDB'
import { messageService } from '../services/messageService'
import '../assets/css/message-form.css'

const platform = ref('sms')
const selectedCountryCode = ref('')
const number = ref('')
const selectedMessageId = ref<number | null>(null)
const messages = ref<any[]>([])
const submitted = ref(false)

const messageStatus = ref('')   // status success / failed
const errorMessage = ref('')    // message d'erreur Twilio

const fullNumber = computed(() => {
  if (!selectedCountryCode.value || !number.value) return ''
  const cleanNumber = number.value.replace(/\D/g, '')
  return selectedCountryCode.value + cleanNumber
})

const selectedMessageTexte = computed(() => {
  const msg = messages.value.find(m => m.id === selectedMessageId.value)
  return msg ? msg.texte : ''
})

onMounted(async () => {
  try {
    messages.value = await messageService.getAll()
  } catch (error) {
    console.error('Erreur lors du chargement des messages :', error)
    alert('Impossible de charger la liste des messages.')
  }
})

const handleSubmit = async () => {
  if (!selectedMessageId.value) {
    alert('Veuillez sélectionner un message.')
    return
  }

  const payload = {
    messageId: selectedMessageId.value,
    destinataire: fullNumber.value,
    expediteur: '+12297570694'
  }

  try {
    const response = await smsService.sendMessage(payload, platform.value)

    // Stocker le status et l’erreur Twilio
    messageStatus.value = response.status || 'unknown'
    errorMessage.value = response.errorMessage || response.message || ''

    submitted.value = true
    console.log('Réponse Twilio :', response)
  } catch (error: any) {
    console.error('Erreur envoi :', error)

    messageStatus.value = 'failed'

    // Récupérer l’erreur Twilio depuis le JSON du backend
    if (error.response && error.response.data) {
      errorMessage.value =
        error.response.data.errorMessage || error.response.data.message || 'Erreur inconnue'
    } else {
      errorMessage.value = error.message || 'Erreur inconnue'
    }

    submitted.value = true
  }
}
</script>
