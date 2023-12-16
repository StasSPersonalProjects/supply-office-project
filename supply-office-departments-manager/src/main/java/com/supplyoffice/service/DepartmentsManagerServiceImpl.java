package com.supplyoffice.service;

import com.supplyoffice.dto.DeadlineDTO;
import com.supplyoffice.dto.DepartmentDTO;
import com.supplyoffice.dto.NewDepartmentDTO;
import com.supplyoffice.entities.Department;
import com.supplyoffice.repository.DepartmentsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DepartmentsManagerServiceImpl implements DepartmentsManagerService {

    @Autowired
    DepartmentsRepository departmentsRepository;
    @Autowired
    private WebClient webClient;

    static Logger LOG = LoggerFactory.getLogger(DepartmentsManagerServiceImpl.class);

    @Override
    public List<NewDepartmentDTO> getAllDepartments() {
        LOG.debug("The request for fetching all departments is processed inside the service class.");
        return departmentsRepository.findAll().stream().map(NewDepartmentDTO::of).toList();
    }

    @Override
    @Transactional
    public String addDepartment(NewDepartmentDTO departmentDTO) {
        LOG.debug("The request for adding new department is processed inside the service class.");
        String result;
        if (existsByName(departmentDTO.getName())) {
            result = "The department already exists.";
            LOG.debug(result);
            return result;
        }
        LOG.debug("The department wasn't found in the database, proceed to adding.");
        Department newDep = new Department();
        newDep.setName(departmentDTO.getName());
        newDep.setManager(departmentDTO.getManager());
        newDep.setDescription(departmentDTO.getDescription());
        Department savedDepartment = departmentsRepository.save(newDep);
        notifyDeadlinesService(DeadlineDTO.of(departmentDTO.getName()));
        result = String.format("Created new department %s with ID %d.", savedDepartment.getName(), savedDepartment.getId());
        LOG.debug(result);
        LOG.debug("Created new department in 'deadlines' DB.");
        return result;
    }

    private void notifyDeadlinesService(DeadlineDTO departmentDeadline) throws WebClientRequestException {
        if (departmentDeadline != null) {
            webClient.post()
                    .uri("/department")
                    .bodyValue(departmentDeadline)
                    .retrieve()
                    .toBodilessEntity()
                    .onErrorResume(throwable -> {
                        LOG.debug(throwable.getMessage());
                        return Mono.empty();
                    })
                    .subscribe(response -> {
                        LOG.debug("Deadlines service notified.");
                    });
        } else {
            throw new IllegalArgumentException("deadlineDTO was null.");
        }
    }

    public boolean existsByName(String name) {
        return departmentsRepository.existsByName(name) > 0;
    }

    @Override
    @Transactional
    public NewDepartmentDTO updateDepartment(DepartmentDTO departmentDTO) throws NoSuchElementException {
        LOG.debug("The request for updating department with ID {} is processed inside the service class.", departmentDTO.getId());
        Optional<Department> depOptional = departmentsRepository.findById(departmentDTO.getId());
        if (depOptional.isPresent()) {
            Department depForUpdate = depOptional.get();
            depForUpdate.setName(departmentDTO.getName());
            depForUpdate.setManager(departmentDTO.getManager());
            depForUpdate.setDescription(departmentDTO.getDescription());
            return NewDepartmentDTO.of(departmentsRepository.save(depForUpdate));
        } else {
            throw new EntityNotFoundException("Entity with ID " + departmentDTO.getId() + " not found.");
        }
    }

    @Override
    @Transactional
    public String removeDepartment(int id) {
        String result;
        LOG.debug("The request for deleting department with ID {} is processed inside the service class.", id);
        Optional<Department> depOptional = departmentsRepository.findById(id);
        if (depOptional.isPresent()) {
            departmentsRepository.deleteById(id);
            result = "Department with ID " + id + " successfully deleted.";
        } else {
            throw new EntityNotFoundException("Entity with ID " + id + " not found.");
        }
        return result;
    }
}
