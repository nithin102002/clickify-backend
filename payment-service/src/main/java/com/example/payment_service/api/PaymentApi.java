package com.example.payment_service.api;

import com.example.payment_service.constants.ApiConstants;
import com.example.payment_service.dto.ApiResponse;
import com.example.payment_service.dto.PaymentRequest;
import com.example.payment_service.dto.PaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(ApiConstants.BASIC_API_URL)
public interface PaymentApi {

    @PostMapping(ApiConstants.PAYMENTS)
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(@RequestBody PaymentRequest request);

    @PostMapping(ApiConstants.VERIFY)
    public ResponseEntity<ApiResponse<String>> verifyPayment(
            @RequestParam String razorpayOrderId,
            @RequestParam String razorpayPaymentId,
            @RequestParam String razorpaySignature
    );

    @GetMapping(ApiConstants.PAYMENT_BY_ORDER_ID)
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByOrderId(
            @RequestHeader("X-User") String username,
            @PathVariable Long orderId );
}
