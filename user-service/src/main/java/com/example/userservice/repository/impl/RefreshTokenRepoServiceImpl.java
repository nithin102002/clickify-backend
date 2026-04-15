package com.example.userservice.repository.impl;

import com.example.userservice.entity.RefreshToken;
import com.example.userservice.repository.RefreshTokenRepoService;
import com.example.userservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenRepoServiceImpl implements RefreshTokenRepoService {

    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public void delete(RefreshToken refreshToken) {

        refreshTokenRepository.delete(refreshToken);

    }

    @Override
    public void deleteByUserId(Long userId) {

        refreshTokenRepository.deleteByUserId(userId);
    }
}
