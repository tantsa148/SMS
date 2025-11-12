import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'  // <--- type-only import
import DashboardView from '../views/DashboardView.vue'
import FormulaireMessage from '../views/FormulaireMessage.vue'
import LoginView from '../views/LoginView.vue'
import ProgrammeEnvoie from '../views/ProgrammeEnvoie.vue'

const routes: Array<RouteRecordRaw> = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: LoginView },
  {path:'/dashboard',component:DashboardView},
  {path:'/message',component:FormulaireMessage},
  {path:'/progammeEnvoie',component:ProgrammeEnvoie}
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
