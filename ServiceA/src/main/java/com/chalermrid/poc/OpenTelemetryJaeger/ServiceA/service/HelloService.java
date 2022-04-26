package com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HelloService {
    public String sayHello() {
        return "hello: " + LocalDateTime.now();
    }
}
