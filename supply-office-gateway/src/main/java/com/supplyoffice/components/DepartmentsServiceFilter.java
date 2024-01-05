package com.supplyoffice.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepartmentsServiceFilter extends AbstractGatewayFilterFactory<DepartmentsServiceFilter.Config> {

    @Autowired
    private SecurityService securityService;
    static Logger LOG = LoggerFactory.getLogger(DepartmentsServiceFilter.class);

    public DepartmentsServiceFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(DepartmentsServiceFilter.Config config) {
        List<String> roles = List.of("MANAGER");
        return ((exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                LOG.debug("No auth header was found or the value doesn't start with 'Bearer'.");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            String jwtToken = authHeader.substring(7);
            LOG.debug("Extracted token {}", jwtToken);
            return securityService.isTokenValid(jwtToken)
                    .flatMap(valid -> {
                        if (valid) {
                            LOG.debug("Token validated successfully.");
                            return securityService.validateRole(exchange, chain, roles);
                        } else {
                            LOG.debug("Token not valid.");
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }
                    });
        });
    }

    public static class Config {

    }
}