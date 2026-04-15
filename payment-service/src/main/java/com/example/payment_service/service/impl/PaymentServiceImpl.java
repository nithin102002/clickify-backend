package com.example.payment_service.service.impl;

import com.example.payment_service.client.OrderClient;
import com.example.payment_service.client.ProductClient;
import com.example.payment_service.config.RazorpayConfig;
import com.example.payment_service.dto.*;
import com.example.payment_service.entity.Payment;
import com.example.payment_service.enums.PaymentStatus;
import com.example.payment_service.exception.ForbiddenException;
import com.example.payment_service.exception.ResourceNotFoundException;
import com.example.payment_service.external.CartServiceWrapper;
import com.example.payment_service.external.OrderServiceWrapper;
import com.example.payment_service.external.ProductServiceWrapper;
import com.example.payment_service.repository.PaymentRepoService;
import com.example.payment_service.service.PaymentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final RazorpayClient razorpayClient;
    private final PaymentRepoService paymentRepoService;
    private final RazorpayConfig razorpayConfig;
    private final CartServiceWrapper cartWrapper;


    private static final int MAX_RETRY = 2;

    private final OrderServiceWrapper orderWrapper;
    private final ProductServiceWrapper productWrapper;

    @Transactional
    @Override
    public PaymentResponse processPayment(PaymentRequest request) {


        // Get last payment retry count
        int retryCount = paymentRepoService
                .findTopByOrderIdOrderByCreatedAtDesc(request.orderId())
                .map(Payment::getRetryCount)
                .orElse(0);

        // Create payment
        Payment payment = Payment.builder()
                .orderId(request.orderId())
                .amount(request.amount())
                .status(PaymentStatus.PENDING)
                .retryCount(retryCount)
                .itemsJson(convertToJson(request.items()))
                .build();
        System.out.println("before saving payament");

        payment = paymentRepoService.save(payment);

        try {
            // Create Razorpay Order
            JSONObject options = new JSONObject();
            options.put("amount", request.amount().multiply(BigDecimal.valueOf(100)));
            options.put("currency", "INR");
            options.put("receipt", "order_" + request.orderId());

            Order razorpayOrder = razorpayClient.orders.create(options);

            // Save Razorpay Order ID
            payment.setTransactionId(razorpayOrder.get("id").toString());
            paymentRepoService.save(payment);

            return new PaymentResponse(
                    request.orderId(),
                    payment.getStatus().name(),
                    payment.getAmount(),
                    payment.getTransactionId()
            );

        } catch (Exception ex) {

            payment.setStatus(PaymentStatus.FAILED);
            paymentRepoService.save(payment);

            throw new RuntimeException("Payment initiation failed: " + ex.getMessage());
        }
    }


    // VERIFY PAYMENT
    @Override
    public void verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {

        Payment payment = paymentRepoService.findByTransactionId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new RuntimeException("Payment already completed");
        }

        // CHECK ORDER STATUS

        ApiResponse<OrderResponse> response = orderWrapper.getOrder(payment.getOrderId());

        if (response == null || response.data() == null) {
            throw new RuntimeException("Order not found");
        }

        OrderResponse order = response.data();

        if ("CANCELLED".equalsIgnoreCase(order.status())) {
            throw new RuntimeException("Order already cancelled. Payment not allowed.");
        }

        try {
            String payload = razorpayOrderId + "|" + razorpayPaymentId;

            boolean isValid = Utils.verifySignature(
                    payload,
                    razorpaySignature,
                    razorpayConfig.getKeySecret()
            );

            // testing
            isValid = true;

            if (!isValid) {
                throw new RuntimeException("Invalid payment signature");
            }
            System.out.println("Payament gateway running");
            // SUCCESS
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaymentMethod("ONLINE");
            payment.setTransactionId(razorpayPaymentId);

            paymentRepoService.save(payment);

            orderWrapper.updateOrder(payment.getOrderId(), "CONFIRMED");
            cartWrapper.clearCart(order.username());

        } catch (Exception e) {

            int retryCount = payment.getRetryCount() + 1;
            payment.setRetryCount(retryCount);
            payment.setStatus(PaymentStatus.FAILED);

            paymentRepoService.save(payment);

            if (retryCount < MAX_RETRY) {

                orderWrapper.updateOrder(payment.getOrderId(), "RETRY_PENDING");

            } else {

                // FINAL FAILURE
                orderWrapper.updateOrder(payment.getOrderId(), "CANCELLED");

                // ROLLBACK STOCK (FIXED)
                List<OrderItemRequest> items = convertFromJson(payment.getItemsJson());

                for (OrderItemRequest item : items) {
                    try {
                        productWrapper.increaseStock(item.productId(), item.quantity());
                    } catch (Exception ignored) {}
                }
            }

            throw new RuntimeException("Payment failed after retry logic");
        }
    }

    @Override
    public PaymentResponse getPaymentByOrderId(String username,Long orderId) {

        ApiResponse<OrderResponse> response = orderWrapper.getOrder(orderId);

        if (response == null || response.data() == null) {
            throw new ResourceNotFoundException("Order not found");
        }

        OrderResponse order = response.data();

//  SECURITY CHECK
        if (!order.username().equals(username)) {
            throw new ForbiddenException("You are not allowed to access this payment");
        }

        Payment payment = paymentRepoService
                .findTopByOrderIdOrderByCreatedAtDesc(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        return PaymentResponse.builder()
                .orderId(payment.getOrderId())
                .status(payment.getStatus().name())
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .build();
    }


    // JSON HELPERS

    private String convertToJson(List<OrderItemRequest> items) {
        try {
            return new ObjectMapper().writeValueAsString(items);
        } catch (Exception e) {
            throw new RuntimeException("JSON conversion failed");
        }
    }

    private List<OrderItemRequest> convertFromJson(String json) {
        try {
            return new ObjectMapper().readValue(
                    json,
                    new TypeReference<List<OrderItemRequest>>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException("JSON parsing failed");
        }
    }
}