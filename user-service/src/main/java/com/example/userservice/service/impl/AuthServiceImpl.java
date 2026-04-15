package com.example.userservice.service.impl;

import com.example.userservice.dto.*;
import com.example.userservice.entity.RefreshToken;
import com.example.userservice.entity.User;
import com.example.userservice.enums.Role;
import com.example.userservice.exception.BadRequestException;
import com.example.userservice.exception.ResourceNotFoundException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepoService;
import com.example.userservice.security.JwtUtil;
import com.example.userservice.service.AuthService;
import com.example.userservice.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepoService userRepoService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public UserResponse register(RegisterRequest request) {

        if (userRepoService.existsByUsername(request.username())) {
            throw new BadRequestException("Username already exists");
        }

        if (userRepoService.existsByEmail(request.email())) {
            throw new BadRequestException("Email already exists");
        }

        User user = userMapper.toEntity(request);

        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setProvider("LOCAL");

        userRepoService.createUser(user);
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new BadRequestException("Invalid username or password");
        }

        User user = userRepoService.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new BadRequestException("User account is disabled");
        }

        String accessToken = jwtUtil.generateToken(user.getUsername(),user.getRole().name());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken.getToken(), "Bearer");
    }

    @Override
    public AuthResponse refreshToken(RefreshRequest refreshRequest) {

        String requestToken = refreshRequest.refreshToken();

        RefreshToken refreshToken = refreshTokenService.findByToken(requestToken)
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));

        refreshTokenService.verifyTokenExpiration(refreshToken);

        User user = refreshToken.getUser();

        refreshTokenService.deleteByUserId(user.getId());

        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        String accessToken = jwtUtil.generateToken(user.getUsername(),user.getRole().name());

        return new AuthResponse(accessToken, newRefreshToken.getToken(), "Bearer");
    }

    @Override
    public void logout(String refreshToken) {

        RefreshToken token = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new BadRequestException("Refresh token not found"));

        refreshTokenService.deleteByUserId(token.getUser().getId());
    }
}