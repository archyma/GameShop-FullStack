<script setup>
import { ref, onMounted, computed } from 'vue';
import apiClient from '@/services/api';
import { useAuthStore } from '@/stores/authStore';

const users = ref([]);
const loading = ref(true);
const authStore = useAuthStore();

const searchQuery = ref('');
const roleFilter = ref('');

const fetchUsers = async () => {
  try {
    const response = await apiClient.get('/admin/users');
    users.value = response.data;
  } catch (err) {
    console.error(err);
  } finally {
    loading.value = false;
  }
};

const switchRole = async (user) => {
  const currentRole = user.role;
  const newRole = currentRole === 'ROLE_ADMIN' ? 'ROLE_USER' : 'ROLE_ADMIN';

  if (user.username === authStore.username && newRole === 'ROLE_USER') {
    alert("Нельзя изменить свою роль!");
    return;
  }

  if (!confirm(`Сменить роль пользователя ${user.username} на ${newRole}?`)) return;

  try {
    await apiClient.put(`/admin/users/${user.id}/role`, null, {
      params: { roleName: newRole }
    });
    fetchUsers();
  } catch (err) {
    alert("Ошибка смены роли");
    console.error(err);
  }
};

const filteredUsers = computed(() => {
  let res = users.value;

  if(roleFilter.value){
    res = res.filter(u => u.role === roleFilter.value);
  }

  if(searchQuery.value){
    const query = searchQuery.value.toLowerCase();
    res = res.filter(u => u.username.toLowerCase().includes(query))
  }

  return res;
});

onMounted(fetchUsers);
</script>

<template>
  <div class="admin-dashboard">
    <h1>УПРАВЛЕНИЕ ПОЛЬЗОВАТЕЛЯМИ</h1>

    <div v-if="loading">Загрузка...</div>

    <div v-else>
      <div class="controls">
        <input v-model="searchQuery" placeholder="Поиск по нику..." class="search-input">
        <select v-model="roleFilter" class="role-select">
          <option value="">ВСЕ РОЛИ</option>
          <option value="ROLE_ADMIN">ADMIN</option>
          <option value="ROLE_USER">USER</option>
        </select>
      </div>

      <div class="table-responsive">
        <table class="admin-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Роль</th>
                <th>Действие</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="user in filteredUsers" :key="user.id">
                <td>{{ user.id }}</td>
                <td>{{ user.username }}</td>
                <td>
                <span :class="['role-badge', user.role === 'ROLE_ADMIN' ? 'admin' : 'user']">
                    {{ user.role.replace('ROLE_', '') }}
                </span>
                </td>
                <td>
                <button @click="switchRole(user)" class="btn btn-switch">
                    {{ user.role === 'ROLE_ADMIN' ? 'Понизить' : 'Повысить' }}
                </button>
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
  margin-top: 1rem; 
}

h1 { 
  color: white; 
  margin-bottom: 1.5rem; 
  font-size: 1.5rem; 
}

.controls{ 
  display: flex; 
  gap: 1rem; 
  margin-bottom: 1.5rem; 
  flex-wrap: wrap; 
}

.search-input{ 
  flex-grow: 1; 
  padding: 0.7rem; 
  border-radius: 4px; 
  border: 1px solid var(--color-border); 
  background-color: var(--color-background); 
  color: white; 
  min-width: 150px; 
  box-sizing: border-box;
}

.role-select{ 
  padding: 0.7rem; 
  border-radius: 4px; 
  border: 1px solid var(--color-border); 
  background-color: var(--color-background); 
  color: white; 
  min-width: 150px; 
}

.table-responsive {
    width: 100%;
    overflow-x: auto;
    -webkit-overflow-scrolling: touch;
}

.admin-table {
  width: 100%; 
  border-collapse: collapse; 
  min-width: 600px;
}

.admin-table th, .admin-table td{ 
  padding: 0.75rem 1rem; 
  border-bottom: 1px solid var(--color-border); 
  text-align: left; 
}

.role-badge { 
  display: inline-block; 
  padding: 0.3rem 0.7rem; 
  border-radius: 12px; 
  font-size: 0.85rem; 
  font-weight: 700; 
  text-transform: uppercase; 
  min-width: 80px; 
  text-align: center; 
}

.role-badge.admin { 
  color: var(--color-danger); 
  background-color: rgba(231, 76, 60, 0.15); 
  border: 1px solid var(--color-danger) 
}

.role-badge.user { 
  color: var(--color-primary); 
  background-color: rgba(52, 152, 219, 0.15); 
  border: 1px solid var(--color-primary); 
}

.btn-switch { 
  background-color: var(--color-primary); 
  color: white; 
  padding: 0.5rem; 
  border: none; 
  border-radius: 4px; 
  cursor: pointer; 
  white-space: nowrap; 
}

@media (max-width: 600px) {
    .controls { 
      flex-direction: column; 
    }

    .search-input, .role-select { 
      width: 100%; 
    }
}
</style>