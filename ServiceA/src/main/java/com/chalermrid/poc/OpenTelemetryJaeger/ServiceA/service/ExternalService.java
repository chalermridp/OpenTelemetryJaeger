package com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class ExternalService {
    @Autowired
    private RestTemplate restTemplate;

    private final String SERVICE_B_URL = "http://localhost:8081";

    public String getHello() throws URISyntaxException {
        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(new URI(SERVICE_B_URL + "/v1/hello"), String.class);
        return responseEntity.getBody();
    }
}
