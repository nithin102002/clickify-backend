package com.example.payment_service.service;

import com.example.payment_service.dto.PaymentRequest;
import com.example.payment_service.dto.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);

    void verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature);

    PaymentResponse getPaymentByOrderId(String username,Long orderId);
}
