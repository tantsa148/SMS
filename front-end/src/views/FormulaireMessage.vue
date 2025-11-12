<template>
  <div class="form-container">
    <h2>Envoyer un message</h2>
    <form @submit.prevent="handleSubmit">
      <!-- Sélecteur de plateforme -->
      <div class="form-group">
        <label for="platform">Plateforme</label>
        <select id="platform" v-model="platform" required>
          <option value="">Sélectionnez une option</option>
          <option value="whatsapp">WhatsApp</option>
          <option value="sms">SMS</option>
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
      :class="['preview', messageStatus === 'success' ? 'success' : 'error']"
    >
      <h3>Données soumises :</h3>
      <p><strong>Plateforme :</strong> {{ platform }}</p>
      <p><strong>Numéro complet :</strong> {{ fullNumber }}</p>
      <p><strong>Message :</strong> {{ message }}</p>
      <p><strong>Status :</strong> {{ messageStatus }}</p>
      <p v-if="errorMessage"><strong>Erreur :</strong> {{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import countries from '../assets/countries.json'
import { sendMessage } from '../services/smsService'
import '../assets/css/message-form.css'

const platform = ref('')
const selectedCountryCode = ref('')
const number = ref('')
const message = ref('')
const submitted = ref(false)

const messageStatus = ref('')   // status success / failed
const errorMessage = ref('')    // message d'erreur Twilio

const fullNumber = computed(() => {
  if (!selectedCountryCode.value || !number.value) return ''
  const cleanNumber = number.value.replace(/\D/g, '')
  return selectedCountryCode.value + cleanNumber
})

const handleSubmit = async () => {
  if (!platform.value) {
    alert('Veuillez choisir une plateforme.')
    return
  }

  submitted.value = true

  const payload = {
    expediteur: '+14155238886',
    destinataire: fullNumber.value,
    messageTexte: {
      contenu: message.value
    }
  }

  try {
    const response = await sendMessage(payload, platform.value)

    // Stocker le status et l’erreur Twilio
    messageStatus.value = response.status || 'unknown'
    errorMessage.value = response.errorMessage || response.message || ''

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
  }
}
</script>
