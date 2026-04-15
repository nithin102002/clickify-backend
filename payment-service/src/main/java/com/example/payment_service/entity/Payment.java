package com.example.payment_service.entity;

import com.example.payment_service.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private String transactionId;

    private String paymentMethod;

    private int retryCount;

    private LocalDateTime createdAt;


    @Column(columnDefinition = "TEXT")
    private String itemsJson;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
