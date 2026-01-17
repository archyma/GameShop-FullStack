<script setup>
import { ref, onMounted, computed } from 'vue';
import apiClient from '@/services/api';
import { useAuthStore } from '@/stores/authStore';

const originalOrders = ref([]); 
const loading = ref(true);
const authStore = useAuthStore();

const searchText = ref('');
const statusFilter = ref('');
const sortKey = ref('createdAt');
const sortDirection = ref('desc');

const formatDate = (dateString) =>{
    if(!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString('ru-RU',
        {
            year: 'numeric', month: 'short',
            day: 'numeric', hour: '2-digit',
            minute: '2-digit'
        }
    );
};

const fetchAllOrders = async() => {
    loading.value = true;
    try{
        const response = await apiClient.get('/orders');
        originalOrders.value = response.data;
    } catch(e){
        console.error("Ошибка загрузки заказов:", e);
    } finally{
        loading.value = false;
    }
};

onMounted(() => {
    authStore.initialize();
    fetchAllOrders();
});

const sortBy = (key) => {
    if (sortKey.value === key) {
        sortDirection.value = sortDirection.value === 'asc' ? 'desc' : 'asc';
    } else {
        sortKey.value = key;
        sortDirection.value = 'asc';
    }
};

const handleStatusChange = async(ordersId, newStatus) =>{
    if(!newStatus) return;

    try{
        await apiClient.put(`/orders/${ordersId}/status`, { status: newStatus });

        const i = originalOrders.value.findIndex(o => o.id === ordersId);
        if(i !== -1){
            originalOrders.value[i].status = newStatus;
        }
    } catch(e){
        alert("Ошибка при обновлении статуса!!!");
        console.error(e.response?.status, e.response?.data);
    }
};

const filteredOrders = computed(() => {
    let list = originalOrders.value;
    const search = searchText.value.toLowerCase();
    
    if (statusFilter.value) {
        list = list.filter(order => order.status === statusFilter.value);
    }

    if (search) {
        list = list.filter(order => {
            const userId = String(order.id); 
            const username = order.username ? order.username.toLowerCase() : '';
            const totalPrice = String(order.totalPrice); 

            return username.includes(search) || userId.includes(search) || totalPrice.includes(search);
        });
    }
    return list;
});

const sortedAndFilteredOrders = computed(() => {
    const list = [...filteredOrders.value]; 
    const key = sortKey.value;
    const direction = sortDirection.value;

    return list.sort((a, b) => {
        const aValue = a[key];
        const bValue = b[key];
        
        let comparison = 0;

        if (key === 'createdAt') {
            comparison = new Date(aValue) - new Date(bValue);
        } else if (typeof aValue === 'string') {
            comparison = aValue.localeCompare(bValue);
        } else {
            comparison = aValue - bValue;
        }
        
        return direction === 'asc' ? comparison : -comparison;
    });
});
</script>

<template>
    <div class="admin-dashboard">
        <h1>УПРАВЛЕНИЕ ЗАКАЗАМИ</h1>

        <div v-if="loading">Загрузка...</div>

        <div v-else>
            <div class="controls mb-4">
                <input type="text" v-model="searchText" placeholder="Поиск..." class="form-control me-3" />
                <select v-model="statusFilter" class="form-select">
                    <option value="">ВСЕ ЗАКАЗЫ</option>
                    <option value="PENDING">ГОТОВ К ВЫДАЧЕ</option>
                    <option value="COMPLETED">ПОЛУЧЕН</option>
                    <option value="CANCELLED">ОТМЕНЕННЫЙ</option>
                </select>
                <button @click="searchText = ''; statusFilter = ''" class="btn-clear">Сброс</button>
            </div>

            <div class="table-responsive">
                <table class="admin-table">
                     <thead>
                        <tr>
                            <th @click="sortBy('id')" class="sortable">ID</th>
                            <th @click="sortBy('username')" class="sortable">User</th>
                            <th @click="sortBy('createdAt')" class="sortable">Дата</th>
                            <th @click="sortBy('totalPrice')" class="sortable">Сумма</th>
                            <th>Статус</th>
                            <th>Изменить</th>
                            <th>Состав</th>
                        </tr>
                     </thead>
                     <tbody>
                        <tr v-for="order in sortedAndFilteredOrders" :key="order.id">
                            <td>{{ order.id }}</td>
                            <td>{{ order.username }}</td>
                            <td>{{ formatDate(order.createdAt) }}</td>
                            <td style="white-space: nowrap;">{{ order.totalPrice }} BYN</td>
                            <td>
                                <span :class="['status-badge', order.status.toLowerCase()]">
                                    {{ order.status === 'PENDING' ? 'ГОТОВ К ВЫДАЧЕ' : order.status === 'COMPLETED' ? 'ПОЛУЧЕН' : order.status === 'CANCELLED' ? 'ОТМЕНЕН' : order.status }}
                                </span>
                            </td>
                            <td>
                                <select :value="order.status" @change="handleStatusChange(order.id, $event.target.value)" class="status-select">
                                    <option value="PENDING">ГОТОВ</option>
                                    <option value="COMPLETED">ПОЛУЧЕН</option>
                                    <option value="CANCELLED">ОТМЕНЕН</option>
                                </select>
                            </td>
                            <td>
                                <ul class="item-list-small">
                                    <li v-for="item in order.games" :key="item.id">{{ item.title }} ({{ item.quantity }})</li>
                                </ul>
                            </td>
                        </tr>
                     </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<style scoped>
.admin-dashboard{ 
    width: 100%; 
    padding: 1rem; 
    box-sizing: border-box; 
}

.controls { 
    display: flex; 
    gap: 10px; 
    margin-bottom: 20px; 
    flex-wrap: wrap; 
}

.form-control, .form-select { 
    padding: 10px; 
    border: 1px solid var(--color-border); 
    border-radius: 6px; 
    background-color: var(--color-background); 
    color: white; 
    min-width: 150px; 
    box-sizing: border-box;
}

.btn-clear { 
    padding: 10px 15px; 
    background-color: var(--color-surface); 
    border: 1px solid var(--color-border); 
    border-radius: 6px; 
    cursor: pointer; 
    color: var(--color-text); 
}

.table-responsive { 
    width: 100%; 
    overflow-x: auto; 
    padding-bottom: 1rem; 
}

.admin-table { 
    width: 100%; 
    border-collapse: collapse; 
    min-width: 800px; 
}

.admin-table th, .admin-table td{ 
    padding: 0.75rem 1rem; 
    border-bottom: 1px solid var(--color-border); 
    text-align: left; 
}

.status-badge{ 
    padding: 0.3rem 0.6rem; 
    border-radius: 4px; 
    font-weight: bold; 
    font-size: 0.8rem; 
}

.status-select{
    padding: 0.3rem 0.6rem;
}

.status-badge.pending{ 
    background-color: #f39c12;
    color: white; 
}

.status-badge.completed{ 
    background-color: #2ecc71; 
    color: white; 
}

.status-badge.cancelled{ 
    background-color: #e74c3c; 
    color: white; 
}

.item-list-small { 
    list-style: none; 
    padding: 0; 
    margin: 0; 
    font-size: 0.8rem; 
    min-width: 150px; 
}

@media (max-width: 768px) {
    .controls { 
        flex-direction: column; 
    }

    .form-control, .form-select, .btn-clear { 
        width: 100%; 
    }
}
</style>