package com.supplyoffice.service;

import com.supplyoffice.dto.RequestDTO;
import com.supplyoffice.dto.UpdateRequestDTO;

public interface RequestReceivingService {

    String addRequest(RequestDTO requestDTO);
    String updateRequest(UpdateRequestDTO requestDTO);
    String removeRequest(long id);
}
