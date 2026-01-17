<script setup>
import { ref, onMounted, computed } from 'vue';
import apiClient from '@/services/api';
import { useAuthStore } from '@/stores/authStore';

const orders = ref([]);
const loading = ref(true);
const authStore = useAuthStore();

const statusFilter = ref('');

onMounted(async () => {
  authStore.initialize();
  try {
    const response = await apiClient.get('/orders/my-orders');
    orders.value = response.data;
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
});

const filteredOrders = computed(() => {
  if(!statusFilter.value){
    return orders.value;
  }
  return orders.value.filter(order => order.status === statusFilter.value);
});

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('ru-RU', {
    year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit'
  });
};
</script>

<template>
  <div class="orders-container">
    <h1>МОИ ЗАКАЗЫ</h1>

    <div v-if="loading" class="loading-text">Загрузка ваших заказов...</div>

    <div v-if="!loading && orders.length === 0" class="empty-orders">
      <p>У вас пока нет заказов.</p>
    </div>

    <div v-else>
      <div class="filters">
        <label>Статус:</label>
        <select v-model="statusFilter" class="status-select">
          <option value="">ВСЕ ЗАКАЗЫ</option>
          <option value="PENDING">ГОТОВ К ВЫДАЧЕ</option>
          <option value="COMPLETED">ПОЛУЧЕН</option>
          <option value="CANCELLED">ОТМЕНЕННЫЙ</option>
        </select>
      </div>

      <div v-if="filteredOrders.length === 0" class="no-orders">
        <p>Заказов с таким статусом нет</p>
      </div>

      <div v-else class="order-list">
        <div v-for="order in filteredOrders" :key="order.id" class="order-card">
          <div class="order-header">
            <h3>Заказ №{{ order.id }}</h3>
            <span :class="['status-badge', order.status.toLowerCase()]">
              {{ order.status === 'PENDING' ? 'ГОТОВ К ВЫДАЧЕ' : order.status === 'COMPLETED' ? 'ПОЛУЧЕН' : order.status === 'CANCELLED' ? 'ОТМЕНЕН' : order.status }}
            </span>
          </div>
          <div class="order-body">
            <div class="order-meta">
                <p><strong>Дата:</strong> {{ formatDate(order.createdAt) }}</p>
                <p class="total-price"><strong>Итого:</strong> {{ order.totalPrice }} BYN</p>
            </div>

            <h4>Состав заказа:</h4>
            <ul class="order-items">
              <li v-for="item in order.games" :key="item.gameId">
                <span class="item-title">{{ item.title }}</span>
                <span class="item-details">{{ item.quantity }} шт. × {{ item.price }} BYN</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.orders-container { 
  max-width: 800px; 
  margin: 2rem auto; 
  padding: 0 1rem;
}

h1 { 
  color: var(--color-primary); 
  text-align: center;
  margin-bottom: 2rem;
}

.loading-text { 
  text-align: center; 
  font-size: 1.2rem; 
  margin-top: 2rem;
}

.filters{
  margin-bottom: 1.5rem;
  display: flex;
  gap: 1rem;
  align-items: center;
  justify-content: flex-end;
  flex-wrap: wrap;
}

.status-select{
  padding: 0.5rem;
  border-radius: 4px;
  border: 1px solid var(--color-border);
  background-color: var(--color-surface);
  color: var(--color-text);
  min-width: 150px;
}

.empty-orders, .no-orders { 
  padding: 2rem; 
  background-color: var(--color-surface); 
  text-align: center; 
  border-radius: 8px; 
  color: #999;
}

.order-list { 
  display: flex; 
  flex-direction: column; 
  gap: 1.5rem; 
}

.order-card {
  background-color: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: 8px;
  overflow: hidden;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: var(--color-background);
  padding: 1rem 1.5rem;
  border-bottom: 1px solid var(--color-border);
}

.order-header h3 { 
  margin: 0; 
  color: var(--color-primary); 
}

.order-body { 
  padding: 1.5rem; 
}

.order-meta { 
  margin-bottom: 1rem; 
  border-bottom: 1px solid var(--color-border); 
  padding-bottom: 1rem;
}

.total-price { 
  font-size: 1.1rem; 
  color: var(--color-success); 
}

.order-items { 
  list-style: none; 
  padding: 0; 
  margin: 0; 
}

.order-items li { 
  display: flex; 
  justify-content: space-between;
  padding: 0.5rem 0;
  border-bottom: 1px solid #333;
}

.order-items li:last-child {
  border-bottom: none; 
}

.status-badge{
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: bold;
  color: white;
  text-transform: uppercase;
  white-space: nowrap;
}

.status-badge.pending{ 
  background-color: #f1c40f; 
  color: #333; 
}

.status-badge.completed{ 
  background-color: #2ecc71; 
}

.status-badge.cancelled{ 
  background-color: #e74c3c; 
}

@media (max-width: 600px) {
  .filters {
    flex-direction: column;
    align-items: stretch;
  }
    
  .status-select { 
    width: 100%; 
  }

  .order-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
    
  .order-items li {
    flex-direction: column;
  }

  .item-details {
    color: #aaa;
    font-size: 0.9rem;
  }
}
</style>