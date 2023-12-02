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

import java.time.LocalDateTime;

@Service
public class DeadlinesServiceImpl implements DeadlinesService {

    @Autowired
    DeadlinesRepository deadlinesRepository;
    @Autowired
    SupplyRequestsRepository supplyRequestsRepository;

    Logger LOG = LoggerFactory.getLogger(DeadlinesServiceImpl.class);

    @Override
    @Transactional
    public String setDeadline(DeadlineDTO deadlineDTO) {
        LOG.debug("The request for updating a deadline for department {} is processed inside the service class.", deadlineDTO.getDepartmentName());
        Deadline deadline = deadlinesRepository.findByName(deadlineDTO.getDepartmentName());
        if (deadline != null) {
            LOG.debug("Found the requested department in DB. Scheduling a new deadline for supply request.");
            deadline.setDeadline(deadlineDTO.getDeadline());
            deadlinesRepository.save(deadline);
            updateSupplyRequestRepository(deadlineDTO.getDepartmentName(), deadline.getDeadline());
            LOG.debug("New deadline is now being scheduled...");
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
}
