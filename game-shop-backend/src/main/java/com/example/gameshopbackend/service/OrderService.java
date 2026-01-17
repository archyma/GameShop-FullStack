package com.example.gameshopbackend.service;

import com.example.gameshopbackend.dto.OrderRequestDto;
import com.example.gameshopbackend.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {

    OrderResponseDto createOrder(OrderRequestDto requestDto, String username);

    List<OrderResponseDto> findOrdersForCurrentUser(String username);

    List<OrderResponseDto> findAllOrders();

    OrderResponseDto updateOrderStatus(Long orderId, String newStatus);
}
