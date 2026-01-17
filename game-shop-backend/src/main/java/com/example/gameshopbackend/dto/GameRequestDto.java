package com.example.gameshopbackend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class GameRequestDto {                //получаем информацию об игре
    @NotBlank(message = "Название не может быть пустым")
    @Size(min = 3, message = "Название должно быть как миннимум 3 символа в длину")
    private String title;

    private String platform;
    private String genre;
    private String description;

    @Min(value = 0, message = "Цена не может быть отрицательной")
    private double price;
    private String imageUrl;

    public GameRequestDto() {}

    public GameRequestDto(String title, String platform, String genre, String description, double price, String imageUrl) {
        this.title = title;
        this.platform = platform;
        this.genre = genre;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
