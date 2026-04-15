package com.example.userservice.repository;

import com.example.userservice.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepoService {
    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);

    void delete(RefreshToken refreshToken);

    void deleteByUserId(Long userId);
}
