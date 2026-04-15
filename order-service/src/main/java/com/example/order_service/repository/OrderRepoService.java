package com.example.order_service.repository;

import com.example.order_service.entity.Order;
import com.example.order_service.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;


import java.util.List;
import java.util.Optional;

public interface OrderRepoService {

    Order createOrder(Order order);

    Optional<Order> getOrderById(Long id);

    Optional<Order> findByIdAndUsername(Long orderId, String username);


    Page<Order> findByUsername(String username, Pageable pageable);

    Page<Order> findByUsernameAndStatus(
            String username,
            OrderStatus status,
            Pageable pageable
    );
}
