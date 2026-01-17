<script setup>
import { ref, onMounted, reactive, watch } from 'vue';
import apiClient from '@/services/api';
import { useAuthStore } from '@/stores/authStore';

const games = ref([]);
const loading = ref(true);
const error = ref(null);
const authStore = useAuthStore();

const isEditing = ref(false);
const editindId = ref(null);

const genres = ['Аркада', 'Викторина', 'Казино', 'Музыкальная', 'Обучающая', 'Приключения', 'Ролевая' , 'Стратегия', 'Спорт', 'Шутер', 'Экшен'];
const platforms = ['PC', 'PS5', 'PS4', 'Xbox Series X', 'Xbox One', 'Switch'];

const newGameForm = reactive({
  title: '',
  platform: '',
  genre: '',
  description: '',
  price: 0.0,
  imageUrl: '/images/default.jpg'
});

const filters = reactive({
  title: '',
  genre: '',
  platform: '',
  sort: 'id,asc'
});

const fetchGames = async () => {
  loading.value = true;
  try {
    const params = {
      page: 0,
      size: 1000,
      sort: filters.sort,
      ...(filters.title && { title: filters.title }),
      ...(filters.genre && { genre: filters.genre }),
      ...(filters.platform && { platform: filters.platform })
    };

    const response = await apiClient.get('/games', {params});
    games.value = response.data.content;
  } catch (err) {
    error.value = 'Ошибка загрузки игр.';
    console.error(err);
  } finally {
    loading.value = false;
  }
};

watch(filters, () => {
  fetchGames();
});

const startEdit = (game) => {
  isEditing.value = true;
  editindId.value = game.id;

  Object.assign(newGameForm, {
    title: game.title,
    platform: game.platform,
    genre: game.genre,
    description: game.description,
    price: game.price,
    imageUrl: game.imageUrl
  });

  window.scrollTo({ top: 0, behavior:'smooth'});
};

const cancelEdit = () => {
  isEditing.value = false;
  editindId.value = null;
  resetForm();
};

const handleSubmit = async () => {
  try{
    if(isEditing.value){
      await apiClient.put(`/games/${editindId.value}`, newGameForm);
      alert("Игра успешно обновлена!");
    } else{
      await apiClient.post('/games', newGameForm);
      alert("Игра успешно создана!");
    }
    cancelEdit();
    fetchGames();
  } catch(e){
    alert("Ошибка: " + e.response?.data?.message || e.message);
  }
}

const handleDeleteGame = async (gameId) => {
  if (!confirm('Вы уверены, что хотите удалить эту игру?')) {
    return;
  }
  
  try {
    await apiClient.delete(`/games/${gameId}`);
    fetchGames();
  } catch (err) {
    console.error(err);
    alert("Ошибка при удалении игры.");
  }
};

const resetForm = () => {
  Object.assign(newGameForm, { title: '', platform: '', genre: '', description: '' , price: 0.0, imageUrl: '/images/default.jpg'});
};

onMounted(() => {
  authStore.initialize();
  fetchGames();
});
</script>

