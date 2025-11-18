<template>
  <Teleport to="body">
    <div v-if="show" class="modal-backdrop" @click.self="$emit('cancel')">
      <div class="modal-content" @click.stop>
        <h3>Confirmer l'ajout</h3>
        <p>Voulez-vous vraiment enregistrer ce numéro ?</p>
        <p class="highlight">{{ numero }}</p>

        <div class="modal-actions">
          <button @click="$emit('confirm')" class="btn-confirm">Oui, ajouter</button>
          <button @click="$emit('cancel')" class="btn-cancel">Annuler</button>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
defineProps<{
  show: boolean
  numero: string
}>()

defineEmits<{
  confirm: []
  cancel: []
}>()
</script>














