package com.supplyoffice.service;

import com.supplyoffice.dto.DeadlineDTO;
import com.supplyoffice.dto.RequestDTO;
import com.supplyoffice.dto.UpdateRequestDTO;

import java.util.List;

public interface RequestReceivingService {

    String addRequest(RequestDTO requestDTO);
    String updateRequest(UpdateRequestDTO requestDTO);
    String removeRequest(long id);
    void updateDeadline(DeadlineDTO deadlineDTO);
    List<UpdateRequestDTO> getAllRequestsByName(String departmentName);
    void removeAllByName(String departmentName);
}
