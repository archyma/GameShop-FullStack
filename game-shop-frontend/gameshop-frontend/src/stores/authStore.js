import { defineStore } from 'pinia';
import apiClient from '@/services/api';
import { jwtDecode } from 'jwt-decode';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || null,
    user: JSON.parse(localStorage.getItem('user') || 'null'),
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    isAdmin: (state) => state.user && state.user.roles && state.user.roles.includes('ROLE_ADMIN'),
    username: (state) => state.user ? state.user.sub : null,
  },

  actions: {
    async login(username, password) {
      const response = await apiClient.post('/auth/login', { username, password });
      const token = response.data;

      if(!token || typeof token !== 'string'){
        throw new Error('Некорректный ответ от сервера при входе.')
      }
      
      this.setToken(token);
    },

    setToken(token) {
      this.token = token;
      localStorage.setItem('token', token);
      
      const decoded = jwtDecode(token);
      this.user = {
        sub: decoded.sub,
        roles: decoded.roles,
      };
      localStorage.setItem('user', JSON.stringify(this.user));

      apiClient.defaults.headers.common['Authorization'] = `Bearer ${token}`;

      console.log('Токен:', this.token);
      console.log('Декодированный пользователь:', this.user);
      console.log('Роли пользователя:', this.user?.roles);
    },

    logout() {
      this.token = null;
      this.user = null;
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      delete apiClient.defaults.headers.common['Authorization'];
      
      import('@/router').then(routerModule => {
        routerModule.default.push('/login');
      });
    },
    
    initialize() {
      if (this.token) {
        apiClient.defaults.headers.common['Authorization'] = `Bearer ${this.token}`;
      }
    }
  }
});