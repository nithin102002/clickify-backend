package com.example.userservice.security.oauth;

import com.example.userservice.config.PasswordConfig;
import com.example.userservice.entity.User;
import com.example.userservice.enums.Role;
import com.example.userservice.repository.UserRepoService;
import com.example.userservice.security.JwtUtil;
import com.example.userservice.service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepoService userRepoService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService; // ✅ NEW

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String firstName = name != null ? name.split(" ")[0] : "User";
        String lastName = name != null && name.contains(" ") ? name.substring(name.indexOf(" ") + 1) : "User";

        Optional<User> optionalUser = userRepoService.findByUsername(email);
        User user;


        // ✅ Create user if not exists
        if (optionalUser.isEmpty()) {

            user = User.builder()
                    .email(email)
                    .username(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .phoneNumber(null)
                    .password(passwordEncoder.encode(UUID.randomUUID().toString())) // ✅ secure
                    .role(Role.ROLE_USER)
                    .enabled(true)
                    .provider("GOOGLE")
                    .accountNonLocked(true)
                    .build();

            userRepoService.createUser(user);

        } else {
            user = optionalUser.get();
        }

        // ✅ Generate tokens
        String accessToken = jwtUtil.generateToken(user.getUsername(),user.getRole().name());
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();

        // ✅ Redirect with tokens
        response.sendRedirect(
                "http://localhost:3000/oauth-success?accessToken=" + accessToken +
                        "&refreshToken=" + refreshToken
        );
    }
}