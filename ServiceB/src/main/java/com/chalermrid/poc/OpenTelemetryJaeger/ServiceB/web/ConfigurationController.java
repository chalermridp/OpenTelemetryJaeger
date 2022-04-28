package com.chalermrid.poc.OpenTelemetryJaeger.ServiceB.web;

import com.chalermrid.poc.OpenTelemetryJaeger.ServiceB.domain.Configuration;
import com.chalermrid.poc.OpenTelemetryJaeger.ServiceB.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/configurations")
public class ConfigurationController {
    @Autowired
    private ConfigurationService configurationService;

    @GetMapping
    public ResponseEntity<List<Configuration>> getAll() {
        List<Configuration> response = configurationService.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
