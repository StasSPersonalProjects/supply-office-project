package com.supplyoffice.service;

import com.supplyoffice.dto.DeadlineDTO;
import com.supplyoffice.entities.Deadline;

import java.time.LocalDateTime;
import java.util.List;

public interface DeadlinesService {

    void addDepartment(DeadlineDTO deadlineDTO);

    String setDeadline(DeadlineDTO deadlineDTO);

    String setActiveStatus(String departmentName, int status);

    LocalDateTime getDeadline(String departmentName);

    List<Deadline> getAllDeadlines();
}
