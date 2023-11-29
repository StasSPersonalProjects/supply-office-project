package com.supplyoffice.service;

import com.supplyoffice.dto.DeadlineDTO;
import com.supplyoffice.entities.Deadline;
import com.supplyoffice.repositories.DeadlinesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeadlinesServiceImpl implements DeadlinesService {

    @Autowired
    DeadlinesRepository repository;

    Logger LOG = LoggerFactory.getLogger(DeadlinesServiceImpl.class);

    @Override
    public String setDeadline(DeadlineDTO deadlineDTO) {
        LOG.debug("The request for updating a deadline for department {} is processed inside the service class.", deadlineDTO.getDepartmentName());
        Deadline deadline = repository.findByName(deadlineDTO.getDepartmentName());
        if (deadline != null) {
            LOG.debug("Found the requested department in DB. Scheduling a new deadline for supply request.");
            deadline.setDeadline(deadlineDTO.getDeadline());
            repository.save(deadline);
            LOG.debug("New deadline is now being scheduled...");
            return String.format
                    ("Next deadline for supply for department %s is set to %s.",
                            deadlineDTO.getDepartmentName(), deadlineDTO.getDeadline().toString());
        } else {
            throw new EntityNotFoundException("Entity " + deadlineDTO.getDepartmentName() + " not found.");
        }
    }
}
