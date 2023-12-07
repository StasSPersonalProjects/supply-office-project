package com.supplyoffice.controller;

import com.supplyoffice.dto.DeadlineDTO;
import com.supplyoffice.service.DeadlinesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/status")
    ResponseEntity<String> setActiveStatus(@RequestParam String name, @RequestParam int status) {
        LOG.debug("Received new status update to {} for department {}.", status, name);
        String result = service.setActiveStatus(name, status);
        LOG.debug("New active status updated successfully!");
        return ResponseEntity.ok().body(result);
    }

    private boolean checkDate(LocalDateTime deadline) {
        LocalDateTime currentDate = LocalDateTime.now();
        return !deadline.isBefore(currentDate);
    }
}
