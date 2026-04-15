package com.example.graphql_gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Cart {
    private String username;
    private List<CartItem> items;
}