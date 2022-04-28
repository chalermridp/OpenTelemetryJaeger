package com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.service;

import com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.domain.Configuration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class ExternalService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    private final String SERVICE_B_URL = "http://localhost:8081";

    public String getHello() throws URISyntaxException {
        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(new URI(SERVICE_B_URL + "/v1/hello"), String.class);
        return responseEntity.getBody();
    }

    public List<Configuration> getConfigurations() throws URISyntaxException, JsonProcessingException {
        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity(new URI(SERVICE_B_URL + "/v1/configurations"), String.class);
        return objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {
        });
    }
}
