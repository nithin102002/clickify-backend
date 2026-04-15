package com.example.graphql_gateway.resolver;

import com.example.graphql_gateway.dto.ApiResponse;
import com.example.graphql_gateway.dto.PaymentResponse;
import com.example.graphql_gateway.entity.Order;
import com.example.graphql_gateway.entity.Payment;
import com.example.graphql_gateway.exception.ForbiddenException;
import com.example.graphql_gateway.external.PaymentServiceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class OrderFieldResolver {

    private final PaymentServiceWrapper paymentWrapper;

    @SchemaMapping(typeName = "Order", field = "payment")
    public Payment getPayment(Order order,@ContextValue("X-User") String username) {

        if (username == null || username.isBlank()) {
            throw new ForbiddenException("User not authenticated");
        }
        System.out.println("USERNAME IN GRAPHQL: " + username);

        ApiResponse<PaymentResponse> response =
                paymentWrapper.getPayment(username,order.getId());

        if (response == null || response.data() == null) {
            return null;
        }

        PaymentResponse payment = response.data();

        return new Payment(
                payment.status(),
                payment.amount()
        );
    }
}