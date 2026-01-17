package com.example.gameshopbackend.service;

import com.example.gameshopbackend.dto.UserResponseDto;
import com.example.gameshopbackend.entity.Role;
import com.example.gameshopbackend.entity.User;
import com.example.gameshopbackend.exception.ResourceNotFoundException;
import com.example.gameshopbackend.repository.RoleRepository;
import com.example.gameshopbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AdminServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserResponseDto dto = new UserResponseDto();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setRole(user.getRole().getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void setUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Role newRole = roleRepository.findByName(roleName).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        user.setRole(newRole);
        userRepository.save(user);
    }
}
