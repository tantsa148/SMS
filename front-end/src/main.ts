// src/main.ts
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'

import 'bootstrap/dist/css/bootstrap.min.css'
// Bootstrap JS (nécessaire pour modals, dropdown, tooltip)
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import './assets/css/global.css' // <-- CSS global avec Google Fonts
import '@fortawesome/fontawesome-free/css/all.css'

const app = createApp(App)

app.use(router)
app.use(createPinia()) // <-- obligatoire !
app.mount('#app')
