<template>
  <div class="form-container">
    <h2>Formulaire Numéro</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="numero">Numéro :</label>
        <input 
          type="text" 
          id="numero" 
          v-model="numero" 
          placeholder="Entrez votre numéro"
        />
      </div>
      <button type="submit">Envoyer</button>
    </form>

    <div v-if="submitted">
      <p>Numéro soumis : {{ numero }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// état du formulaire
const numero = ref('')
const submitted = ref(false)

// fonction de soumission
const handleSubmit = () => {
  if (numero.value.trim() === '') {
    alert('Veuillez entrer un numéro !')
    return
  }

  submitted.value = true
  console.log('Numéro soumis :', numero.value)
}
import '../assets/css/login.css'

</script>

