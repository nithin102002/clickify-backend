package com.example.payment_service.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.example.payment_service.entity.Payment;

import java.util.Optional;

public interface PaymentRepoService {
    Payment save(Payment payment);

    Optional<Payment> findByTransactionId(String razorpayOrderId);


    Optional<Payment> findTopByOrderIdOrderByCreatedAtDesc(Long orderId);
}
