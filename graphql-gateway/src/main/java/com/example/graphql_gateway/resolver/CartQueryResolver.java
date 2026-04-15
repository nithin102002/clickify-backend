package com.example.graphql_gateway.resolver;

import com.example.graphql_gateway.dto.ApiResponse;
import com.example.graphql_gateway.dto.CartResponse;
import com.example.graphql_gateway.entity.Cart;
import com.example.graphql_gateway.entity.CartItem;
import com.example.graphql_gateway.exception.ResourceNotFoundException;
import com.example.graphql_gateway.external.CartServiceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartQueryResolver {

    private final CartServiceWrapper cartWrapper;

    @QueryMapping
    public Cart cart(@Argument String username) {

        ApiResponse<CartResponse> response =
                cartWrapper.getCart(username);

        if (response == null || response.data() == null) {
            throw new ResourceNotFoundException("Product not found");
        }
        CartResponse cart = response.data();

        List<CartItem> items = cart.items()
                .stream()
                .map(i -> new CartItem(
                        i.productId(),
                        i.quantity()
                ))
                .toList();

        return new Cart(cart.username(), items);
    }
}
