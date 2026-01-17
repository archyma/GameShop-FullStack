package com.example.gameshopbackend.dto;

public class AuthResponseDto {              //для ответа с токеном - АУТЕНТИФИКАЦИЯ
    private String token;

    public AuthResponseDto(String token) {
        this.token = token;
    }

    public AuthResponseDto() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
