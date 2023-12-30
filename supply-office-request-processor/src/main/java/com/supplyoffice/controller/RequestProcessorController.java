package com.supplyoffice.controller;

import com.supplyoffice.dto.DeadlineDTO;
import com.supplyoffice.service.RequestProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/processor")
public class RequestProcessorController {

    @Autowired
    RequestProcessorService service;

    Logger LOG = LoggerFactory.getLogger(RequestProcessorController.class);

    @PutMapping("/update-deadline")
    public void processNotification(@RequestBody DeadlineDTO deadlineDTO) {
        LOG.debug("Received a notification that a deadline for department {} was updated.", deadlineDTO.getDepartmentName());
        service.updateDepartmentDeadlines(deadlineDTO);
    }

}
