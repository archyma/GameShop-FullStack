package com.example.gameshopbackend.controller;

import com.example.gameshopbackend.dto.OrderRequestDto;
import com.example.gameshopbackend.dto.OrderResponseDto;
import com.example.gameshopbackend.dto.UpdateOrderStatusDto;
import com.example.gameshopbackend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/orders")
@Tag(
        name = "Order Controller",
        description = "API для управления заказами"
)
@SecurityRequirement(name = "bearerAuth")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
       this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(
           summary = "Создание нового заказа",
           description = "Доступен только USER"
    )
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto requestDto, Principal principal) {
       String username = principal.getName();
       OrderResponseDto createdOrder = orderService.createOrder(requestDto, username);
       return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('USER')")
    @Operation(
           summary = "Получить все заказы текущего пользователя",
           description = "Доступен только USER"
    )
    public ResponseEntity<List<OrderResponseDto>> getMyOrders(Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(orderService.findOrdersForCurrentUser(username));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Получить ВСЕ заказы ВСЕХ пользователей",
            description = "Доступен только ADMIN"
    )
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }

    @PutMapping("/{ordersId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Изменить статус заказа",
            description = "Доступен только ADMIN"
    )
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long ordersId, @RequestBody UpdateOrderStatusDto statusDto) {
        OrderResponseDto updatedOrder = orderService.updateOrderStatus(ordersId, statusDto.getStatus());
        return ResponseEntity.ok(updatedOrder);
    }
}
