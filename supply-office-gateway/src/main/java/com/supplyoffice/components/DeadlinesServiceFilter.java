package com.supplyoffice.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeadlinesServiceFilter extends AbstractGatewayFilterFactory<DeadlinesServiceFilter.Config> {

    @Autowired
    private SecurityService securityService;
    static Logger LOG = LoggerFactory.getLogger(DeadlinesServiceFilter.class);

    public DeadlinesServiceFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(DeadlinesServiceFilter.Config config) {
        List<String> roles = List.of("MANAGER", "USER");
        return ((exchange, chain) -> securityService.validateRole(exchange, chain, roles));
    }

    public static class Config {

    }
}
