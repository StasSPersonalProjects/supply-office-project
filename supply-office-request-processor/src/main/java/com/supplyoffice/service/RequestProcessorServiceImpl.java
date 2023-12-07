package com.supplyoffice.service;

import com.supplyoffice.component.ScheduledFetchAndSend;
import com.supplyoffice.dto.DeadlineDTO;
import com.supplyoffice.entities.Deadline;
import com.supplyoffice.repositories.DeadlinesRepository;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RequestProcessorServiceImpl implements RequestProcessorService {

    @Autowired
    private DeadlinesRepository deadlinesRepository;
    @Autowired
    private ScheduledFetchAndSend scheduledFetchAndSend;
    private Map<String, LocalDateTime> departmentDeadlines;

    static Logger LOG = LoggerFactory.getLogger(RequestProcessorServiceImpl.class);

    @Override
    public void updateDepartmentDeadlines(DeadlineDTO deadlineDTO) {
        createDepartmentDeadlines();
        LOG.debug("Updated deadlines local data structure. Current values:");
        for (Map.Entry<String, LocalDateTime> entry : departmentDeadlines.entrySet()) {
            LOG.debug("{} : {}", entry.getKey(), entry.getValue());
        }
    }

    @Scheduled(fixedRate = 60000, initialDelay = 60000)
    @Transactional
    public void checkDeadlines() throws IOException, MessagingException {
        Set<String> departmentsToRemove = new HashSet<>();
        LOG.debug("In scheduled task, current deadline values: {}", departmentDeadlines);
        if (!departmentDeadlines.isEmpty()) {
            for (Map.Entry<String, LocalDateTime> entry : departmentDeadlines.entrySet()) {
                if (entry.getValue() != null && entry.getValue().isBefore(LocalDateTime.now())) {
                    LOG.debug("Found a department who's deadline passed: {}.", entry.getKey());
                    scheduledFetchAndSend.fetchDataAndSendEmail(entry.getKey());
                    deadlinesRepository.setToFalseByName(entry.getKey());
                    departmentsToRemove.add(entry.getKey());
                }
            }
            for (String department : departmentsToRemove) {
                departmentDeadlines.remove(department);
                LOG.debug("Removed department {} from deadlines data structure.", department);
            }
            return;
        }
        LOG.debug("Deadlines data structure is empty.");
    }
    @PostConstruct
    public void initialCreateDepartmentDeadlines() {
        departmentDeadlines = new HashMap<>();
        createDepartmentDeadlines();
    }

    public void createDepartmentDeadlines() {
        try {
            List<Deadline> deadlines = deadlinesRepository.findAll();
            Map<String, Optional<LocalDateTime>> departmentDeadlinesOptional = deadlines.stream()
                    .collect(Collectors.toMap(Deadline::getDepartmentName, d -> Optional.ofNullable(d.getDeadline())));
            for (Map.Entry<String, Optional<LocalDateTime>> entry : departmentDeadlinesOptional.entrySet()) {
                if (entry.getValue().isPresent()) {
                    departmentDeadlines.put(entry.getKey(), entry.getValue().get());
                    LOG.debug("Created a registration for department {} and its deadline: {}", entry.getKey(), entry.getValue().get());
                } else {
                    LOG.debug("Created a registration for department {} and its deadline is not defined.", entry.getKey());
                }
            }
            LOG.debug("Existing deadlines: {}", departmentDeadlines);
        } catch (Exception e) {
            LOG.error("Error during departments deadline data structure creation: {}", e.getMessage());
        }
    }

}
