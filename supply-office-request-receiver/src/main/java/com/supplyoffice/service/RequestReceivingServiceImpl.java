package com.supplyoffice.service;

import com.supplyoffice.dto.RequestDTO;
import com.supplyoffice.dto.UpdateRequestDTO;
import com.supplyoffice.entities.SupplyRequest;
import com.supplyoffice.repositories.DeadlinesRepository;
import com.supplyoffice.repositories.DepartmentsRepository;
import com.supplyoffice.repositories.SupplyRequestsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RequestReceivingServiceImpl implements RequestReceivingService {

    @Autowired
    private SupplyRequestsRepository supplyRequestsRepository;
    @Autowired
    private DepartmentsRepository departmentsRepository;
    @Autowired
    private DeadlinesRepository deadlinesRepository;

    static Logger LOG = LoggerFactory.getLogger(RequestReceivingServiceImpl.class);

    @Override
    @Transactional
    public String addRequest(RequestDTO requestDTO) {
        LOG.debug("The new request is processed inside RequestReceivingService class.");
        if (departmentExists(requestDTO.getDepartmentName())) {
            LOG.debug("Department found: {}.", requestDTO.getDepartmentName());
            LocalDateTime deadline = getDeadline(requestDTO.getDepartmentName());
            SupplyRequest newRequest = SupplyRequest.of(requestDTO);
            newRequest.setDeadline(deadline);
            SupplyRequest savedRequest = supplyRequestsRepository.save(newRequest);
            LOG.debug("New Request added, ID: {}.", savedRequest.getId());
            return String.format
                    ("New request for department %s added. Request number: %d.",
                            savedRequest.getDepartmentName(), savedRequest.getId());
        }
        else {
            throw new EntityNotFoundException("Entity with name " + requestDTO.getDepartmentName() + " not found.");
        }
    }

    @Override
    public String updateRequest(UpdateRequestDTO requestDTO) {
        LOG.debug("Request update is processed inside RequestReceivingService class.");
        Optional<SupplyRequest> optRequestToUpdate = supplyRequestsRepository.findById(String.valueOf(requestDTO.getId()));
        if (optRequestToUpdate.isPresent()) {
            LOG.debug("Found the request for update.");
            SupplyRequest requestToUpdate = optRequestToUpdate.get();
            requestToUpdate.setItem(requestDTO.getItem());
            requestToUpdate.setQuantity(requestDTO.getQuantity());
            requestToUpdate.setComments(requestDTO.getComments());
            supplyRequestsRepository.save(requestToUpdate);
            LOG.debug("Request number {} updated successfully!", requestDTO.getId());
            return String.format("Request with ID %d was updated.", requestDTO.getId());
        } else {
            throw new EntityNotFoundException("Request with ID " + requestDTO.getId() + " wasn't found.");
        }
    }

    @Override
    public String removeRequest(long id) {
        LOG.debug("Delete request is processed inside RequestReceivingService class.");
        if (supplyRequestsRepository.existsById(String.valueOf(id))) {
            supplyRequestsRepository.deleteById(String.valueOf(id));
            LOG.debug("Request number {} deleted.", id);
            return String.format("Request number %d deleted.", id);
        } else {
            throw new EntityNotFoundException("Request with ID " + id + " wasn't found.");
        }
    }

    private LocalDateTime getDeadline(String departmentName) {
        return deadlinesRepository.findDeadlineByName(departmentName);
    }

    private boolean departmentExists(String departmentName) {
        return departmentsRepository.existsByName(departmentName) > 0;
    }
}
