<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import apiClient from '@/services/api';
import { useCartStore } from '@/stores/cartStore';

const route = useRoute();
const router = useRouter();
const cartStore = useCartStore();

const game = ref(null);
const loading = ref(true);

const fetchGame = async () => {
  try {
    const response = await apiClient.get(`/games/${route.params.id}`);
    game.value = response.data;
  } catch (err) {
    console.error(err);
  } finally {
    loading.value = false;
  }
};

const addToCart = () => {
  if (game.value) {
    cartStore.addItem(game.value);
  }
};

const goBack = () => router.back();

onMounted(fetchGame);
</script>

<template>
  <div class="details-container">
    <button @click="goBack" class="btn-back">← Назад</button>

    <div v-if="loading" class="loading">Загрузка...</div>
    
    <div v-else-if="game" class="game-content">
      <div class="image-section">
        <img :src="game.imageUrl" :alt="game.title">
      </div>

      <div class="info-section">
        <h1>{{ game.title }}</h1>
        
        <div class="meta-tags">
          <span class="tag genre">{{ game.genre }}</span>
          <span class="tag platform">{{ game.platform }}</span>
        </div>

        <p class="description">{{ game.description }}</p>

        <div class="purchase-block">
          <span class="price">{{ game.price }} BYN</span>
          <button @click="addToCart" class="btn-add">В корзину</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.details-container { 
  max-width: 1200px; 
  margin: 2rem auto; 
  padding: 0 1rem; 
}

.btn-back { 
  background: none; border: none; color: #aaa; cursor: pointer; 
  font-size: 1.1rem; margin-bottom: 1rem; transition: 0.2s;
}

.btn-back:hover { 
  color: white; 
}

.game-content { 
  display: grid; 
  grid-template-columns: 1fr 1.5fr; 
  gap: 3rem; 
  background-color: var(--color-surface); 
  padding: 2rem; 
  border-radius: 12px; 
}

.image-section img { 
  width: 100%; 
  height: auto;
  border-radius: 8px; 
  box-shadow: 0 4px 12px rgba(0,0,0,0.5); 
  object-fit: cover;
}

.info-section { 
  display: flex; 
  flex-direction: column; 
}

.info-section h1 { 
  margin-top: 0;
  color: white; 
  font-size: 2.5rem; 
  line-height: 1.2;
}

.meta-tags {
  display: flex; 
  gap: 1rem; 
  margin: 1rem 0; 
  flex-wrap: wrap; 
}

.tag { 
  padding: 0.4rem 1rem; 
  border-radius: 20px; 
  font-weight: bold; 
  font-size: 0.9rem; 
}

.tag.genre { 
  background-color: var(--color-primary); 
  color: white; 
}

.tag.platform { 
  background-color: #475569; 
  color: white; 
}

.description { 
  font-size: 1.1rem; 
  line-height: 1.6; 
  color: #ccc; 
  margin-bottom: 2rem; 
}

.purchase-block { 
  display: flex; 
  align-items: center; 
  gap: 2rem; 
  margin-top: auto; 
  flex-wrap: wrap;
}

.price { 
  font-size: 2rem; 
  font-weight: bold; 
  color: var(--color-success);
  white-space: nowrap;
}

.btn-add { 
  padding: 1rem 2rem; 
  background-color: var(--color-primary); 
  color: white; 
  border: none; 
  border-radius: 8px; 
  font-size: 1.2rem; 
  font-weight: bold; 
  cursor: pointer; 
  transition: 0.2s; 
  flex-grow: 1; 
  text-align: center;
}

.btn-add:hover { 
  background-color: var(--color-primary-hover); 
}

@media (max-width: 900px) {
  .game-content { 
    grid-template-columns: 1fr;
    gap: 1.5rem;
    padding: 1.5rem;
  }
  
  .info-section h1 { 
    font-size: 1.8rem; 
  }
  
  .purchase-block {
    width: 100%;
    justify-content: space-between;
    gap: 1rem;
  }
}
</style>