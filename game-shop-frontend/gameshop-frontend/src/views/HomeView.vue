<script setup>
import { ref, onMounted, reactive, watch } from 'vue';
import apiClient from '@/services/api';
import { useCartStore } from '@/stores/cartStore';

const games = ref([]);
const loading = ref(true);
const error = ref(null);
const cartStore = useCartStore();

const showMobileFilters = ref(false);

const filters = reactive({
  title: '',
  genre: '',
  platform: '',
  minPrice: null,
  maxPrice: null,
  sort: 'id,asc'
});

const genres = ['Аркада', 'Викторина', 'Казино', 'Музыкальная', 'Обучающая', 'Приключения', 'Ролевая' , 'Стратегия', 'Спорт', 'Шутер', 'Экшен'];
const platforms = ['PC', 'PS5', 'PS4', 'Xbox Series X', 'Xbox One', 'Switch'];
const sortOptions = [
  {label: 'Сначала новые', value: 'id,desc'},
  {label: 'Сначала дешевые', value: 'price,asc'},
  {label: 'Сначала дорогие', value: 'price,desc'},
  {label: 'По названию (А-Я)', value: 'title,asc'}
];

const fetchGames = async () => {
  loading.value = true;
  error.value = null;
  try {
    const params = {
      page: 0,
      size: 21,
      sort: filters.sort,
      ...(filters.title && { title: filters.title }),
      ...(filters.genre && { genre: filters.genre }),
      ...(filters.platform && { platform: filters.platform }),
      ...(filters.minPrice && { minPrice: filters.minPrice }),
      ...(filters.maxPrice && { maxPrice: filters.maxPrice})
    }

    const response = await apiClient.get('/games', { params });
    games.value = response.data.content;
  } catch (err) {
    console.error(err)
    error.value = 'Не удалось загрузить игры.';
  } finally {
    loading.value = false;
  }
};

const addToCart = (game) => {
    cartStore.addItem(game);
};

const resetFilters = () => {
  filters.title = '';
  filters.genre = '';
  filters.platform = '';
  filters.minPrice = null;
  filters.maxPrice = null;
  filters.sort = 'id,asc';
  fetchGames();
}

const toggleFilters = () => {
    showMobileFilters.value = !showMobileFilters.value;
}

let timeout = null;
watch(filters, () => {
  clearTimeout(timeout);
  timeout = setTimeout(fetchGames, 500);
})

onMounted(fetchGames);
</script>

<template>
  <div class="home-view">
    <h1>КАТАЛОГ ИГР</h1>
    
    <div v-if="loading" class="loading">Загрузка...</div>
    <div v-if="error" class="error-message">{{ error }}</div>

    <div v-else class="content-wrapper">
      
      <button class="mobile-filter-btn" @click="toggleFilters">
        ⚙️ Фильтры
      </button>

      <aside class="filters-panel" :class="{ 'mobile-visible': showMobileFilters }">
        <div class="filters-header-mobile">
            <h3>Фильтры</h3>
            <button class="close-filters-btn" @click="toggleFilters">✖</button>
        </div>

        <div class="filter-group">
          <label>Поиск по названию:</label>
          <input v-model="filters.title" placeholder="Введите название..." class="input-field">
        </div>

        <div class="filter-group">
          <label>Жанр:</label>
          <select v-model="filters.genre" class="input-field">
            <option value="">Все жанры</option>
            <option v-for="g in genres" :key="g" :value="g">{{ g }}</option>
          </select>
        </div>

        <div class="filter-group">
          <label>Платформа:</label>
          <select v-model="filters.platform" class="input-field">
            <option value="">Все платформы</option>
            <option v-for="p in platforms" :key="p" :value="p">{{ p }}</option>
          </select>
        </div>

        <div class="filter-group price-range">
          <label>Цена (BYN)</label>
          <div class="row">
            <div>
              <input v-model.number="filters.minPrice" type="number" placeholder="от" class="input-field price-input">
            </div>
            <div>
              <input v-model.number="filters.maxPrice" type="number" placeholder="до" class="input-field price-input">
            </div>
          </div>
        </div>
        
        <div class="filter-group">
          <label>Сортировка</label>
          <select v-model="filters.sort" class="input-field">
            <option v-for="opt in sortOptions" :key="opt.value" :value="opt.value">{{ opt.label }}</option>
          </select>
        </div>

        <button @click="resetFilters" class="btn-reset">Сбросить фильтры</button>
        <button class="btn-apply-mobile" @click="toggleFilters">Показать результаты</button>
      </aside>

      <div class="filters-overlay" v-if="showMobileFilters" @click="toggleFilters"></div>

      <main class="game-list">
        <div v-for="game in games" :key="game.id" class="game-card">
        
          <div class="game-image-container">
            <RouterLink :to="{name:'game-details', params: {id:game.id}}">
              <img :src="game.imageUrl" :alt="game.title" class="game-cover">
            </RouterLink>
          </div>
        
          <div class="game-info">
            <RouterLink :to="{name:'game-details', params: {id:game.id}}">
              <h2>{{ game.title }} | {{ game.platform }}</h2>
            </RouterLink>
             <div class="meta-info">
                <span class="genre-tag">{{ game.genre }}</span>
             </div>
            <p class="price">{{ game.price }} BYN</p>
            <button class="add-to-cart-button" @click="addToCart(game)">В корзину</button>
          </div>
        </div>
        <div v-if="games.length === 0" class="no-results">По вашему запросу ничего не найдено :/</div>
      </main>
    </div>
  </div>
