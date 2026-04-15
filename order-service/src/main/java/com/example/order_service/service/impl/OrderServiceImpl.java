package com.example.order_service.service.impl;

import com.example.order_service.dto.*;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.enums.OrderStatus;
import com.example.order_service.enums.ProductStatus;
import com.example.order_service.exception.BadRequestException;
import com.example.order_service.exception.ResourceNotFoundException;
import com.example.order_service.exception.ServiceUnavailableException;
import com.example.order_service.external.CartServiceWrapper;
import com.example.order_service.external.PaymentServiceWrapper;
import com.example.order_service.external.ProductServiceWrapper;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.repository.OrderRepoService;
import com.example.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepoService orderRepoService;
    private final OrderMapper orderMapper;
    private final ProductServiceWrapper productWrapper;
    private final PaymentServiceWrapper paymentWrapper;
    private final CartServiceWrapper cartWrapper;
    @Transactional
    @Override
    public OrderResponse createOrder(String username, OrderRequest request) {

        //   FETCH CART
        ApiResponse<CartResponse> cartResponse =
                cartWrapper.getCart(username);

        if (cartResponse == null || cartResponse.data() == null
                || cartResponse.data().items().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }
        List<CartItemResponse> cartItems = cartResponse.data().items();
        // CONVERT TO ORDER ITEMS
        List<OrderItemRequest> items = cartItems.stream()
                .map(i -> new OrderItemRequest(i.productId(), i.quantity()))
                .toList();

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;



        for (OrderItemRequest itemRequest : items) {

            //  Call Product Service
            ApiResponse<ProductResponse> response = productWrapper.getProduct(itemRequest.productId());

            if (response == null || response.data() == null) {
                throw new ResourceNotFoundException("Product not found");
            }
            ProductResponse product = response.data();



            //  CHECK PRODUCT STATUS
            if (product.productStatus() == ProductStatus.INACTIVE) {
                throw new BadRequestException("Product is inactive");
            }

            //  CHECK STOCK
            if (product.stockQuantity() < itemRequest.quantity()) {
                System.out.println(product.stockQuantity() + "-" + itemRequest.quantity());
                throw new BadRequestException("Insufficient stock for " + product.name());
            }


            BigDecimal subtotal =
                    product.price().multiply(BigDecimal.valueOf(itemRequest.quantity()));

            totalPrice = totalPrice.add(subtotal);

            OrderItem orderItem = OrderItem.builder()
                    .productId(product.id())
                    .productName(product.name())
                    .price(product.price())
                    .quantity(itemRequest.quantity())
                    .subtotal(subtotal)
                    .build();

            orderItems.add(orderItem);
        }


        Order order = Order.builder()
                .username(username)
                .totalPrice(totalPrice)
                .status(OrderStatus.PENDING_PAYMENT)
                .build();

        orderItems.forEach(item -> item.setOrder(order));
        order.setItems(orderItems);

        Order savedOrder=orderRepoService.createOrder(order);

        try {
            // Reduce stock
            for (OrderItemRequest itemRequest : items) {
                productWrapper.reduceStock(itemRequest.productId(), itemRequest.quantity());

            }
            // HANDLE PAYMENT MODE
            if ("COD".equalsIgnoreCase(request.paymentMode())) {

                // COD → Direct confirm
                savedOrder.setStatus(OrderStatus.CONFIRMED);
                orderRepoService.createOrder(savedOrder);

                //  CLEAR CART ONLY ON SUCCESS
                cartWrapper.clearCart(username);

            } else {


                // ONLINE PAYMENT
                ApiResponse<PaymentResponse> paymentApiResponse =
                        paymentWrapper.processPayment(
                                new PaymentRequest(savedOrder.getId(), totalPrice, items)
                        );

                if (paymentApiResponse == null || paymentApiResponse.data() == null) {
                    throw new RuntimeException("Payment creation failed");
                }

            }


        }  catch (ServiceUnavailableException ex) {

            //  ROLLBACK STOCK
            for (OrderItemRequest item : items) {
                try {
                    productWrapper.increaseStock(item.productId(), item.quantity());
                } catch (Exception ignored) {}
            }

            //  CANCEL ORDER
            savedOrder.setStatus(OrderStatus.CANCELLED);
            orderRepoService.createOrder(savedOrder);

            //  PROPAGATE ORIGINAL ERROR (503)
            throw ex;
        }

        catch (Exception ex) {



            // ROLLBACK STOCK
            for (OrderItemRequest item : items) {
                try {
                    productWrapper.increaseStock(item.productId(), item.quantity());
                } catch (Exception ignored) {}
            }


            //  FAILURE -> CANCEL ORDER
            savedOrder.setStatus(OrderStatus.CANCELLED);
            orderRepoService.createOrder(savedOrder);

            throw new BadRequestException("Order failed. Rolled back."+ ex.getMessage());
        }

        return orderMapper.toResponse(savedOrder);


    }


    @Override
    public PageResponse<OrderResponse> getOrders(
            String username,
            int page,
            int size,
            String status,
            Long orderId
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Order> orderPage;

        // SEARCH
        if (orderId != null) {
            Order order = orderRepoService.findByIdAndUsername(orderId, username)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

            orderPage = new PageImpl<>(List.of(order));
        }

        // FILTER
        else if (status != null && !status.isBlank()) {

            OrderStatus orderStatus;
            try {
                orderStatus = OrderStatus.valueOf(status.toUpperCase());
            } catch (Exception ex) {
                throw new BadRequestException("Invalid order status");
            }

            orderPage = orderRepoService
                    .findByUsernameAndStatus(username, orderStatus, pageable);
        }

        // DEFAULT
        else {
            orderPage = orderRepoService.findByUsername(username, pageable);
        }

        //  Convert to DTO
        List<OrderResponse> content = orderPage
                .getContent()
                .stream()
                .map(orderMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.isLast()
        );
    }
    @Override
    public OrderResponse getOrderById(Long id) {

        Order order = orderRepoService.getOrderById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));


        return orderMapper.toResponse(order);
    }



    @Transactional
    @Override
    public void updateStatus(Long id, String status) {
        Order order = orderRepoService.getOrderById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        //  LOCK FINAL STATES
        if (order.getStatus() == OrderStatus.CONFIRMED ||
                order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Order is locked");
        }

        OrderStatus newStatus = OrderStatus.valueOf(status);

        // ROLLBACK STOCK IF CANCELLED
        if (newStatus == OrderStatus.CANCELLED) {
            for (OrderItem item : order.getItems()) {
                try {
                    // Revert the stock for each product in the order
                    productWrapper.increaseStock(item.getProductId(), item.getQuantity());
                } catch (Exception ex) {
                    // Log the error but keep going to ensure other items are rolled back
                    System.err.println("Failed to rollback stock for product ID: " + item.getProductId());
                }
            }
        }

        order.setStatus(newStatus);
        orderRepoService.createOrder(order);
    }

}