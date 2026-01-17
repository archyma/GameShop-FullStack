package com.example.gameshopbackend.mapper;

import com.example.gameshopbackend.dto.OrderItemResponseDto;
import com.example.gameshopbackend.dto.OrderResponseDto;
import com.example.gameshopbackend.entity.Order;
import com.example.gameshopbackend.entity.OrderGame;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponseDto toDto(Order order){
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setCreatedAt(order.getCreatedAt());

        dto.setUserId(order.getUser().getId());
        dto.setUsername(order.getUser().getUsername());

        dto.setGames(order.getOrderItems().stream()
                .map(this::orderItemToDto)
                .collect(Collectors.toList()));

        return dto;
    }

    public OrderItemResponseDto orderItemToDto(OrderGame item){
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setId(item.getGame().getId());
        dto.setTitle(item.getGame().getTitle());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPriceToBuy());

        return dto;
    }
}