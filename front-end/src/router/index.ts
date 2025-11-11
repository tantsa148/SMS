import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'  // <--- type-only import
import DashboardView from '../views/DashboardView.vue'

import LoginView from '../views/LoginView.vue'

const routes: Array<RouteRecordRaw> = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: LoginView },
  {path:'/dashboard',component:DashboardView},
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