</template>

<style scoped>
.home-view {
  padding: 1rem;
  max-width: 1400px;
  margin: 0 auto;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

h1 { 
  text-align: center; 
  margin-bottom: 2rem; 
  color: var(--color-primary); 
}

.content-wrapper {
  display: flex;
  gap: 20px;
  align-items: flex-start;
  position: relative;
}

.filters-panel {
  width: 250px;
  background-color: var(--color-surface);
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  flex-shrink: 0;
  position: sticky;
  top: 20px;
  z-index: 100;
  max-height: 90vh;
  overflow-y: auto;
}

.filters-header-mobile, .mobile-filter-btn, .btn-apply-mobile, .filters-overlay {
  display: none;
}

.filter-group { 
  margin-bottom: 1.25rem; 
}

.filter-group label { 
  display: block; 
  margin-bottom: 0.4rem; 
  font-size: 0.9rem; 
  color: #ccc; 
  font-weight: bold; 
}

.input-field { 
  width: 100%; 
  padding: 0.75rem 0.5rem; 
  border-radius: 4px; 
  border: 1px solid var(--color-border); 
  background: var(--color-background); 
  color: white; 
  box-sizing: border-box; 
}

.price-range .row { 
  display: flex; 
  gap: 0.5rem; 
}

.price-input { 
  text-align: center; 
}

.btn-reset { 
  width: 100%; 
  padding: 0.75rem; 
  background: transparent; 
  border: 1px solid var(--color-danger); 
  color: var(--color-danger); 
  cursor: pointer; 
  border-radius: 4px; 
  margin-top: 1rem; 
  transition: all 0.2s; 
}

.btn-reset:hover { 
  background: var(--color-danger); 
  color: white; 
}

.game-list {
  flex-grow: 1; 
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px; 
}

.no-results { 
  grid-column: 1 / -1; 
  text-align: center; 
  padding: 4rem; 
  color: #999; 
}

.game-card {
  background-color: var(--color-surface);
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
  display: flex;
  flex-direction: column;
  transition: transform 0.2s;
}

.game-card:hover { 
  transform: translateY(-5px); 
}

.game-image-container { 
  width: 100%; 
  height: 200px; 
  overflow: hidden;
}

.game-cover { 
  width: 100%;
  height: 100%; 
  object-fit: cover; 
}

.game-info { 
  padding: 1rem; 
  flex-grow: 1; 
  display: flex;
  flex-direction: column; 
}

.game-info h2 { 
  font-size: 1.1rem; 
  font-weight: 600; 
  margin: 0 0 0.5rem 0; 
  line-height: 1.3;
}

.game-info a { 
  text-decoration: none; 
  color: inherit; 
}

.meta-info { 
  font-size: 0.85rem; 
  color: #aaa; 
  margin-bottom: 0.5rem; 
  display: flex; 
  flex-wrap: wrap; 
  gap: 5px;
}

.platform-tag { 
  background: #333; 
  padding: 2px 6px; 
  border-radius: 4px; 
  color: #fff;
}

.game-info .price { 
  font-size: 1.3rem; 
  font-weight: bold; 
  color: var(--color-primary); 
  margin: 0.75rem 0; 
  margin-top: auto; 
}

.add-to-cart-button {
  margin-top: 0.5rem; 
  width: 100%; 
  padding: 0.75rem; 
  background-color: var(--color-success);
  color: white; 
  border: none; 
  border-radius: 4px; 
  cursor: pointer; 
  font-size: 1rem; 
  font-weight: 600;
}

.add-to-cart-button:hover { 
  background-color: #27ae60; 
}

@media (max-width: 900px) {
  .content-wrapper { 
    flex-direction: column; 
  }
    
  .mobile-filter-btn {
    display: block;
    width: 100%;
    padding: 12px;
    background-color: var(--color-surface);
    border: 1px solid var(--color-border);
    color: var(--color-text);
    font-weight: bold;
    border-radius: 8px;
    margin-bottom: 1rem;
    cursor: pointer;
  }

  .filters-panel {
    position: fixed;
    top: 0;
    left: 0;
    width: 80%;
    height: 100vh;
    z-index: 1000;
    transform: translateX(-100%);
    transition: transform 0.3s ease;
    border-radius: 0;
    max-width: 320px;
  }

  .filters-panel.mobile-visible {
    transform: translateX(0);
    box-shadow: 5px 0 15px rgba(0,0,0,0.5);
  }

  .filters-overlay {
    display: block;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,0.6);
    z-index: 999;
    backdrop-filter: blur(2px);
  }

  .filters-header-mobile {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1.5rem;
  }
    
  .filters-header-mobile h3 { 
    margin: 0; 
  }
    
  .close-filters-btn {
    background: none; border: none; font-size: 1.5rem; color: var(--color-text); cursor: pointer;
  }

  
  .btn-apply-mobile {    
    display: block;
    width: 100%;
    padding: 1rem;
    margin-top: 1rem;
    background-color: var(--color-primary);
    color: white;
    border: none;
    border-radius: 4px;
    font-weight: bold;
  }
}
</style>