package com.supplyoffice.service;

import com.supplyoffice.component.ScheduledFetchAndSend;
import com.supplyoffice.dto.DeadlineDTO;
import com.supplyoffice.entities.Deadline;
import com.supplyoffice.repositories.DeadlinesRepository;
import com.supplyoffice.repositories.SupplyRequestsRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestProcessorServiceImpl implements RequestProcessorService {

    @Autowired
    SupplyRequestsRepository supplyRequestsRepository;
    @Autowired
    DeadlinesRepository deadlinesRepository;
    @Autowired
    ScheduledFetchAndSend scheduledFetchAndSend;

    Map<String, Optional<LocalDateTime>> departmentDeadlinesOptional;
    Map<String, LocalDateTime> departmentDeadlines;

    static Logger LOG = LoggerFactory.getLogger(RequestProcessorServiceImpl.class);


    @Override
    public void updateDepartmentDeadlines(DeadlineDTO deadlineDTO) {
        departmentDeadlines.put(deadlineDTO.getDepartmentName(), deadlineDTO.getDeadline());
        LOG.debug("Updated deadlines local data structure. Current values:");
        for (Map.Entry<String, LocalDateTime> entry : departmentDeadlines.entrySet()) {
            LOG.debug("{} : {}", entry.getKey(), entry.getValue());
        }
    }

    @Scheduled(fixedRate = 30000)
    public void checkDeadlines() {
        for (Map.Entry<String, LocalDateTime> entry : departmentDeadlines.entrySet()) {
            if (entry.getValue().isAfter(LocalDateTime.now())) {
                scheduledFetchAndSend.fetchDataAndSendEmail(entry.getKey());
                deadlinesRepository.setToFalseByName(entry.getKey());
            }
        }
    }

    @PostConstruct
    public void createDepartmentDeadlines() {
        try {
            departmentDeadlines = new HashMap<>();
            List<Deadline> deadlines = deadlinesRepository.findAll();
            departmentDeadlinesOptional = deadlines.stream()
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
            LOG.error("Error during @PostConstruct initialization: {}", e.getMessage(), e);
        }
    }

}
