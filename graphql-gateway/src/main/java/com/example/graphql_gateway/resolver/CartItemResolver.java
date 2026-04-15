package com.example.graphql_gateway.resolver;

import com.example.graphql_gateway.dto.ApiResponse;
import com.example.graphql_gateway.dto.ProductResponse;
import com.example.graphql_gateway.entity.CartItem;
import com.example.graphql_gateway.entity.Product;
import com.example.graphql_gateway.external.ProductServiceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartItemResolver {

    private final ProductServiceWrapper productWrapper;

    @SchemaMapping(typeName = "CartItem", field = "product")
    public Product getProduct(CartItem cartItem) {

        ApiResponse<ProductResponse> response =
                productWrapper.getProduct(cartItem.getProductId());

        if (response == null || response.data() == null) {
            return null;   // VERY IMPORTANT
        }


        ProductResponse product = response.data();

        return new Product(
                product.id(),
                product.name(),
                product.price(),
                List.of()
        );
    }
}
