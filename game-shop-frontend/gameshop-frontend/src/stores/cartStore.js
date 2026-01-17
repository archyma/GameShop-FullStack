import { defineStore } from 'pinia';
import apiClient from '@/services/api';

export const useCartStore = defineStore('cart', {
  state: () => ({
    items: JSON.parse(localStorage.getItem('cartItems') || '[]'),
    isLoading: false,
    error: null,
  }),

  getters: {
    totalItems: (state) => state.items.reduce((sum, item) => sum + item.quantity, 0),
    totalPrice: (state) => state.items.reduce((sum, item) => sum + item.price * item.quantity, 0).toFixed(2),
  },

  actions: {
    saveCart() {
      localStorage.setItem('cartItems', JSON.stringify(this.items));
    },

    addItem(game) {
      const existingItem = this.items.find(item => item.gameId === game.id);
      if (existingItem) {
        existingItem.quantity++;
      } else {
        this.items.push({
          gameId: game.id,
          title: game.title,
          price: game.price,
          quantity: 1,
          imageUrl: game.imageUrl
        });
      }
      this.saveCart();
    },

    removeItem(gameId) {
      this.items = this.items.filter(item => item.gameId !== gameId);
      this.saveCart();
    },
    
    updateQuantity(gameId, delta) {
      const item = this.items.find(item => item.gameId === gameId);
      if (item) {
        item.quantity += delta;
        if (item.quantity <= 0) {
          this.removeItem(gameId);
        } else {
          this.saveCart();
        }
      }
    },

    clearCart() {
      this.items = [];
      this.saveCart();
    },
    
    async checkout() {
      if (this.items.length === 0) return false;

      this.isLoading = true;
      this.error = null;

      try {
        const itemsForRequest = this.items.map(item => ({
          gameId: item.gameId,
          quantity: item.quantity
        }));
        
        await apiClient.post('/orders', { items: itemsForRequest });
        
        this.clearCart();
        return true; 

      } catch (err) {
        console.error("Ошибка при оформлении заказа:", err);
        this.error = "Не удалось оформить заказ. Попробуйте снова.";
        return false;
      
      } finally {
        this.isLoading = false;
      }
    }
  }
});