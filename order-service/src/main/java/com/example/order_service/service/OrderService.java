package com.example.order_service.service;

import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.dto.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;


public interface OrderService {
    public OrderResponse createOrder(String username, OrderRequest request);

    PageResponse<OrderResponse> getOrders(
            String username,
            int page,
            int size,
            String status,
            Long orderId
    );

    OrderResponse getOrderById(Long id);

    void updateStatus(Long id, String status);
}
