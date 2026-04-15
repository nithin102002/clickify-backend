package com.example.api_gateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter {

    private final JwtUtil jwtUtil;
    private final SecurityProperties securityProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().toString();
        String method = exchange.getRequest().getMethod().name();

        // Skip AUTH endpoints (LOGIN / REGISTER / OAUTH)
        if (path.contains("/auth") || path.contains("/oauth2")) {
            return chain.filter(exchange);
        }

        //  PUBLIC APIs (NO TOKEN REQUIRED)
        // Products GET -> Public
        if (path.startsWith("/api/v1/products") && method.equalsIgnoreCase("GET")) {
            return chain.filter(exchange);
        }

        // Get Authorization header
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Extract token
        String token = authHeader.substring(7);

        //  Validate token
        if (!jwtUtil.isTokenValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Extract user details
        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);

        //  Dynamic Authorization
        if (securityProperties.getRules() != null) {

            for (SecurityRule rule : securityProperties.getRules()) {

                boolean pathMatches =
                        path.equals(rule.path()) ||
                                path.startsWith(rule.path() + "/");

                boolean methodMatches =
                        method.equalsIgnoreCase(rule.method());

                if (pathMatches && methodMatches) {

                    if (role == null || !hasPermission(role, rule.role())) {
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }
                }
            }
        }

        // Add headers for downstream services
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(r -> r
                        .header("X-User", username)
                        .header("X-Role", role)
                )
                .build();

        // Forward request
        return chain.filter(mutatedExchange);
    }

    // ROLE HIERARCHY
    private boolean hasPermission(String userRole, String requiredRole) {

        // ADMIN -> full access
        if ("ROLE_ADMIN".equalsIgnoreCase(userRole)) {
            return true;
        }

        // USER -> exact match only
        return userRole.equalsIgnoreCase(requiredRole);
    }
}