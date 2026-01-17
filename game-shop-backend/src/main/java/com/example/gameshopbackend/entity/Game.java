package com.example.gameshopbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String platform;
    private String genre;
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(length = 1024)
    private String imageUrl;

    @Column(nullable = false)
    private boolean deleted = false;

    public Game() {}

    public Game(String title, String platform, String genre, String description, double price, String imageUrl, boolean deleted) {
        this.title = title;
        this.platform = platform;
        this.genre = genre;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.deleted = deleted;
    }

    public Long getId(){
        return id;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}