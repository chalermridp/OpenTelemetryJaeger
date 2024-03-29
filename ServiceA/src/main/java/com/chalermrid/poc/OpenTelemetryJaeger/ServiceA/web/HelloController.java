package com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.web;

import com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.service.ExternalService;
import com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping(path = "/v1/hello")
public class HelloController {
    @Autowired
    private HelloService helloService;
    @Autowired
    private ExternalService externalService;

    @GetMapping
    public ResponseEntity<String> getHello() {
        String response = helloService.getHello();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/external")
    public ResponseEntity<String> getHelloFromExternal() throws URISyntaxException {
        String response = externalService.getHello();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
