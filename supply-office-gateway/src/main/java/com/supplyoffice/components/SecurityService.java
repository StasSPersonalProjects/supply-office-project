package com.supplyoffice.components;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class SecurityService {

    @Autowired
    WebClient webClientSecurityService;

    @Value("${application.config.security-service.url}")
    private String securityServiceUrl;

    static Logger LOG = LoggerFactory.getLogger(SecurityService.class);

    @NotNull
    public Mono<Void> validateRole(ServerWebExchange exchange, GatewayFilterChain chain, List<String> requiredRoles) {
        String jwtToken = exchange.getRequest().getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION).substring(7);
        return this.getRole(jwtToken).flatMap(role -> {
            if (compareRoles(requiredRoles, role) || role.equals("ADMIN")) {
                LOG.debug("Request authorized!");
                return chain.filter(exchange);
            } else {
                LOG.debug("Request unauthorized!");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        });
    }

    private boolean compareRoles(List<String> requiredRoles, String role) {
        return requiredRoles.contains(role);
    }

    public Mono<Boolean> isTokenValid(String jwtToken) {
        LOG.debug("Validating token {}", jwtToken);
        LOG.debug("uri: " + securityServiceUrl + "/validate/" + jwtToken);
        return webClientSecurityService
                .get()
                .uri("/validate/{token}", jwtToken)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    public Mono<String> getRole(String jwtToken) {
        LOG.debug("Sending request to security service to extract the role from token.");
        LOG.debug("uri: " + securityServiceUrl + "/role?token=" + jwtToken);
        return webClientSecurityService
                .get()
                .uri(builder ->
                        builder
                                .path("/role")
                                .queryParam("token", jwtToken)
                                .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
