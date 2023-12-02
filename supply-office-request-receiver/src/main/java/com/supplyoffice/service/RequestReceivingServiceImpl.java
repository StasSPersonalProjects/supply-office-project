package com.supplyoffice.service;

import com.supplyoffice.dto.RequestDTO;
import com.supplyoffice.entities.SupplyRequest;
import com.supplyoffice.repositories.DeadlinesRepository;
import com.supplyoffice.repositories.DepartmentsRepository;
import com.supplyoffice.repositories.SupplyRequestsRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Service
public class RequestReceivingServiceImpl implements RequestReceivingService {

    @Autowired
    private SupplyRequestsRepository supplyRequestsRepository;
    @Autowired
    private DepartmentsRepository departmentsRepository;
    @Autowired
    private DeadlinesRepository deadlinesRepository;
    @Autowired
    private WebClient webClient;

    static Logger LOG = LoggerFactory.getLogger(RequestReceivingServiceImpl.class);

    @Override
    @Transactional
    public String addRequest(@RequestBody @Valid RequestDTO requestDTO) {
        LOG.debug("The new request is processed inside RequestReceivingService class.");
        if (departmentExists(requestDTO.getDepartmentName())) {
            LOG.debug("Department found: {}.", requestDTO.getDepartmentName());
            LocalDateTime deadline = getDeadline(requestDTO.getDepartmentName());
            SupplyRequest newRequest = SupplyRequest.of(requestDTO);
            newRequest.setDeadline(deadline);
            SupplyRequest savedRequest = supplyRequestsRepository.save(newRequest);
            LOG.debug("New Request added, ID: {}. Notifying the processor service.", savedRequest.getId());
            notifyProcessor((Long) savedRequest.getId());
            return String.format
                    ("New request for department %s added. Request number: %d.",
                            savedRequest.getDepartmentName(), savedRequest.getId());
        }
        else {
            throw new EntityNotFoundException("Entity with name " + requestDTO.getDepartmentName() + " not found.");
        }
    }

    private LocalDateTime getDeadline(String departmentName) {
        return deadlinesRepository.findDeadlineByName(departmentName);
    }

    private boolean departmentExists(String departmentName) {
        return departmentsRepository.existsByName(departmentName) > 0;
    }

    public void notifyProcessor(Long requestId) {
        if (requestId != null) {
            webClient.post()
                    .uri("/process-request")
                    .bodyValue(requestId)
                    .retrieve()
                    .toBodilessEntity()
                    .subscribe();
            LOG.debug("Processor notified, sent an ID {}.", requestId);
        } else {
            throw new IllegalArgumentException("requestId was null.");
        }
    }
}
