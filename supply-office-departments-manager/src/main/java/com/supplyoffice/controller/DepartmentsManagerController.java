package com.supplyoffice.controller;

import com.supplyoffice.dto.DepartmentDTO;
import com.supplyoffice.dto.DepartmentRequestDTO;
import com.supplyoffice.service.DepartmentsManagerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/supply")
@Validated
public class DepartmentsManagerController {

    @Autowired
    DepartmentsManagerService service;

    static Logger LOG = LoggerFactory.getLogger(DepartmentsManagerController.class);

    @GetMapping(value = "/deps")
    ResponseEntity<List<DepartmentRequestDTO>> getAllDepartments() {
        LOG.debug("Received request for fetching all the registered departments.");
        List<DepartmentRequestDTO> response = service.getAllDepartments();
        if (response.isEmpty()) {
            LOG.debug("The result is an empty list. No departments in database.");
        }
        return response.isEmpty() ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(response)
                : ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    ResponseEntity<String> addNewDepartment(@RequestBody @Valid DepartmentRequestDTO department)
            throws MethodArgumentNotValidException {
        LOG.debug("Received new department to add, name: {}.", department.getName());
        String response = service.addDepartment(department);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    ResponseEntity<String> updateDepartment(@RequestBody @Valid DepartmentDTO department)
            throws MethodArgumentNotValidException {
        LOG.debug("Received an update request for a department. ID: {}, name: {}", department.getId(), department.getName());
        DepartmentRequestDTO response = service.updateDepartment(department);
        LOG.debug("Update completed successfully!");
        String result = String.format
                ("The updated department is: name - %s, manager - %s, description - %s",
                        response.getName(), response.getManager(), response.getDescription());
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping(value = "/deps/{id}")
    ResponseEntity<String> removeDepartment(@PathVariable int id) {
        LOG.debug("Received a delete request for a department. ID: {}.", id);
        String response = service.removeDepartment(id);
        return ResponseEntity.ok().body(response);
    }

}
