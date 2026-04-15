package com.example.cart_service.repository.impl;

import com.example.cart_service.entity.Cart;
import com.example.cart_service.repository.CartRepoService;
import com.example.cart_service.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartRepoServiceImpl implements CartRepoService {

    private final CartRepository cartRepository;
    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Optional<Cart> findByUsername(String username) {
        return cartRepository.findByUsername(username);
    }

    @Override
    public void delete(Cart cart) {

        cartRepository.delete(cart);
    }
}
