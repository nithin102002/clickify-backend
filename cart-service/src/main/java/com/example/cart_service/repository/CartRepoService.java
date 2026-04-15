package com.example.cart_service.repository;

import com.example.cart_service.entity.Cart;

import java.util.Optional;

public interface CartRepoService {

    Cart save(Cart cart);

    Optional<Cart> findByUsername(String username);

    void delete(Cart cart);
}
