package com.example.gameshopbackend.service;

import com.example.gameshopbackend.dto.UserResponseDto;

import java.util.List;

public interface AdminService {

    List<UserResponseDto> getAllUsers();

    void setUserRole(Long userId, String roleName);
}