<template>
  <div class="admin-dashboard">
    <h1>УПРАВЛЕНИЕ ИГРАМИ</h1>

    <form @submit.prevent="handleSubmit" class="admin-form" :class="{'edit-mode': isEditing}">
      <h3>{{ isEditing ? 'Редактировать игру' : 'Добавить новую игру' }}</h3>
      
      <div class="form-grid">
        <div class="form-group">
          <label>Название:</label>
          <input v-model="newGameForm.title" type="text" required>
        </div>
        <div class="form-group">
          <label>Жанр:</label>
          <select v-model="newGameForm.genre" class="new-select" required>
            <option value="" disabled selected>Выберите жанр</option>
            <option v-for="g in genres" :key="g" :value="g">{{ g }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>Платформа:</label>
          <select v-model="newGameForm.platform" class="new-select" required>
            <option value="" disabled selected>Выберите платформу</option>
            <option v-for="p in platforms" :key="p" :value="p">{{ p }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>Цена:</label>
          <input v-model.number="newGameForm.price" type="number" required>
        </div>
        <div class="form-group full-width">
          <label>URL Обложки:</label>
          <input v-model="newGameForm.imageUrl" type="text">
        </div>
        <div class="form-group full-width">
          <label>Описание:</label>
          <textarea v-model="newGameForm.description"></textarea>
        </div>
      </div>
      
      <div class="form-actions">
        <button type="submit" class='btn' :class="isEditing ? 'btn-edit' : 'btn-create'">{{ isEditing ? 'Сохранить' : 'Добавить' }}</button>
        <button v-if="isEditing" type="button" @click="cancelEdit" class="btn btn-cancel">Отмена</button>
      </div>
    </form>

    <hr class="divider">

    <div class="admin-filters">
      <input v-model="filters.title" placeholder="Поиск..." class="search-input">
      <select v-model="filters.sort" class="sort-select">
        <option value="id,desc">Новые</option>
        <option value="id,asc">Старые</option>
        <option value="title,asc">По имени</option>
      </select>
      <select v-model="filters.genre" class="sort-select">
        <option value="">Все жанры</option>
        <option v-for="g in genres" :key="g" :value="g">{{ g }}</option>
      </select>
      <select v-model="filters.platform" class="sort-select">
          <option value="">Все платформы</option>
          <option v-for="p in platforms" :key="p" :value="p">{{ p }}</option>
        </select>
    </div>

    <h3>Список Игр ({{ games.length }})</h3>
    <div v-if="loading">Загрузка...</div>
    
    <div v-if="!loading" class="table-responsive">
        <table class="admin-table">
        <thead>
            <tr>
            <th>ID</th>
            <th>Img</th>
            <th>Инфо</th>
            <th>Цена</th>
            <th>Действия</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="game in games" :key="game.id" :class="{'highlight': game.id === editindId}">
            <td>{{ game.id }}</td>
            <td><img :src="game.imageUrl" class="table-thumb"></td>
            <td>
                <strong>{{ game.title }}</strong><br>
                <small>{{ game.platform }} | {{ game.genre }}</small>
            </td>
            <td style="white-space: nowrap;">{{ game.price }} BYN</td>
            <td class="actions">
                <button @click="startEdit(game)" class="btn btn-edit">✎</button>
                <button @click="handleDeleteGame(game.id)" class="btn btn-delete">✖</button>
            </td>
            </tr>
        </tbody>
        </table>
    </div>

  </div>
</template>

<style scoped>
.admin-dashboard { 
  background-color: var(--color-surface);
  padding: 2rem; 
  border-radius: 8px; 
}

.admin-form { 
  background-color: var(--color-background); 
  padding: 1.5rem; 
  border-radius: 8px; 
  margin-bottom: 2rem; 
}

.form-grid { 
  display: grid; 
  grid-template-columns: 1fr 1fr; 
  gap: 1rem; 
}

.form-group { 
  display: flex; 
  flex-direction: column; 
}

.form-group.full-width { 
  grid-column: 1 / -1; 
}

.form-group input, .form-group textarea, .new-select { 
  padding: 0.75rem; 
  border: 1px solid var(--color-border); 
  border-radius: 4px; 
  background-color: var(--color-surface); 
  color: white;
}


.admin-filters {
  display: flex; 
  gap: 1rem; 
  margin-bottom: 1rem; 
  flex-wrap: wrap; 
}

.search-input, .sort-select { 
  flex-grow: 1; 
  padding: 0.75rem;
  border-radius: 4px; 
  border: 1px solid var(--color-border); 
  background-color: var(--color-background); 
  color: white; 
  box-sizing: border-box;
}

.table-responsive { 
  width: 100%;
  overflow-x: auto; 
}

.admin-table { 
  width: 100%; 
  border-collapse: collapse; 
  min-width: 600px; 
}

.admin-table th, .admin-table td { 
  padding: 0.75rem 0.5rem; 
  border-bottom: 1px solid var(--color-border); 
  text-align: left; 
}

.table-thumb { 
  width: 40px;
  height: 50px; 
  object-fit: cover; 
  border-radius: 4px; 
}

.actions { 
  gap: 0.5rem; 
}

.btn { 
  margin: 0.5rem;
  padding: 0.5rem 1rem; 
  border: none; 
  border-radius: 4px; 
  cursor: pointer; 
  font-weight: 600; 
  color: white; 
}

.btn-create { 
  background-color: var(--color-success); 
}

.btn-edit {
  background-color: var(--color-primary); 
}

.btn-delete { 
  background-color: var(--color-danger); 
}

.btn-cancel { 
    padding: 10px 15px; 
    background-color: var(--color-surface); 
    border: 1px solid var(--color-border); 
    border-radius: 6px; 
    cursor: pointer; 
    color: var(--color-text); 
}

@media (max-width: 768px) {
    .admin-dashboard { 
      padding: 1rem; 
    }
    
    .form-grid { 
      grid-template-columns: 1fr; 
    }
    
    .admin-filters { 
      flex-direction: column; 
    }

    .search-input, .sort-select { 
      width: 100%; 
    }
    
    .btn { 
      padding: 0.5rem; 
    }
}
</style>