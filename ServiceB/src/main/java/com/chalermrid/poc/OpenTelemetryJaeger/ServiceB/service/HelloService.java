package com.chalermrid.poc.OpenTelemetryJaeger.ServiceB.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HelloService {
    public String getHello() {
        return "hello from ServiceB: " + LocalDateTime.now();
    }
}
