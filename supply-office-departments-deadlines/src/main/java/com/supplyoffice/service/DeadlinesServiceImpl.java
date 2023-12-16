package com.supplyoffice.service;

import com.supplyoffice.dto.DeadlineDTO;
import com.supplyoffice.entities.Deadline;
import com.supplyoffice.repository.DeadlinesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeadlinesServiceImpl implements DeadlinesService {

    @Autowired
    DeadlinesRepository deadlinesRepository;
    @Autowired
    private WebClient webClientProcessor;
    @Autowired
    private WebClient webClientReceiver;

    Logger LOG = LoggerFactory.getLogger(DeadlinesServiceImpl.class);

    @Override
    @Transactional
    public void addDepartment(DeadlineDTO deadlineDTO) {
        LOG.debug("The request for adding new department to deadlines DB table is processed inside service class.");
        Deadline newDepartmentDeadline = Deadline.of(deadlineDTO, false);
        Deadline savedDepartmentDeadline = deadlinesRepository.save(newDepartmentDeadline);
        LOG.debug("Created new department {} and it's deadline is set to {}.", savedDepartmentDeadline.getDepartmentName(), savedDepartmentDeadline.getDeadline());
    }

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
            notifySupplyRequestReceiver(deadlineDTO.getDepartmentName(), deadline.getDeadline());
            LOG.debug("New deadline is now scheduled to {}.", deadline.getDeadline());
            notifyProcessorService(deadlineDTO);
            LOG.debug("Notified the requests processor that a deadline for department {} was updated to {}.", deadlineDTO.getDepartmentName(), deadlineDTO.getDeadline());
            return String.format
                    ("Next deadline for supply for department %s is set to %s.",
                            deadlineDTO.getDepartmentName(), deadlineDTO.getDeadline().toString());
        } else {
            throw new EntityNotFoundException("Entity " + deadlineDTO.getDepartmentName() + " not found.");
        }
    }

    @Override
    @Transactional
    public String setActiveStatus(String departmentName, int status) {
        deadlinesRepository.updateStatusByName(departmentName, status);
        return String.format("Status updated for department %s to %s.",
                departmentName, Boolean.valueOf(String.valueOf(status)));
    }

    @Override
    public LocalDateTime getDeadline(String departmentName) {
        return deadlinesRepository.findDeadlineByName(departmentName);
    }

    @Override
    public List<Deadline> getAllDeadlines() {
        return deadlinesRepository.findAll();
    }

    private void notifySupplyRequestReceiver(String departmentName, LocalDateTime deadline)
            throws WebClientRequestException {
        DeadlineDTO deadlineDTO = new DeadlineDTO(departmentName, deadline);
        webClientReceiver.put()
                .uri("/deadline")
                .bodyValue(deadlineDTO)
                .retrieve()
                .toBodilessEntity()
                .onErrorResume(throwable -> {
                    LOG.debug(throwable.getMessage());
                    return Mono.empty();
                })
                .subscribe(response -> {
                    LOG.debug("Receiver service notified.");
                });
    }

    public void notifyProcessorService(DeadlineDTO deadlineDTO) throws WebClientRequestException {
        if (deadlineDTO != null) {
            webClientProcessor.put()
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
