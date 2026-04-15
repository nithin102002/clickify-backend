package com.example.order_service.repository.impl;


import com.example.order_service.entity.Order;
import com.example.order_service.enums.OrderStatus;
import com.example.order_service.repository.OrderRepoService;
import com.example.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderRepoServiceImpl implements OrderRepoService {

    private final OrderRepository orderRepository;


    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }


    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Optional<Order> findByIdAndUsername(Long orderId, String username) {
        return orderRepository.findByIdAndUsername(orderId, username);
    }


    @Override
    public Page<Order> findByUsername(String username, Pageable pageable) {
        return orderRepository.findByUsername(username, pageable);
    }

    @Override
    public Page<Order> findByUsernameAndStatus(
            String username,
            OrderStatus status,
            Pageable pageable
    ) {
        return orderRepository.findByUsernameAndStatus(username, status, pageable);
    }
}
