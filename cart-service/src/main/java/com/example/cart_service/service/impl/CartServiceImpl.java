package com.example.cart_service.service.impl;

import com.example.cart_service.dto.ApiResponse;
import com.example.cart_service.dto.CartItemRequest;
import com.example.cart_service.dto.CartResponse;
import com.example.cart_service.dto.ProductResponse;
import com.example.cart_service.entity.Cart;
import com.example.cart_service.entity.CartItem;
import com.example.cart_service.exception.BadRequestException;
import com.example.cart_service.exception.ResourceNotFoundException;
import com.example.cart_service.external.ProductServiceWrapper;
import com.example.cart_service.mapper.CartMapper;
import com.example.cart_service.repository.CartRepoService;
import com.example.cart_service.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepoService cartRepoService;
    private final CartMapper cartMapper;
    private final ProductServiceWrapper productWrapper;
    @Override
    public CartResponse addToCart(String username, CartItemRequest request) {

        if (request.quantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }
        // Fetch product (Circuit Breaker protected)
        ApiResponse<ProductResponse> productResponse =
                productWrapper.getProduct(request.productId());

        if (productResponse == null || productResponse.data() == null) {
            throw new ResourceNotFoundException("Product not found");
        }

        ProductResponse product = productResponse.data();

        Cart cart = cartRepoService.findByUsername(username)
                .orElseGet(() -> Cart.builder()
                        .username(username)
                        .items(new ArrayList<>())
                        .build());

        Optional<CartItem> existingItem = cart.getItems()
                .stream()
                .filter(item -> item.getProductId().equals(request.productId()))
                .findFirst();

        int existingQty = existingItem.map(CartItem::getQuantity).orElse(0);

        // STOCK VALIDATION
        if (existingQty + request.quantity() > product.stockQuantity()) {
            throw new BadRequestException("Requested quantity exceeds available stock");
        }

        if (existingItem.isPresent()) {

            existingItem.get().setQuantity(existingQty + request.quantity());
        } else {
            CartItem newItem = CartItem.builder()
                    .productId(request.productId())
                    .quantity(request.quantity())
                    .cart(cart)
                    .build();

            cart.getItems().add(newItem);
        }

        Cart saved = cartRepoService.save(cart);

        return cartMapper.toResponse(saved);
    }

    @Override
    public CartResponse updateCart(String username, CartItemRequest request) {

        //  Quantity validation
        if (request.quantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        // Fetch product (Circuit Breaker protected)
        ApiResponse<ProductResponse> productResponse =
                productWrapper.getProduct(request.productId());

        if (productResponse == null || productResponse.data() == null) {
            throw new ResourceNotFoundException("Product not found");
        }

        ProductResponse product = productResponse.data();

        Cart cart = cartRepoService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem item = cart.getItems()
                .stream()
                .filter(i -> i.getProductId().equals(request.productId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));


        // STOCK VALIDATION
        if (request.quantity() > product.stockQuantity()) {
            throw new BadRequestException("Requested quantity exceeds available stock");
        }

        item.setQuantity(request.quantity());

        Cart saved = cartRepoService.save(cart);

        return cartMapper.toResponse(saved);
    }

    @Override
    public void removeFromCart(String username, Long productId) {

        Cart cart = cartRepoService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        boolean removed = cart.getItems().removeIf(
                item -> item.getProductId().equals(productId)
        );

        if (!removed) {
            throw new ResourceNotFoundException("Product not found in cart");
        }

        cartRepoService.save(cart);
    }

    @Override
    public CartResponse getCart(String username) {
        Cart cart = cartRepoService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return cartMapper.toResponse(cart);
    }

    @Override
    public void clearCart(String username) {

        Cart cart = cartRepoService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.getItems().clear();
        cartRepoService.save(cart);
    }
}
