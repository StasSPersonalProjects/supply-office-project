package com.supplyoffice.service;

import com.supplyoffice.dto.DepartmentDTO;
import com.supplyoffice.dto.DepartmentRequestDTO;
import com.supplyoffice.entities.Department;
import com.supplyoffice.entities.DepartmentDeadline;
import com.supplyoffice.repositories.DeadlinesRepository;
import com.supplyoffice.repositories.DepartmentsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DepartmentsManagerServiceImpl implements DepartmentsManagerService {

    @Autowired
    DepartmentsRepository departmentsRepository;
    @Autowired
    DeadlinesRepository deadlinesRepository;

    static Logger LOG = LoggerFactory.getLogger(DepartmentsManagerServiceImpl.class);

    @Override
    public List<DepartmentRequestDTO> getAllDepartments() {
        LOG.debug("The request for fetching all departments is processed inside the service class.");
        return convertToDepReqDTOList(departmentsRepository.findAll());
    }

    @Override
    @Transactional
    public String addDepartment(DepartmentRequestDTO departmentDTO) {
        LOG.debug("The request for adding new department is processed inside the service class.");
        String result;
        if (departmentsRepository.findByName(departmentDTO.getName()) != null) {
            result = "The department already exists.";
            LOG.debug(result);
            return result;
        }
        LOG.debug("The department wasn't found in the database, proceed to adding.");
        Department newDep = new Department();
        newDep.setName(departmentDTO.getName());
        newDep.setManager(departmentDTO.getManager());
        newDep.setDescription(departmentDTO.getDescription());
        DepartmentDeadline newDepDL = DepartmentDeadline.of(departmentsRepository.save(newDep));
        deadlinesRepository.save(newDepDL);
        result = String.format("Created new department %s with ID %d.", newDep.getName(), newDep.getId());
        LOG.debug(result);
        LOG.debug("Created new department in 'deadlines' DB, current deadline is NULL.");
        return result;
    }

    @Override
    @Transactional
    public DepartmentRequestDTO updateDepartment(DepartmentDTO departmentDTO) throws NoSuchElementException {
        LOG.debug("The request for updating department with ID {} is processed inside the service class.", departmentDTO.getId());
        Optional<Department> depOptional = departmentsRepository.findById(departmentDTO.getId());
        if (depOptional.isPresent()) {
            Department depForUpdate = depOptional.get();
            depForUpdate.setName(departmentDTO.getName());
            depForUpdate.setManager(departmentDTO.getManager());
            depForUpdate.setDescription(departmentDTO.getDescription());
            return DepartmentRequestDTO.of(departmentsRepository.save(depForUpdate));
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

    private List<DepartmentRequestDTO> convertToDepReqDTOList(List<Department> listOfDepartments) {
        return listOfDepartments.stream().map(DepartmentRequestDTO::of).toList();
    }
}
