<template>
  <nav class="navbar-header">
    <div class="container d-flex align-items-center justify-content-between">
      <!-- Barre de recherche avec bouton à côté et encadrée -->
      <form class="search-form d-flex align-items-center" @submit.prevent="onSearch">
        <input
          type="text"
          v-model="query"
          class="form-control search-input"
          placeholder="Search ..."
        />
        <button class="btn btn-outline-secondary search-btn" type="submit">
          <i class="fa fa-search"></i>
        </button>
      </form>

      <!-- Notifications et profil à droite -->
      <div class="user-actions d-flex align-items-center gap-3">
        <!-- Notification -->
        <div class="notif-container position-relative">
          <button 
            class="btn btn-outline-secondary btn-sm notif-btn" 
            @click="toggleNotifications"
          >
            <i class="fa fa-bell"></i>
            <span class="badge">{{ unreadNotifications }}</span>
          </button>

          <!-- Fenêtre des notifications -->
          <div v-if="showNotifications" class="notifications-dropdown">
            <div class="notifications-header">
              <h6>Notifications</h6>
              <span class="notifications-count">{{ notifications.length }} non lues</span>
            </div>
            <div class="notifications-list">
              <div 
                v-for="notification in notifications" 
                :key="notification.id"
                class="notification-item"
                :class="{ unread: !notification.read }"
                @click="markAsRead(notification.id)"
              >
                <div class="notification-icon">
                  <i :class="notification.icon"></i>
                </div>
                <div class="notification-content">
                  <p class="notification-text">{{ notification.text }}</p>
                  <span class="notification-time">{{ notification.time }}</span>
                </div>
              </div>
            </div>
            <div class="notifications-footer">
              <button class="btn-view-all" @click="viewAllNotifications">
                Voir toutes les notifications
              </button>
            </div>
          </div>
        </div>

        <!-- Profil utilisateur -->
        <div class="user-profile position-relative">
          <button 
            class="btn btn-outline-secondary btn-sm user-btn"
            @click="toggleUserMenu"
          >
            <i class="fa fa-user"></i>
          </button>

          <!-- Menu utilisateur -->
          <div v-if="showUserMenu" class="user-dropdown">
            <div class="user-info">
              <div class="user-avatar">
                <i class="fa fa-user-circle"></i>
              </div>
              <div class="user-details">
                <p class="user-name">John Doe</p>
                <span class="user-email">john.doe@example.com</span>
              </div>
            </div>
            <div class="user-menu">
              <a href="#" class="menu-item">
                <i class="fa fa-user"></i>
                Mon profil
              </a>
              <a href="#" class="menu-item">
                <i class="fa fa-cog"></i>
                Paramètres
              </a>
              <a href="#" class="menu-item">
                <i class="fa fa-question-circle"></i>
                Aide
              </a>
              <div class="menu-divider"></div>
              <a href="#" class="menu-item logout" @click="logout">
                <i class="fa fa-sign-out"></i>
                Déconnexion
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, computed } from 'vue'

const query = ref('')
const showNotifications = ref(false)
const showUserMenu = ref(false)

// Données de notification simulées
const notifications = ref([
  {
    id: 1,
    text: 'Nouveau message reçu',
    time: 'Il y a 5 min',
    read: false,
    icon: 'fa fa-envelope'
  },
  {
    id: 2,
    text: 'Votre message a été envoyé',
    time: 'Il y a 1 heure',
    read: false,
    icon: 'fa fa-check-circle'
  },
  {
    id: 3,
    text: 'Mise à jour du système',
    time: 'Il y a 2 heures',
    read: true,
    icon: 'fa fa-cog'
  },
  {
    id: 4,
    text: 'Nouvel utilisateur inscrit',
    time: 'Il y a 1 jour',
    read: true,
    icon: 'fa fa-user-plus'
  }
])

// Computed property pour les notifications non lues
const unreadNotifications = computed(() => {
  return notifications.value.filter(notif => !notif.read).length
})

function onSearch() {
  console.log('Recherche:', query.value)
  query.value = ''
}

function toggleNotifications() {
  showNotifications.value = !showNotifications.value
  showUserMenu.value = false // Fermer le menu utilisateur si ouvert
}

function toggleUserMenu() {
  showUserMenu.value = !showUserMenu.value
  showNotifications.value = false // Fermer les notifications si ouvertes
}

function markAsRead(notificationId) {
  const notification = notifications.value.find(n => n.id === notificationId)
  if (notification) {
    notification.read = true
  }
}

function viewAllNotifications() {
  console.log('Voir toutes les notifications')
  showNotifications.value = false
}

