package com.example.gameshopbackend.controller;

import com.example.gameshopbackend.dto.AuthRequestDto;
import com.example.gameshopbackend.dto.UserRegistrationDto;
import com.example.gameshopbackend.dto.UserResponseDto;
import com.example.gameshopbackend.security.JwtUtil;
import com.example.gameshopbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(
        name = "Authentication Controller",
        description = "API для регистрации и входа пользователя"
)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создает нового пользователя с ролью USER"
    )
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegistrationDto registrationDto) {
        return ResponseEntity.ok(userService.registerNewUser(registrationDto));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Вход пользователя",
            description = "Авторизирует пользователя и дает ему JWT-токен"
    )
    public String login(@RequestBody AuthRequestDto authRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        final UserDetails userDetails = (UserDetails)authentication.getPrincipal();

        return jwtUtil.generateToken(userDetails);
    }
}
