<script setup>
import { ref } from 'vue';
import { RouterView, RouterLink } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';
import { useCartStore } from '@/stores/cartStore';

const authStore = useAuthStore();
const cartStore = useCartStore();

const isMenuOpen = ref(false);

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value;
};

const closeMenu = () => {
  isMenuOpen.value = false;
};
</script>

<template>
  <div id="main-content">
    <header>
      <nav>
        <RouterLink v-if="authStore.isAdmin" to="/admin/games" class="nav-link logo" @click="closeMenu">AAMK Games</RouterLink>
        <RouterLink v-else to="/" class="nav-link logo" @click="closeMenu">AAMK Games</RouterLink>

        <button class="burger-btn" @click="toggleMenu" :class="{ open: isMenuOpen }">
          <span></span>
          <span></span>
          <span></span>
        </button>

        <div class="nav-links" :class="{ open: isMenuOpen }">
          
          <template v-if="authStore.isAdmin">
            <RouterLink to="/admin/games" class="nav-link admin-link" @click="closeMenu">Игры</RouterLink>
            <RouterLink to="/admin/orders" class="nav-link admin-link" @click="closeMenu">Заказы</RouterLink>
            <RouterLink to="/admin/users" class="nav-link admin-link" @click="closeMenu">Пользователи</RouterLink>
          </template>

          <template v-else>
            <RouterLink v-if="authStore.isLoggedIn" to="/cart" class="nav-link cart-link" @click="closeMenu">
              Корзина ({{ cartStore.totalItems }})
            </RouterLink>
            <RouterLink v-if="authStore.isLoggedIn" to="/my-orders" class="nav-link" @click="closeMenu">Мои Заказы</RouterLink>
          </template>
          
          <div class="auth-links-mobile">
            <span v-if="authStore.isLoggedIn" class="user-block">
              <span class="username">Привет, {{ authStore.username }}!</span>
              <a @click="authStore.logout(); closeMenu()" class="nav-link logout-btn">Выйти</a>
            </span>
            <span v-else class="guest-block">
              <RouterLink to="/login" class="nav-link" @click="closeMenu">Войти</RouterLink>
              |
              <RouterLink to="/register" class="nav-link" @click="closeMenu">Регистрация</RouterLink>
            </span>
          </div>
        </div>
      </nav>
    </header>

    <main>
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
header {
  background-color: var(--color-surface);
  padding: 1rem 2rem;
  border-bottom: 1px solid var(--color-border);
  position: relative;
  z-index: 1000;
}

nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  flex-grow: 1;
  justify-content: flex-end;
}

.nav-link {
  color: var(--color-text);
  font-weight: 500;
  cursor: pointer;
  text-decoration: none;
  white-space: nowrap;
}

.nav-link:hover { 
  color: var(--color-primary); 
}

.logo { 
  font-size: 1.5rem; 
  font-weight: 700; 
  color: var(--color-primary); 
  margin-right: auto; 
}

.cart-link { 
  font-weight: bold; 
}

.auth-links-mobile {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-left: 1rem;
}

.username { 
  margin-right: 1rem; 
  font-style: italic; 
}

.logout-btn { 
  background-color: var(--color-danger); 
  padding: 0.5rem 1rem;
  border-radius: 4px; 
  color: white; 
}

.admin-link{ 
  font-weight: bold;
  color: var(--color-danger); 
}

.burger-btn {
  display: none;
  background: none;
  border: none;
  cursor: pointer;
  flex-direction: column;
  gap: 5px;
  z-index: 1001;
}

.burger-btn span {
  width: 25px;
  height: 3px;
  background-color: var(--color-text);
  transition: 0.3s;
}

main {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

@media (max-width: 768px) {
  header { 
    padding: 1rem; 
  }
  
  .burger-btn { 
    display: flex; 
  }

  .burger-btn.open span:nth-child(1) { 
    transform: rotate(45deg) translate(5px, 6px); 
  }
  
  .burger-btn.open span:nth-child(2) {
    opacity: 0; 
  }

  .burger-btn.open span:nth-child(3) { 
    transform: rotate(-45deg) translate(5px, -6px); 
  }

  .nav-links {
    position: absolute;
    top: 100%;
    left: 0;
    width: 100%;
    background-color: var(--color-surface);
    flex-direction: column;
    align-items: center;
    padding: 2rem 0;
    gap: 2rem;
    border-bottom: 1px solid var(--color-border);
    display: none; 
    box-shadow: 0 10px 10px rgba(0,0,0,0.5);
  }

  .nav-links.open { 
    display: flex; 
  }

  .auth-links-mobile {
    flex-direction: column;
    margin: 0;
    gap: 1.5rem;
  }
  
  .user-block, .guest-block {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 1rem;
  }
  
  .username { 
    margin: 0; 
  }
}
</style>