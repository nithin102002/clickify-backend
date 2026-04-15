package com.example.graphql_gateway.entity;

import com.example.graphql_gateway.dto.ReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private List<ReviewResponse> reviews;
}
