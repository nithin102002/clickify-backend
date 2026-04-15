package com.example.graphql_gateway.external;

import com.example.graphql_gateway.client.PaymentClient;
import com.example.graphql_gateway.dto.ApiResponse;
import com.example.graphql_gateway.dto.PaymentResponse;
import com.example.graphql_gateway.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceWrapper {

    private final PaymentClient paymentClient;

    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    public ApiResponse<PaymentResponse> getPayment(String username,Long orderId) {
        try {
            return paymentClient.getPaymentByOrderId(username, orderId);
        } catch (Exception ex) {
            System.out.println("Payment error: " + ex.getMessage());
            return null;
        }
    }

    public ApiResponse<PaymentResponse> paymentFallback(String username,Long orderId, Throwable ex) {

        return new ApiResponse<>(
                "SUCCESS",
                "Payment unavailable",
                new PaymentResponse("UNKNOWN", BigDecimal.ZERO)
        );
    }

}