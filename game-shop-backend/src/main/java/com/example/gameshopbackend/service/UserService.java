package com.example.gameshopbackend.service;

import com.example.gameshopbackend.dto.UserRegistrationDto;
import com.example.gameshopbackend.dto.UserResponseDto;

public interface UserService {
    UserResponseDto registerNewUser(UserRegistrationDto registrationDto);
}
