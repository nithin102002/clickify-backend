package com.example.order_service.external;

import com.example.order_service.client.PaymentClient;
import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.PaymentRequest;
import com.example.order_service.dto.PaymentResponse;
import com.example.order_service.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceWrapper {

    private final PaymentClient paymentClient;

    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    public ApiResponse<PaymentResponse> processPayment(PaymentRequest request) {
        return paymentClient.processPayment(request);
    }

    public ApiResponse<PaymentResponse> paymentFallback(PaymentRequest request, Throwable ex) {
        throw new ServiceUnavailableException("Payment service is down. Try again later.");
    }
}