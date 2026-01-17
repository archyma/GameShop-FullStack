<script setup>
import { computed, ref } from 'vue';
import { useCartStore } from '@/stores/cartStore';
import { useRouter } from 'vue-router';
import { RouterLink } from 'vue-router';

const cartStore = useCartStore();
const router = useRouter(); 

const cartItems = computed(() => cartStore.items);
const totalPrice = computed(() => cartStore.totalPrice);
const isLoading = computed(() => cartStore.isLoading);
const error = computed(() => cartStore.error);
const orderSuccess = ref(false);

const removeItem = (id) => cartStore.removeItem(id);
const updateQuantity = (id, delta) => cartStore.updateQuantity(id, delta);

const checkout = async () => {
    orderSuccess.value = false;
    const success = await cartStore.checkout();
    if (success) {
        orderSuccess.value = true;
        setTimeout(() => {
            router.push('/');
        }, 2000);
    }
};
</script>

<template>
  <div class="cart-container">
    <h1>ВАША КОРЗИНА</h1>
    
    <div v-if="orderSuccess" class="success-message">
      <p>Заказ успешно оформлен!</p>
    </div>

    <div v-if="cartItems.length === 0 && !orderSuccess" class="empty-cart">
      <p>Ваша корзина пуста. <RouterLink to="/">Начните покупки!</RouterLink></p>
    </div>

    <div v-if="cartItems.length > 0 && !orderSuccess" class="cart-content">
      
      <div class="cart-list">
        <div v-for="item in cartItems" :key="item.gameId" class="cart-item">
            <img :src="item.imageUrl" :alt="item.title" class="item-cover">
            <div class="item-details">
                <h2>{{ item.title }}</h2>
                <p>Цена: <span>{{ item.price }} BYN</span></p>
            </div>
            <div class="item-quantity">
                <button @click="updateQuantity(item.gameId, -1)" :disabled="isLoading" class="quantity-btn">-</button>
                <span class="quantity-display">{{ item.quantity }}</span>
                <button @click="updateQuantity(item.gameId, 1)" :disabled="isLoading" class="quantity-btn">+</button>
            </div>
            <div class="item-total">
                <p>{{ (item.price * item.quantity).toFixed(2) }} BYN</p>
            </div>
            <button @click="removeItem(item.gameId)" :disabled="isLoading" class="remove-btn">✖</button>
        </div>
      </div>
      
      <div class="cart-summary">
        <h3>Итого:</h3>
        <p class="total-price-display">{{ totalPrice }} BYN</p>
        <button @click="checkout" :disabled="isLoading" class="checkout-btn">
            {{ isLoading ? 'Обработка...' : 'Оформить заказ' }}
        </button>
        <p v-if="error" class="error-message">{{ error }}</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.cart-container { 
  max-width: 1200px; 
  margin: 3rem auto; 
  padding: 1rem; 
}

.cart-content { 
  display: flex; 
  flex-direction: column; 
  gap: 2rem; 
}

.cart-list { 
  flex-grow: 1; 
  display: flex; 
  flex-direction: column; 
  gap: 1rem; 
}

.cart-item { 
  display: flex; 
  flex-wrap: wrap; 
  align-items: center; 
  background-color: var(--color-surface); 
  padding: 1rem; 
  border-radius: 8px; 
}

.item-cover { 
  width: 60px; 
  height: 80px; 
  object-fit: cover; 
  border-radius: 4px; 
  margin-right: 1rem; 
}

.item-details { 
  flex-grow: 1; 
  min-width: 150px; 
}

.item-details h2 { 
  font-size: 1.2rem; 
  color: var(--color-primary); 
  margin: 0 0 0.2rem 0; 
}

.item-quantity { 
  display: flex; 
  align-items: center; 
  margin: 0.5rem 1rem; 
  border: 1px solid var(--color-border); 
  border-radius: 4px; 
}

.quantity-btn { 
  background-color: var(--color-background); 
  border: none; 
  padding: 0.5rem 0.8rem; 
  cursor: pointer; 
  font-size: 1rem; 
}

.quantity-display { 
  padding: 0.5rem 0.5rem; 
}

.item-total { 
  font-weight: bold; 
  font-size: 1.2rem; 
  margin-right: 1rem; 
}

.remove-btn { 
  background: none; 
  color: var(--color-danger); 
  border: none; 
  font-size: 1.2rem; 
  cursor: pointer; 
  margin-left: auto; 
}

.cart-summary { 
  background-color: var(--color-surface);
  padding: 1.5rem; 
  border-radius: 8px; 
}

.total-price-display { 
  font-size: 2rem; 
  color: var(--color-primary); 
  font-weight: bold; 
  margin-bottom: 1.5rem; 
}

.checkout-btn { 
  width: 100%; 
  padding: 1rem; 
  background-color: var(--color-success); 
  color: white; 
  border: none; 
  border-radius: 4px; 
  font-size: 1.1rem; 
  cursor: pointer; 
}

.checkout-btn:hover {
  background-color: #27ae60; 
}

.checkout-btn:disabled { 
  background-color: #7f8c8d; 
  cursor: not-allowed; 
}

.empty-cart { 
  text-align: center; 
  padding: 3rem; 
  background-color: var(--color-surface);
  border-radius: 8px; 
}

.empty-cart a { 
  font-weight: bold; 
}

.success-message { 
  background-color: var(--color-success); 
  color: white; 
  padding: 1.5rem; 
  border-radius: 8px; 
  text-align: center; 
  font-size: 1.2rem; 
  font-weight: 600; 
}

.error-message { 
  color: var(--color-danger); 
  font-weight: bold; 
  text-align: center; 
  margin-top: 1rem; 
}

@media (min-width: 768px) {
    .cart-content { 
      flex-direction: row; 
    }

    .cart-summary { 
      width: 300px; 
      height: fit-content; 
    }
}
</style>
