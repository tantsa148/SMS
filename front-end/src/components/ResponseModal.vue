<template>
  <div v-if="visible" class="modal-overlay" @click="$emit('close')">
    <div class="modal-content" @click.stop>
      <div class="modal-body">
        <div class="response-details">
          <div class="detail-item">
            <strong>Statut :</strong> 
            <span :class="messageStatus !== 'failed' ? 'status-success' : 'status-error'">
              {{ messageStatus !== 'failed' ? 'Succès' : 'Échec' }}
            </span>
          </div>
          <div v-if="messageStatus !== 'failed'" class="detail-item">
            <strong>ID Message :</strong> {{ responseData?.sid || 'Non disponible' }}
          </div>
          <div v-if="errorMessage" class="detail-item error-message">
            <strong>Erreur :</strong> {{ errorMessage }}
          </div>
          <div class="detail-item">
            <strong>Destinataire :</strong> {{ destinataire }}
          </div>
          <div class="detail-item">
            <strong>Date d'envoi :</strong> {{ new Date().toLocaleString() }}
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn-cancel" @click="$emit('close')">Fermer</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  visible: boolean
  messageStatus: string
  errorMessage: string
  destinataire: string
  responseData?: any
}>()

defineEmits<{
  close: []
}>()
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-body {
  padding: 1.5rem;
}

.modal-footer {
  padding: 1rem 1.5rem;
  border-top: 1px solid #e5e5e5;
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}

.response-details {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.status-success {
  color: #28a745;
  font-weight: bold;
}

.status-error {
  color: #dc3545;
  font-weight: bold;
}

.error-message {
  color: #dc3545;
}

.btn-cancel {
  padding: 0.5rem 1rem;
  border: 1px solid #6c757d;
  background-color: white;
  color: #6c757d;
  border-radius: 4px;
  cursor: pointer;
}

.btn-cancel:hover {
  background-color: #f8f9fa;
}
</style>