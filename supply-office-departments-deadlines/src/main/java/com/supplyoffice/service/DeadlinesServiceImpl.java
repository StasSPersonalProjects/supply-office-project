package com.supplyoffice.service;

import com.supplyoffice.dto.DeadlineDTO;
import com.supplyoffice.entities.Deadline;
import com.supplyoffice.repositories.DeadlinesRepository;
import com.supplyoffice.repositories.SupplyRequestsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.time.LocalDateTime;

@Service
public class DeadlinesServiceImpl implements DeadlinesService {

    @Autowired
    DeadlinesRepository deadlinesRepository;
    @Autowired
    SupplyRequestsRepository supplyRequestsRepository;
    @Autowired
    private WebClient webClient;

    Logger LOG = LoggerFactory.getLogger(DeadlinesServiceImpl.class);

    @Override
    @Transactional
    public String setDeadline(DeadlineDTO deadlineDTO) {
        LOG.debug("The request for updating a deadline for department {} is processed inside the service class.", deadlineDTO.getDepartmentName());
        Deadline deadline = deadlinesRepository.findByName(deadlineDTO.getDepartmentName());
        if (deadline != null) {
            LOG.debug("Found the requested department in DB. Scheduling a new deadline for supply request.");
            deadline.setDeadline(deadlineDTO.getDeadline());
            deadline.setActive(true);
            deadlinesRepository.save(deadline);
            notifyProcessor(deadlineDTO);
            LOG.debug("Notified the requests processor that a deadline for department {} was updated to {}.", deadlineDTO.getDepartmentName(), deadlineDTO.getDeadline());
            updateSupplyRequestRepository(deadlineDTO.getDepartmentName(), deadline.getDeadline());
            LOG.debug("New deadline is now scheduled to {}.", deadline.getDeadline());
            return String.format
                    ("Next deadline for supply for department %s is set to %s.",
                            deadlineDTO.getDepartmentName(), deadlineDTO.getDeadline().toString());
        } else {
            throw new EntityNotFoundException("Entity " + deadlineDTO.getDepartmentName() + " not found.");
        }
    }

    @Transactional
    private void updateSupplyRequestRepository(String departmentName, LocalDateTime deadline) {
        supplyRequestsRepository.updateDeadlineByName(departmentName, deadline);
        LOG.debug("Updated the deadline for department {} to {} in the supply_requests DB.", departmentName, deadline);
    }

    public void notifyProcessor(DeadlineDTO deadlineDTO) throws WebClientRequestException {
        if (deadlineDTO != null) {
            webClient.put()
                    .uri("/update-deadline")
                    .bodyValue(deadlineDTO)
                    .retrieve()
                    .toBodilessEntity()
                    .subscribe();
            LOG.debug("Processor notified.");
        } else {
            throw new IllegalArgumentException("deadlineDTO was null.");
        }
    }
}
