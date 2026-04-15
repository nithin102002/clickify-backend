package com.example.order_service.controller;

import com.example.order_service.api.OrderApi;
import com.example.order_service.constants.ApiConstants;
import com.example.order_service.constants.MessageConstants;
import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.dto.PageResponse;
import com.example.order_service.enums.ApiStatus;
import com.example.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService orderService;
    @Override
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(String username, OrderRequest request) {
        OrderResponse response = orderService.createOrder(username, request);

        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .status(ApiStatus.SUCCESS)
                        .message(MessageConstants.ORDER_CREATED)
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> getUserOrders(String username, int page, int size, String status, Long orderId) {
        PageResponse<OrderResponse> orders =
                orderService.getOrders(username, page, size, status, orderId);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<OrderResponse>>builder()
                        .status(ApiStatus.SUCCESS)
                        .message(MessageConstants.ORDER_FETCHED)
                        .data(orders)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }


    @Override
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(Long id) {

        OrderResponse order = orderService.getOrderById(id);

        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .status(ApiStatus.SUCCESS)
                        .message(MessageConstants.ORDER_FETCHED)
                        .data(order)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> updateOrderStatus(Long id, String status) {
        orderService.updateStatus(id, status);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(ApiStatus.SUCCESS)
                        .message(MessageConstants.ORDER_FETCHED)
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
