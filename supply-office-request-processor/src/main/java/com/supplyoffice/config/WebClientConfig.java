package com.supplyoffice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Bean
    public WebClient webClientDeadlines(@Value("${deadlines.service.url}") String deadlinesServiceUrl) {
        return WebClient.builder().baseUrl(deadlinesServiceUrl).build();
    }

    @Bean
    public WebClient webClientReceiver(@Value("${receiver.service.url}") String receiverServiceUrl) {
        return WebClient.builder().baseUrl(receiverServiceUrl).build();
    }
}