function logout() {
  console.log('Déconnexion')
  showUserMenu.value = false
  // Logique de déconnexion ici
}

// Fermer les menus en cliquant à l'extérieur
document.addEventListener('click', (e) => {
  if (!e.target.closest('.notif-container')) {
    showNotifications.value = false
  }
  if (!e.target.closest('.user-profile')) {
    showUserMenu.value = false
  }
})
</script>

<style scoped>
.navbar-header {
  position: fixed;
  top: 0;
  left: 250px;
  width: calc(100% - 250px);
  height: 60px;
  background-color: #fff;
  border-bottom: 1px solid #e5e7eb;
  z-index: 900;
  display: flex;
  align-items: center;
  padding: 0 1rem;
}

.search-form {
  display: flex;
  align-items: center;
  border: 1px solid #ced4da;
  border-radius: 4px;
  overflow: hidden;
}

.search-input {
  border: none;
  outline: none;
  padding: 0.375rem 0.75rem;
  width: 200px;
}

.search-btn {
  border-left: 1px solid #ced4da;
  border-radius: 0;
  padding: 0.375rem 0.75rem;
}

.user-actions {
  margin-left: auto;
}

.notif-container, .user-profile {
  position: relative;
}

.notif-btn, .user-btn {
  position: relative;
  font-size: 0.9rem;
  padding: 0.4rem 0.6rem;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.notif-btn:hover, .user-btn:hover {
  background-color: #f8f9fa;
  border-color: #6c757d;
}

.badge {
  position: absolute;
  top: -5px;
  right: -5px;
  background: #ef4444;
  color: white;
  font-size: 0.6rem;
  padding: 0.1rem 0.25rem;
  border-radius: 999px;
  min-width: 16px;
  text-align: center;
}

/* Styles pour le dropdown des notifications */
.notifications-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  width: 320px;
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  margin-top: 8px;
}

.notifications-header {
  padding: 1rem;
  border-bottom: 1px solid #f1f5f9;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.notifications-header h6 {
  margin: 0;
  font-weight: 600;
  color: #1f2937;
}

.notifications-count {
  font-size: 0.75rem;
  color: #6b7280;
}

.notifications-list {
  max-height: 300px;
  overflow-y: auto;
}

.notification-item {
  display: flex;
  align-items: flex-start;
  padding: 0.75rem 1rem;
  border-bottom: 1px solid #f1f5f9;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.notification-item:hover {
  background-color: #f8fafc;
}

.notification-item.unread {
  background-color: #f0f9ff;
}

.notification-item.unread:hover {
  background-color: #e0f2fe;
}

.notification-icon {
  margin-right: 0.75rem;
  color: #6b7280;
  font-size: 1rem;
}

.notification-content {
  flex: 1;
}

.notification-text {
  margin: 0 0 0.25rem 0;
  font-size: 0.875rem;
  color: #374151;
  line-height: 1.4;
}

.notification-time {
  font-size: 0.75rem;
  color: #9ca3af;
}

.notifications-footer {
  padding: 0.75rem 1rem;
  border-top: 1px solid #f1f5f9;
  text-align: center;
}

.btn-view-all {
  background: none;
  border: none;
  color: #3b82f6;
  font-size: 0.875rem;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 4px;
  transition: background-color 0.2s ease;
}

.btn-view-all:hover {
  background-color: #f1f5f9;
}

/* Styles pour le menu utilisateur */
.user-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  width: 250px;
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  margin-top: 8px;
}

.user-info {
  padding: 1rem;
  border-bottom: 1px solid #f1f5f9;
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.user-avatar {
  font-size: 2rem;
  color: #6b7280;
}

.user-details {
  flex: 1;
}

.user-name {
  margin: 0;
  font-weight: 600;
  color: #1f2937;
  font-size: 0.9rem;
}

.user-email {
  font-size: 0.75rem;
  color: #6b7280;
}

.user-menu {
  padding: 0.5rem 0;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 0.75rem 1rem;
  color: #374151;
  text-decoration: none;
  font-size: 0.875rem;
  transition: background-color 0.2s ease;
  gap: 0.75rem;
}

.menu-item:hover {
  background-color: #f8fafc;
  color: #1f2937;
}

.menu-item i {
  width: 16px;
  text-align: center;
  color: #6b7280;
}

.menu-item.logout {
  color: #ef4444;
}

.menu-item.logout:hover {
  background-color: #fef2f2;
  color: #dc2626;
}

.menu-divider {
  height: 1px;
  background-color: #f1f5f9;
  margin: 0.5rem 0;
}
</style>