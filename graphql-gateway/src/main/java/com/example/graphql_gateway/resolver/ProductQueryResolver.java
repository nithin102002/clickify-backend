package com.example.graphql_gateway.resolver;


import com.example.graphql_gateway.dto.ApiResponse;
import com.example.graphql_gateway.dto.ProductResponse;
import com.example.graphql_gateway.dto.ReviewResponse;
import com.example.graphql_gateway.entity.Product;
import com.example.graphql_gateway.exception.ResourceNotFoundException;
import com.example.graphql_gateway.external.ProductServiceWrapper;
import com.example.graphql_gateway.external.ReviewServiceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductQueryResolver {

    private final ProductServiceWrapper productWrapper;
    private final ReviewServiceWrapper reviewWrapper;

    @QueryMapping
    public Product product(@Argument Long id) {

        // Call Product Service
        ApiResponse<ProductResponse> productResponse =
                productWrapper.getProduct(id);

        if (productResponse == null || productResponse.data() == null) {
            throw new ResourceNotFoundException("Product not found");
        }

        ProductResponse product = productResponse.data();

        //  Call Review Service
        ApiResponse<List<ReviewResponse>> reviewResponse =
                reviewWrapper.getReviews(id);

        List<ReviewResponse> reviews = (reviewResponse != null && reviewResponse.data() != null)
                ? reviewResponse.data()
                : List.of();

        // Combine
        return new Product(
                product.id(),
                product.name(),
                product.price(),
                reviews
        );
    }
}
