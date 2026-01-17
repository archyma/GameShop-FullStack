import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { useAuthStore } from './stores/authStore'

import App from './App.vue'
import router from './router'
import apiClient from './services/api'

const app = createApp(App)
const pinia = createPinia();

app.use(pinia);

const authStore = useAuthStore();
if(authStore.token){
    apiClient.defaults.headers.common['Authorization'] = `Bearer ${authStore.token}`;
}

app.use(router)
app.mount('#app')
