package com.supplyoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServiceAppl {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServiceAppl.class, args);
    }
}
