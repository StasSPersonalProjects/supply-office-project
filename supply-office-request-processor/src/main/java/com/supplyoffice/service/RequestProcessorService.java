package com.supplyoffice.service;

import com.supplyoffice.dto.DeadlineDTO;

public interface RequestProcessorService {

    void updateDepartmentDeadlines(DeadlineDTO deadlineDTO);
    void removeRequestsByDepartmentName(String name);

}
