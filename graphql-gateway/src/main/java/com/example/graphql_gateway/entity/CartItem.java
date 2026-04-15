package com.example.graphql_gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItem {
    private Long productId;
    private Integer quantity;
}