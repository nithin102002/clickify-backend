package com.example.payment_service.repository.impl;

import com.example.payment_service.entity.Payment;
import com.example.payment_service.repository.PaymentRepoService;
import com.example.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentRepoServiceImpl implements PaymentRepoService {

    private final PaymentRepository paymentRepository;
    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> findByTransactionId(String razorpayOrderId) {
        return paymentRepository.findByTransactionId(razorpayOrderId);
    }

    @Override
    public Optional<Payment> findTopByOrderIdOrderByCreatedAtDesc(Long orderId) {
        return paymentRepository.findTopByOrderIdOrderByCreatedAtDesc(orderId);
    }
}
