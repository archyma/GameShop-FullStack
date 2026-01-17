<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import apiClient from '@/services/api';
import { useAuthStore } from '@/stores/authStore';

const username = ref('');
const password = ref('');
const errorMessage = ref('');
const successMessage = ref('');
const router = useRouter();
const authStore = useAuthStore();

const handleRegister = async () => {
  errorMessage.value = '';
  successMessage.value = '';

  try {
    await apiClient.post('/auth/register', {
      username: username.value,
      password: password.value,
    });
    successMessage.value = 'Регистрация прошла успешно!';
    await authStore.login(username.value, password.value);
    setTimeout(() => {
      router.push('/');
    }, 2000);

  } catch (error) {
    if (error.response && error.response.data.message) {
        errorMessage.value = `Ошибка: ${error.response.data.message}`;
    } else {
        errorMessage.value = 'Ошибка при регистрации.';
    }
  }
};
</script>

<template>
  <div class="auth-wrapper">
    <div class="auth-container">
      <h2>Регистрация</h2>
      
      <form @submit.prevent="handleRegister" class="auth-form">
        <div class="form-group">
          <label for="username">Логин:</label>
          <input type="text" id="username" v-model="username" required placeholder="Придумайте логин">
        </div>
        <div class="form-group">
          <label for="password">Пароль:</label>
          <input type="password" id="password" v-model="password" required placeholder="Придумайте пароль">
        </div>

        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        <p v-if="successMessage" class="success-message">{{ successMessage }}</p>

        <button type="submit" class="auth-button">Зарегистрироваться</button>
        
        <p class="auth-link">
          Уже есть аккаунт? <RouterLink to="/login">Войти</RouterLink>
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
}

.auth-button:hover { 
  background-color: var(--color-primary-hover); 
}

.error-message { 
  color: var(--color-danger); 
  text-align: center; 
  font-weight: bold; 
}

.success-message { 
  color: var(--color-success); 
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
  .auth-container { 
    margin-top: 0; 
    padding: 1.5rem; 
    box-shadow: none;
    border: 1px solid var(--color-border);
  }
}
</style>