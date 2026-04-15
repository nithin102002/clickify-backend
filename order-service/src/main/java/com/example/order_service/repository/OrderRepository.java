package com.example.order_service.repository;

import com.example.order_service.entity.Order;
import com.example.order_service.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    Page<Order> findByUsername(
            String username,
            Pageable pageable
    );

    Page<Order> findByUsernameAndStatus(
            String username,
            OrderStatus status,
            Pageable pageable
    );

    Optional<Order> findByIdAndUsername(Long id, String username);
}
