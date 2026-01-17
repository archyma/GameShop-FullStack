import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

import HomeView from '@/views/HomeView.vue'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import CartView from '@/views/CartView.vue'
import AdminGamesView from '@/views/AdminGames.vue'
import MyOrdersView from '@/views/MyOrdersView.vue'
import AdminOrdersView from '@/views/AdminOrdersView.vue'
import AdminUsersView from '@/views/AdminUsersView.vue'
import GameDetailsView from '@/views/GameDetailsView.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: '/cart',
      name: 'cart',
      component: CartView,
      meta: {
        requiresAuth: true
      },
    },
    {
      path: '/admin/games',
      name: 'admin-games',
      component: AdminGamesView,
      meta:{
        requiresAuth: true,
        requiresAdmin: true,
      }
    },
    {
      path: '/my-orders',
      name: 'my-orders',
      component: MyOrdersView,
      meta:{
        requiresAuth: true
      }
    },
    {
      path: '/admin/orders',
      name: 'admin-orders',
      component: AdminOrdersView,
      meta:{
        requiresAuth: true,
        requiresAdmin: true
      }
    },
    {
      path: '/admin/users',
      name: 'admin-users',
      component: AdminUsersView,
      meta:{
        requiresAuth: true,
        requiresAdmin: true
      }
    },
    {
      path: '/game/:id',
      name: 'game-details',
      component: GameDetailsView,
      meta:{
        requiresAuth: true
      }
    }
  ]
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  const requiresAuth = to.meta.requiresAuth;
  const requiresAdmin = to.meta.requiresAdmin;

  if(requiresAdmin){
    if(authStore.isLoggedIn && authStore.isAdmin){
      next();
    } else if(authStore.isLoggedIn && !authStore.isAdmin){
      next('/');
    } else{
      next('/login');
    }
  } else if(requiresAuth){
    if(authStore.isLoggedIn){
      next();
    } else{
      next('/login')
    }
  } else{
    next();
  }
});

export default router