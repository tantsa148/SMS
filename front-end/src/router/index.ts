import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'  // <--- type-only import

import LoginView from '../views/LoginView.vue'

const routes: Array<RouteRecordRaw> = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: LoginView },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
