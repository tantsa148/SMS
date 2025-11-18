<template>
  <Teleport to="body">
    <div v-if="show" class="modal-backdrop" @click.self="$emit('close')">
      <div class="modal-content success-modal" @click.stop>
        <div class="success-icon">Success</div>
        <h3>Numéro ajouté avec succès !</h3>

        <p class="highlight">{{ numero }}</p>


        <button @click="$emit('close')" class="btn-close">OK</button>
      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
defineProps<{
  show: boolean
  numero: string
  username?: string
}>()

defineEmits<{
  close: []
}>()
</script>