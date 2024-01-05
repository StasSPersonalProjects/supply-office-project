package com.supplyoffice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
        String message = "";
        if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            message = "Unauthorized";
        }
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }
}
