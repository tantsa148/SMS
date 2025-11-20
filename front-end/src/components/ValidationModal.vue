<template>
  <div v-if="visible" class="modal-overlay" @click="$emit('close')">
    <div class="modal-content" @click.stop>
      <div class="modal-body">
        <div class="confirmation-details">
          <h4>Veuillez vérifier les détails de votre message :</h4>
          <div class="detail-item">
            <strong>Plateforme :</strong> {{ platform.toUpperCase() }}
          </div>
          <div class="detail-item">
            <strong>Expéditeur :</strong> {{ expediteur }}
          </div>
          <div class="detail-item">
            <strong>Destinataire :</strong> {{ destinataire }}
          </div>
          <div class="detail-item">
            <strong>Message :</strong> 
            <div class="message-preview">{{ message }}</div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn-cancel" @click="$emit('close')">Annuler</button>
        <button class="btn-confirm" @click="$emit('confirm')">Confirmer l'envoi</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  visible: boolean
  platform: string
  expediteur: string
  destinataire: string
  message: string
}>()

defineEmits<{
  close: []
  confirm: []
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

.confirmation-details {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.message-preview {
  background-color: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 4px;
  padding: 0.75rem;
  margin-top: 0.25rem;
  white-space: pre-wrap;
}

.btn-cancel {
  padding: 0.5rem 1rem;
  border: 1px solid #6c757d;
  background-color: white;
  color: #6c757d;
  border-radius: 4px;
  cursor: pointer;
}

.btn-confirm {
  padding: 0.5rem 1rem;
  border: none;
  background-color: #007bff;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}

.btn-confirm:hover {
  background-color: #0056b3;
}

.btn-cancel:hover {
  background-color: #f8f9fa;
}
</style>