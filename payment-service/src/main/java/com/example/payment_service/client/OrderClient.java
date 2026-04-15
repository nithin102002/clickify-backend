package com.example.payment_service.client;

import com.example.payment_service.dto.ApiResponse;
import com.example.payment_service.dto.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service")
public interface OrderClient {

    @PutMapping("/api/v1/orders/{id}/status")
    void updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status
    );
    @GetMapping("/api/v1/orders/{id}")
    ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id);
}
