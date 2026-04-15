package com.example.payment_service.controller;

import com.example.payment_service.api.PaymentApi;
import com.example.payment_service.constants.MessageConstants;
import com.example.payment_service.dto.ApiResponse;
import com.example.payment_service.dto.PaymentRequest;
import com.example.payment_service.dto.PaymentResponse;
import com.example.payment_service.enums.ApiStatus;
import com.example.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentApi {

    private final PaymentService paymentService;
    @Override
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(PaymentRequest request) {
        PaymentResponse response=paymentService.processPayment(request);
        return ResponseEntity.ok(
                ApiResponse.<PaymentResponse>builder()
                        .status(ApiStatus.SUCCESS)
                        .message(MessageConstants.PAYMENT_PROCESS_SUCCESS)
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<String>> verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        paymentService.verifyPayment(
                razorpayOrderId,
                razorpayPaymentId,
                razorpaySignature
        );

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .status(ApiStatus.SUCCESS)
                        .message(MessageConstants.PAYMENT_VERIFICATION_SUCCESS)
                        .data("SUCCESS")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }



    @Override
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByOrderId(String username,Long orderId) {
        PaymentResponse response = paymentService.getPaymentByOrderId(username,orderId);

        return ResponseEntity.ok(
                ApiResponse.<PaymentResponse>builder()
                        .status(ApiStatus.SUCCESS)
                        .message("Payment fetched")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }


}
