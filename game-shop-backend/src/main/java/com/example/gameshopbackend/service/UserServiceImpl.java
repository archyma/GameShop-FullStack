package com.example.gameshopbackend.service;

import com.example.gameshopbackend.dto.UserRegistrationDto;
import com.example.gameshopbackend.dto.UserResponseDto;
import com.example.gameshopbackend.entity.Role;
import com.example.gameshopbackend.entity.User;
import com.example.gameshopbackend.repository.RoleRepository;
import com.example.gameshopbackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public UserResponseDto registerNewUser(UserRegistrationDto registrationDto) {
        if(userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already in use");
        }

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(userRole);

        User savedUser = userRepository.save(user);
        UserResponseDto responseDto = new UserResponseDto();

        responseDto.setId(savedUser.getId());
        responseDto.setUsername(savedUser.getUsername());
        responseDto.setRole(savedUser.getRole().getName());

        return responseDto;
    }
}
