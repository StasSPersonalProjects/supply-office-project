package com.supplyoffice.component;

import com.supplyoffice.dto.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SecurityServiceClient {

    @Value("${application.security.url}")
    private String securityServiceUrl;
    @Autowired
    private RestTemplate restTemplate;

    public boolean isTokenValid(String jwtToken) {
        String validationURL = securityServiceUrl + "/valid";
        HttpEntity<String> entity = getHttpEntity("Authorization", jwtToken);
        ResponseEntity<String> response = restTemplate.exchange(
                validationURL,
                HttpMethod.GET,
                entity,
                String.class
        );
        return response.getStatusCode().is2xxSuccessful();
    }

    public AuthenticationResponse refreshJwtToken(String refreshToken) {
        String refreshURL = securityServiceUrl + "/refresh";
        HttpEntity<String> entity = getHttpEntity("Refresh", refreshToken);
        ResponseEntity<AuthenticationResponse> response = restTemplate.exchange(
                refreshURL,
                HttpMethod.POST,
                entity,
                AuthenticationResponse.class
        );
        return response.getBody();
    }

    private HttpEntity<String> getHttpEntity(String headerField, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(headerField, "Bearer " + token);
        return new HttpEntity<>(headers);
    }
}
