package com.example.payment_service.config;

import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RazorpayClientConfig {

    private final RazorpayConfig razorpayConfig;

    @Bean
    public RazorpayClient razorpayClient() throws Exception {
        return new RazorpayClient(
                razorpayConfig.getKeyId(),
                razorpayConfig.getKeySecret()
        );
    }
}
