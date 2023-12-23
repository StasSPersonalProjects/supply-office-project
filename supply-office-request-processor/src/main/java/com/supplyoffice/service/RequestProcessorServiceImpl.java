package com.supplyoffice.service;

import com.supplyoffice.component.ScheduledFetchAndSend;
import com.supplyoffice.dto.DeadlineDTO;
import com.supplyoffice.entities.Deadline;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RequestProcessorServiceImpl implements RequestProcessorService {

    @Autowired
    private ScheduledFetchAndSend scheduledFetchAndSend;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    WebClient webClientDeadlines;
    @Autowired
    WebClient webClientReceiver;

    @Value("${deadlines.service.get.url}")
    private String deadlinesServiceGetUrl;

    private Map<String, LocalDateTime> departmentDeadlines;

    static Logger LOG = LoggerFactory.getLogger(RequestProcessorServiceImpl.class);

    @Scheduled(initialDelay = 30000)
    public void initialCreateDepartmentDeadlines() {
        departmentDeadlines = new HashMap<>();
        createDepartmentDeadlines();
    }

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
                    removeRequestsByDepartmentName(entry.getKey());
                    setToFalseByName(entry.getKey());
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

    public void createDepartmentDeadlines() {
        try {
            List<Deadline> deadlines = getAllDeadlines();
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

    private List<Deadline> getAllDeadlines() {
        LOG.debug("Fetching data from deadlines service.");
        ResponseEntity<List<DeadlineDTO>> responseEntity = restTemplate.exchange(
                deadlinesServiceGetUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DeadlineDTO>>() {});
        List<DeadlineDTO> fetchedDeadlines = responseEntity.getBody();
        return fetchedDeadlines.stream().map(Deadline::of).toList();
    }

    private void setToFalseByName(String name) {
        LOG.debug("Notifying deadlines service to set 'active' field to 'FALSE' for department {}.", name);
        webClientDeadlines.put()
                .uri(builder -> builder
                        .path("/status")
                        .queryParam("name", name)
                        .queryParam("status", "0")
                        .build())
                .retrieve()
                .toBodilessEntity()
                .subscribe(response -> LOG.debug(response.toString()));
    }

    public void removeRequestsByDepartmentName(String name) {
        LOG.debug("Sending 'remove all requests' command to receiver service for department {}.", name);
        webClientReceiver.delete()
                .uri(builder -> builder
                        .path("/remove/" + name)
                        .build())
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }
}
