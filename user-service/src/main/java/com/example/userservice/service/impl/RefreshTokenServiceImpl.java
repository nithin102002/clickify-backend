package com.example.userservice.service.impl;

import com.example.userservice.entity.RefreshToken;
import com.example.userservice.entity.User;
import com.example.userservice.exception.BadRequestException;
import com.example.userservice.repository.RefreshTokenRepoService;
import com.example.userservice.repository.impl.RefreshTokenRepoServiceImpl;
import com.example.userservice.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {


    private final RefreshTokenRepoService refreshTokenRepository;

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidity;

    private final SecureRandom secureRandom = new SecureRandom();

    //  Create Refresh Token
    @Override
    public RefreshToken createRefreshToken(User user) {

        String tokenValue = generateSecureToken();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(tokenValue)
                .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenValidity / 1000))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    // Find by Token
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    // Verify Expiration
    @Override
    public RefreshToken verifyTokenExpiration(RefreshToken refreshToken) {

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new BadRequestException("Refresh token expired. Please login again.");
        }

        return refreshToken;
    }

    //  Delete by User ID (Logout / Refresh Flow)
    @Override
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    //  Generate Secure Random Token
    private String generateSecureToken() {
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
