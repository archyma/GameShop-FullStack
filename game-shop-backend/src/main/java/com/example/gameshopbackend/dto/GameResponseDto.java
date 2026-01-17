package com.example.gameshopbackend.dto;

public class GameResponseDto {              //отдаем информацию об игре
    private Long id;
    private String title;
    private String platform;
    private String genre;
    private String description;
    private double price;
    String imageUrl;

    public GameResponseDto() {}

    public GameResponseDto(Long id, String title, String platform, String genre, String description, double price, String imageUrl) {
        this.id = id;
        this.title = title;
        this.platform = platform;
        this.genre = genre;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

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
