package com.example.gameshopbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_games")
public class OrderGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price_to_buy")
    private double priceToBuy;

    public OrderGame() {}

    public OrderGame(Order order, Game game, int quantity, double priceToBuy) {
        this.order = order;
        this.game = game;
        this.quantity = quantity;
        this.priceToBuy = priceToBuy;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Game getGame() {
        return game;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPriceToBuy() {
        return priceToBuy;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPriceToBuy(double priceToBuy) {
        this.priceToBuy = priceToBuy;
    }
}
