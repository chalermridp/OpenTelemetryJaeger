package com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.web;

import com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/internal")
public class InternalController {
    @Autowired
    private HelloService helloService;

    @GetMapping(path = "/hello")
    public ResponseEntity<String> sayHello() {
        String response = helloService.sayHello();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
