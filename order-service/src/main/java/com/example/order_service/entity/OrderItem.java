package com.example.order_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private BigDecimal subtotal;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
