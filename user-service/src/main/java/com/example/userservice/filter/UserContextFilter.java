package com.example.userservice.filter;

import com.example.userservice.context.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String username = httpRequest.getHeader("X-User");

        if (username != null) {
            UserContext.setUser(username);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}