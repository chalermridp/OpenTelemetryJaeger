package com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.web;

import com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.domain.Configuration;
import com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.service.ConfigurationService;
import com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.service.ExternalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(path = "/v1/configurations")
public class ConfigurationController {
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private ExternalService externalService;

    @GetMapping
    public ResponseEntity<List<Configuration>> getAll() {
        List<Configuration> response = configurationService.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/external")
    public ResponseEntity<List<Configuration>> getAllFromExternal() throws URISyntaxException, JsonProcessingException {
        List<Configuration> response = externalService.getConfigurations();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
