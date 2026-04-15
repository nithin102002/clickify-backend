package com.example.payment_service.external;

import com.example.payment_service.client.OrderClient;
import com.example.payment_service.dto.ApiResponse;
import com.example.payment_service.dto.OrderResponse;
import com.example.payment_service.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceWrapper {

    private final OrderClient orderClient;

    @CircuitBreaker(name = "orderService", fallbackMethod = "getOrderFallback")
    public ApiResponse<OrderResponse> getOrder(Long orderId) {
        ResponseEntity<ApiResponse<OrderResponse>> response =
                orderClient.getOrderById(orderId);

        return response.getBody();
    }

    public ApiResponse<OrderResponse> getOrderFallback(Long orderId, Throwable ex) {
        throw new ServiceUnavailableException("Order service unavailable (fallback triggered)");
    }

    @CircuitBreaker(name = "orderService", fallbackMethod = "updateOrderFallback")
    public void updateOrder(Long orderId, String status) {
        orderClient.updateOrderStatus(orderId, status);
    }

    public void updateOrderFallback(Long orderId, String status, Throwable ex) {
        throw new ServiceUnavailableException("Order service unavailable while updating status");
    }
}