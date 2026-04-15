package com.example.graphql_gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Order {
    private Long id;
    private String status;
    private BigDecimal totalPrice;
}
