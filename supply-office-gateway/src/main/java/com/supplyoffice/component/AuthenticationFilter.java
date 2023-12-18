package com.supplyoffice.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    SecurityServiceClient securityServiceClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String accessToken = headers.getFirst("Authorization");
        String refreshToken = headers.getFirst("Refresh");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            String jwtToken = accessToken.substring(7);
            if (securityServiceClient.isTokenValid(jwtToken)) {
                return chain.filter(exchange);
            } else {
                String jwtRefreshToken = refreshToken.substring(7);
                String newJwtToken = securityServiceClient.refreshJwtToken(jwtRefreshToken).getAccessToken();
                if (newJwtToken != null) {
                    exchange.getRequest().mutate()
                            .header("Authorization", "Bearer " + newJwtToken);
                    return chain.filter(exchange);
                }
            }
        }
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
