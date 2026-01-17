package com.example.gameshopbackend.dto;

public class UpdateOrderStatusDto {
    private String status;

    public UpdateOrderStatusDto() {}

    public UpdateOrderStatusDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
