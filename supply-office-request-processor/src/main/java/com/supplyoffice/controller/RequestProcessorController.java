package com.supplyoffice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestProcessorController {

    Logger LOG = LoggerFactory.getLogger(RequestProcessorController.class);

    @PostMapping("/process-request")
    public ResponseEntity<String> processNotification(@RequestBody String requestId) {
        // TODO
        LOG.debug("Received a notification that a request with id {} was added to DB.", Long.valueOf(requestId));
        return null;
    }

}
