package com.supplyoffice.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IOException.class)
    public void handleIOException (IOException ex) {
        LOG.debug("IO Exception: {}", ex.getMessage());
    }

    @ExceptionHandler(MessagingException.class)
    public void handleMessagingException (MessagingException ex) {
        LOG.debug("Messaging Exception: {}", ex.getMessage());
    }

    @ExceptionHandler(jakarta.mail.MessagingException.class)
    public void handleJakartaMailMessagingException (jakarta.mail.MessagingException ex) {
        LOG.debug(ex.getMessage());
    }
}
