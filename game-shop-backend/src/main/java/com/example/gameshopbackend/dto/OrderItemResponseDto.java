package com.example.gameshopbackend.dto;

public class OrderItemResponseDto {
    private Long id;
    private String title;
    private int quantity;
    private Double price;

    public OrderItemResponseDto(Long id, String title, int quantity, Double price) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItemResponseDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
