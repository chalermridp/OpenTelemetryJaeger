package com.chalermrid.poc.OpenTelemetryJaeger.ServiceB.service;

import com.chalermrid.poc.OpenTelemetryJaeger.ServiceB.domain.Configuration;
import com.chalermrid.poc.OpenTelemetryJaeger.ServiceB.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigurationService {
    @Autowired
    private ConfigurationRepository configurationRepository;

    public List<Configuration> getAll() {
        return configurationRepository.findAll();
    }
}
