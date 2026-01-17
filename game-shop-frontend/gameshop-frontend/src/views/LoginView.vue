<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';

const username = ref('');
const password = ref('');
const errorMessage = ref('');
const router = useRouter();
const authStore = useAuthStore();

const handleLogin = async () => {
  errorMessage.value = '';
  try {
    await authStore.login(username.value, password.value);

    if(authStore.isAdmin){
      router.push('/admin/games');
    } else{
      router.push('/');
    }
  } catch (error) {
    console.error(error)
    errorMessage.value = 'Неверный логин или пароль.';
  }
};
</script>

<template>
  <div class="auth-wrapper">
    <div class="auth-container">
      <h2>Вход в аккаунт</h2>
      
      <form @submit.prevent="handleLogin" class="auth-form">
        <div class="form-group">
          <label for="username">Логин:</label>
          <input type="text" id="username" v-model="username" required placeholder="Введите логин">
        </div>
        <div class="form-group">
          <label for="password">Пароль:</label>
          <input type="password" id="password" v-model="password" required placeholder="Введите пароль">
        </div>

        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>

        <button type="submit" class="auth-button">Войти</button>
        
        <p class="auth-link">
          Нет аккаунта? <RouterLink to="/register">Создай его!</RouterLink>
        </p>
      </form>
    </div>
  </div>
</template>

<style scoped>
.auth-wrapper {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  min-height: 100vh;
  padding: 1rem;
  box-sizing: border-box;
}

.auth-container {
  width: 100%;
  max-width: 400px;
  margin-top: 5rem;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.5);
  background-color: var(--color-surface);
}

h2 {
  text-align: center;
  margin-bottom: 1.5rem;
}

.auth-form { 
  display: flex; 
  flex-direction: column; 
  gap: 1.5rem; 
}

.form-group { 
  display: flex; 
  flex-direction: column; 
}

.form-group label { 
  margin-bottom: 0.5rem; 
  font-weight: 500; 
}

.form-group input {
  width: 100%;
  padding: 0.75rem; 
  border: 1px solid var(--color-border); 
  border-radius: 4px; 
  background-color: var(--color-background);
  color: var(--color-text);
  box-sizing: border-box; 
  font-size: 16px;
}

.auth-button {
  padding: 0.8rem;
  background-color: var(--color-primary); 
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1.1rem;
  margin-top: 0.5rem;
  font-weight: 600;
  transition: background-color 0.2s;
}

.auth-button:hover { 
  background-color: var(--color-primary-hover); 
}

.error-message { 
  color: var(--color-danger); 
  text-align: center; 
  font-weight: bold; 
}

.auth-link { 
  text-align: center; 
  margin-top: 1rem;
}

.auth-link a { 
  font-weight: 600; 
  color: var(--color-primary);
  text-decoration: none;
}

@media (max-width: 600px) {
  .auth-wrapper {
    padding: 1rem;
  }

  .auth-container {
    margin-top: 0;
    padding: 1.5rem;
    box-shadow: none;
    border: 1px solid var(--color-border);
  }

  .auth-button {
    padding: 1rem; 
  }
}
</style>