package com.supplyoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.supplyoffice")
public class ApiGatewayAppl {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayAppl.class, args);
    }
}
