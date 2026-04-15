package com.example.graphql_gateway.resolver;

import com.example.graphql_gateway.dto.ApiResponse;
import com.example.graphql_gateway.dto.OrderResponse;
import com.example.graphql_gateway.entity.Order;
import com.example.graphql_gateway.exception.ResourceNotFoundException;
import com.example.graphql_gateway.external.OrderServiceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class OrderQueryResolver {

    private final OrderServiceWrapper orderWrapper;

    @QueryMapping
    public Order order(@Argument Long id) {

        ApiResponse<OrderResponse> response =
                orderWrapper.getOrder(id);

        if (response == null || response.data() == null) {
            throw new ResourceNotFoundException("Product not found");
        }
        OrderResponse order = response.data();

        return new Order(
                order.id(),
                order.status(),
                order.totalPrice()
        );
    }
}
