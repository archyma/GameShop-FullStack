package com.example.gameshopbackend.dto;

public class OrderItemRequestDto {
    private Long gameId;
    private int quantity;

    public OrderItemRequestDto() {}

    public OrderItemRequestDto(Long gameId, int quantity) {
        this.gameId = gameId;
        this.quantity = quantity;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
