package com.supplyoffice.controller;

import com.supplyoffice.dto.RequestDTO;
import com.supplyoffice.service.RequestReceivingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/request")
@Validated
public class RequestReceivingController {

    @Autowired
    RequestReceivingService service;

    static Logger LOG = LoggerFactory.getLogger(RequestReceivingController.class);

    @PostMapping
    ResponseEntity<String> addRequest(@RequestBody @Valid RequestDTO requestDTO)
            throws MethodArgumentNotValidException {
        LOG.debug("Received new request for department {}.", requestDTO.getDepartmentName());
        String response = service.addRequest(requestDTO);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    ResponseEntity<String> updateRequest() {
        // TODO
        return null;
    }

    @DeleteMapping
    ResponseEntity<String> deleteRequest() {
        // TODO
        return null;
    }
}
