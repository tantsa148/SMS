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
              <p class="user-name">
                {{ currentUser ? currentUser.username : 'Utilisateur' }}
              </p>

            </div>
            <div class="user-menu">
              <a href="/listenumero" class="menu-item">
                <i class="fa fa-user"></i>
                Mes numero
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
<script setup lang="ts">
import { ref,computed ,onMounted  } from 'vue';
import { getCurrentUser } from '../services/userService'; // ton service
import '../assets/css/NavbarHeader.css';

const query = ref('');
const showNotifications = ref(false);
const showUserMenu = ref(false);
const currentUser = ref<{ username: string; role: string } | null>(null);

// Données de notification simulées
const notifications = ref([
  { id: 1, text: 'Nouveau message reçu', time: 'Il y a 5 min', read: false, icon: 'fa fa-envelope' },
  { id: 2, text: 'Votre message a été envoyé', time: 'Il y a 1 heure', read: false, icon: 'fa fa-check-circle' },
  { id: 3, text: 'Mise à jour du système', time: 'Il y a 2 heures', read: true, icon: 'fa fa-cog' },
  { id: 4, text: 'Nouvel utilisateur inscrit', time: 'Il y a 1 jour', read: true, icon: 'fa fa-user-plus' }
]);

// Notifications non lues
const unreadNotifications = computed(() => notifications.value.filter(n => !n.read).length);

// Fonctions
function onSearch() {
  console.log('Recherche:', query.value);
  query.value = '';
}

function toggleNotifications() {
  showNotifications.value = !showNotifications.value;
  showUserMenu.value = false;
}

function toggleUserMenu() {
  showUserMenu.value = !showUserMenu.value;
  showNotifications.value = false;
}

function markAsRead(notificationId: number) {
  const notif = notifications.value.find(n => n.id === notificationId);
  if (notif) notif.read = true;
}

function viewAllNotifications() {
  console.log('Voir toutes les notifications');
  showNotifications.value = false;
}

function logout() {
  console.log('Déconnexion');
  showUserMenu.value = false;
  localStorage.removeItem('token'); // Supprime JWT
  window.location.reload(); // Redirection simple
}

// ⚡ Récupérer l'utilisateur connecté
onMounted(async () => {
  currentUser.value = await getCurrentUser();
});

// Fermer les menus si clic à l'extérieur
document.addEventListener('click', (e) => {
  const target = e.target as HTMLElement | null;
  if (!target) return;

  if (!target.closest('.notif-container')) showNotifications.value = false;
  if (!target.closest('.user-profile')) showUserMenu.value = false;
});

</script>

