package com.supplyoffice.component;

import com.supplyoffice.dto.UpdateRequestDTO;
import com.supplyoffice.entities.SupplyRequest;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Component
public class ScheduledFetchAndSend {

    @Autowired
    EmailSender emailSender;
    @Autowired
    ExcelGenerator excelGenerator;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${requests.service.get.url}")
    private String requestsGetUrl;

    Logger LOG = LoggerFactory.getLogger(ScheduledFetchAndSend.class);

    public void fetchDataAndSendEmail(String departmentName) throws IOException, MessagingException {
        List<SupplyRequest> requests = getAllRequests(departmentName);
        if (requests.isEmpty()) {
            LOG.debug("Didn't find any requests that need to be processed.");
            return;
        }
        LOG.debug("Found requests.");
        String[][] convertedRequests = convertTo2dArray(requests);
        String requestsExcelFile = excelGenerator.generateExcel(convertedRequests, departmentName);
        emailSender.sendEmailWithAttachment(requestsExcelFile);
    }

    private List<SupplyRequest> getAllRequests(String departmentName) {
        LOG.debug("Fetching all requests for department {}.", departmentName);
        ResponseEntity<List<UpdateRequestDTO>> responseEntity = restTemplate.exchange(
                requestsGetUrl + departmentName,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UpdateRequestDTO>>() {}
        );
        List<UpdateRequestDTO> fetchedRequests = responseEntity.getBody();
        return fetchedRequests.stream().map(SupplyRequest::of).toList();
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
