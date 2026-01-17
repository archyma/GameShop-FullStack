package com.example.gameshopbackend.service;

import com.example.gameshopbackend.dto.OrderItemRequestDto;
import com.example.gameshopbackend.dto.OrderResponseDto;
import com.example.gameshopbackend.dto.OrderRequestDto;
import com.example.gameshopbackend.exception.ResourceNotFoundException;
import com.example.gameshopbackend.mapper.OrderMapper;
import com.example.gameshopbackend.entity.*;
import com.example.gameshopbackend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, GameRepository gameRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        List<Long> requestedGameIds = requestDto.getItems().stream()
                .map(OrderItemRequestDto::getGameId)
                .collect(Collectors.toList());

        List<Game> games = gameRepository.findAllById(requestedGameIds);

        Map<Long, Game> gameMap = games.stream().collect(Collectors.toMap(Game::getId, g -> g));

        if (games.size() != requestedGameIds.size()) {
            throw new ResourceNotFoundException("One or more games not found");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        List<OrderGame> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemRequestDto itemRequest : requestDto.getItems()) {
            Game game = gameMap.get(itemRequest.getGameId());

            double priceAtPurchase = game.getPrice();
            int quantity = itemRequest.getQuantity();

            OrderGame orderItem = new OrderGame(order, game, quantity, priceAtPurchase);

            orderItems.add(orderItem);
            totalAmount += priceAtPurchase * quantity;
        }

        order.setTotalPrice(totalAmount);
        order.setOrderItems(new HashSet<>(orderItems));

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }


    @Override
    public List<OrderResponseDto> findOrdersForCurrentUser(String username) {
        return orderRepository.findByUserUsername(username)
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> findAllOrders(){
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found, ID: " + orderId));

        OrderStatus status;
        try{
            status = OrderStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value: " + newStatus);
        }

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toDto(updatedOrder);
    }
}
