package com.supplyoffice.service;

import com.supplyoffice.dto.DeadlineDTO;

public interface DeadlinesService {

    String setDeadline(DeadlineDTO deadlineDTO);

    String setActiveStatus(String departmentName, int status);
}
