package com.supplyoffice.component;

import com.supplyoffice.entities.SupplyRequest;
import com.supplyoffice.repositories.SupplyRequestsRepository;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ScheduledFetchAndSend {

    @Autowired
    EmailSender emailSender;
    @Autowired
    ExcelGenerator excelGenerator;
    @Autowired
    SupplyRequestsRepository supplyRequestsRepository;

    Logger LOG = LoggerFactory.getLogger(ScheduledFetchAndSend.class);

    public void fetchDataAndSendEmail(String departmentName) throws IOException, MessagingException {
        List<SupplyRequest> requests = supplyRequestsRepository.findAllByName(departmentName);
        if (requests.isEmpty()) {
            LOG.debug("Didn't find any requests that need to be processed.");
            return;
        }
        LOG.debug("Found requests.");
        String[][] convertedRequests = convertTo2dArray(requests);
        String requestsExcelFile = excelGenerator.generateExcel(convertedRequests);
        emailSender.sendEmailWithAttachment(requestsExcelFile);
    }



    private String[][] convertTo2dArray(List<SupplyRequest> requests) {
        LOG.debug("Converting to 2D array...");
        int size = requests.size();
        String[][] result = new String[size][5];
        for (int i = 0; i < size; i++) {
            SupplyRequest request = requests.get(i);
            result[i][0] = request.getDepartmentName();
            result[i][1] = request.getItem();
            result[i][2] = String.valueOf(request.getQuantity());
            result[i][3] = request.getMeasureUnit();
            result[i][4] = request.getComments();
        }
        LOG.debug("Converted to 2D array.");
        return result;
    }
}
