package com.example.graphql_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.server.WebGraphQlInterceptor;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class GraphQLContextConfig {

    @Bean
    public WebGraphQlInterceptor webGraphQlInterceptor() {
        return (request, chain) -> {

            String username = request.getHeaders().getFirst("X-User");

            // Create safe context map
            Map<String, Object> contextMap = new HashMap<>();

            // Only add if present (IMPORTANT)
            if (username != null && !username.isBlank()) {
                contextMap.put("X-User", username);
            }

            request.configureExecutionInput((executionInput, builder) ->
                    builder.graphQLContext(contextMap).build()
            );

            return chain.next(request);
        };
    }
}