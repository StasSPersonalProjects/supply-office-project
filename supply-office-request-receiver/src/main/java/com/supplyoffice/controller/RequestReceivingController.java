package com.supplyoffice.controller;

import com.supplyoffice.dto.DeadlineDTO;
import com.supplyoffice.dto.RequestDTO;
import com.supplyoffice.dto.UpdateRequestDTO;
import com.supplyoffice.service.RequestReceivingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/request")
@Validated
public class RequestReceivingController {

    @Autowired
    RequestReceivingService service;

    static Logger LOG = LoggerFactory.getLogger(RequestReceivingController.class);

    @GetMapping(value = "/all/{departmentName}")
    List<UpdateRequestDTO> getAllRequestsByName(@PathVariable String departmentName) {
        LOG.debug("Received request to fetch all supply requests for department {}.", departmentName);
        return service.getAllRequestsByName(departmentName);
    }

    @PostMapping
    ResponseEntity<String> addRequest(@RequestBody @Valid RequestDTO requestDTO)
            throws MethodArgumentNotValidException {
        LOG.debug("Received new request for department {}.", requestDTO.getDepartmentName());
        String response = service.addRequest(requestDTO);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    ResponseEntity<String> updateRequest(@RequestBody @Valid UpdateRequestDTO requestDTO)
            throws MethodArgumentNotValidException {
        LOG.debug("Received update for request with ID {}.", requestDTO.getId());
        String response = service.updateRequest(requestDTO);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<String> deleteRequest(@PathVariable long id) {
        LOG.debug("Received delete request for ID {}.", id);
        String response = service.removeRequest(id);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/deadline")
    void updateDeadline(@RequestBody DeadlineDTO deadlineDTO) {
        LOG.debug("Received update for deadline to {} for department {}.", deadlineDTO.getDeadline(), deadlineDTO.getDepartmentName());
        service.updateDeadline(deadlineDTO);
    }

    @DeleteMapping(value = "/remove/{departmentName}")
    void removeRequestsByDepartment(@PathVariable String departmentName) {
        LOG.debug("Received delete request for all supply requests for department {}.", departmentName);
        service.removeAllByName(departmentName);
    }
}
