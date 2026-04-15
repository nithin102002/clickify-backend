package com.example.api_gateway.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String method = exchange.getRequest().getMethod().name();
        String path = exchange.getRequest().getURI().toString();

        // BEFORE REQUEST
        log.info("➡️ Incoming Request: {} {}", method, path);

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {

                    // AFTER RESPONSE
                    int status = exchange.getResponse().getStatusCode() != null
                            ? exchange.getResponse().getStatusCode().value()
                            : 0;

                    log.info("⬅️ Response: {} {} → {}", method, path, status);
                }));
    }
}
