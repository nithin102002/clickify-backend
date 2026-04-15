package com.example.graphql_gateway.client;

import com.example.graphql_gateway.dto.ApiResponse;
import com.example.graphql_gateway.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @GetMapping("/api/v1/payments/order/{orderId}")
    ApiResponse<PaymentResponse> getPaymentByOrderId(@RequestHeader("X-User") String username, @PathVariable Long orderId);
}
