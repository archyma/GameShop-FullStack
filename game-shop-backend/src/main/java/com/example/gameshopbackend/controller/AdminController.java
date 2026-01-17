package com.example.gameshopbackend.controller;

import com.example.gameshopbackend.dto.UserResponseDto;
import com.example.gameshopbackend.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(
        name = "Admin Controller",
        description = "API для управления пользователями"
)
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Получить всех пользователей",
            description = "Доступен только ADMIN"
    )
    @SecurityRequirement(
            name = "bearerAuth"
    )
    public List<UserResponseDto> getAllUsers() {
        return adminService.getAllUsers();
    }

    @PutMapping("/users/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Изменить роль пользователя",
            description = "Доступен только ADMIN"
    )
    @SecurityRequirement(
            name = "bearerAuth"
    )
    public void changeUserRole(@PathVariable Long id, @RequestParam String roleName){
        adminService.setUserRole(id, roleName);
    }
}
