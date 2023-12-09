package com.supplyoffice.controller;

import com.supplyoffice.dto.DeadlineDTO;
import com.supplyoffice.entities.Deadline;
import com.supplyoffice.service.DeadlinesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/deadline")
public class DeadlinesController {

    @Autowired
    DeadlinesService service;

    Logger LOG = LoggerFactory.getLogger(DeadlinesController.class);

    @PostMapping(value = "/department")
    void addDepartment(@RequestBody DeadlineDTO deadlineDTO) {
        if (deadlineDTO != null) {
            LOG.debug("Received a department {} to add to deadlines table in DB.", deadlineDTO.getDepartmentName());
            service.addDepartment(deadlineDTO);
        } else {
            LOG.debug("Received empty data object.");
        }
    }

    @PutMapping(value = "/new")
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

    @PutMapping(value = "/status")
    ResponseEntity<String> setActiveStatus(@RequestParam String name, @RequestParam int status) {
        LOG.debug("Received new status update to {} for department {}.", status, name);
        String result = service.setActiveStatus(name, status);
        LOG.debug("New active status updated successfully!");
        return ResponseEntity.ok().body(result);
    }

    @GetMapping
    LocalDateTime getDeadline(@RequestParam String name) {
        LOG.debug("Received request for current deadline for department {}.", name);
        return service.getDeadline(name);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<DeadlineDTO>> getAllDeadlines() {
        LOG.debug("Received request to fetch all deadlines.");
        List<Deadline> fetchedDeadline = service.getAllDeadlines();
        if (!fetchedDeadline.isEmpty()) {
            LOG.debug("Deadlines fetched.");
            List<DeadlineDTO> deadlineDTOList = fetchedDeadline.stream().map(DeadlineDTO::of).toList();
            return new ResponseEntity<>(deadlineDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    private boolean checkDate(LocalDateTime deadline) {
        LocalDateTime currentDate = LocalDateTime.now();
        return !deadline.isBefore(currentDate);
    }
}
