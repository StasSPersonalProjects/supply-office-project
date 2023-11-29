package com.supplyoffice.controller;

import com.supplyoffice.dto.DeadlineDTO;
import com.supplyoffice.service.DeadlinesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/deadline")
public class DeadlinesController {

    @Autowired
    DeadlinesService service;

    Logger LOG = LoggerFactory.getLogger(DeadlinesController.class);

    @PutMapping
    ResponseEntity<String> setDeadline(@RequestBody DeadlineDTO deadlineDTO) {
        if(checkDate(deadlineDTO.getDeadline())) {
            LOG.debug("Received new update request for department {}.", deadlineDTO.getDepartmentName());
            String result = service.setDeadline(deadlineDTO);
            LOG.debug("New deadline scheduled successfully!");
            return ResponseEntity.ok().body(result);
        } else {
            throw new IllegalArgumentException("The provided date is in the past.");
        }
    }

    private boolean checkDate(LocalDateTime deadline) {
        LocalDateTime currentDate = LocalDateTime.now();
        return !deadline.isBefore(currentDate);
    }
}
