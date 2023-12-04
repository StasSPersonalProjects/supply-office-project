package com.supplyoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RequestProcessorAppl {
    public static void main(String[] args) {
        SpringApplication.run(RequestProcessorAppl.class, args);
    }
}