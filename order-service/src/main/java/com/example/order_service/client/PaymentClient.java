package com.example.order_service.client;

import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.PaymentRequest;
import com.example.order_service.dto.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")

public interface PaymentClient {


    @PostMapping("/api/v1/payments")
    ApiResponse<PaymentResponse> processPayment(@RequestBody PaymentRequest request);
}
