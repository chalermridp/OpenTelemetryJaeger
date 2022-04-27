package com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.web;

import com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.service.ExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/v1/external")
public class ExternalController {
    @Autowired
    private ExternalService externalService;

    @GetMapping(path = "/hello")
    public ResponseEntity<String> getHello() throws URISyntaxException {
        String response = externalService.getHello();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
