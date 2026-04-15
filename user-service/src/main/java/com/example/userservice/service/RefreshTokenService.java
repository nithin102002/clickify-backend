package com.example.userservice.service;

import com.example.userservice.entity.RefreshToken;
import com.example.userservice.entity.User;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);
    Optional<RefreshToken> findByToken(String token);
    RefreshToken verifyTokenExpiration(RefreshToken refreshToken);
    void deleteByUserId(Long userId);
}
