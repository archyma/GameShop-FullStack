package com.example.gameshopbackend.dto;

import com.example.gameshopbackend.entity.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {
    private Long id;
    private Long userId;
    private String username;
    private List<OrderItemResponseDto> games;
    private OrderStatus status;
    private Double totalPrice;
    private LocalDateTime createdAt;

    public OrderResponseDto() {}

    public OrderResponseDto(Long id, Long userId, String username, List<OrderItemResponseDto> games, OrderStatus status, Double price, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.games = games;
        this.status = status;
        this.totalPrice = price;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<OrderItemResponseDto> getGames() {
        return games;
    }

    public void setGames(List<OrderItemResponseDto> games) {
        this.games = games;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
