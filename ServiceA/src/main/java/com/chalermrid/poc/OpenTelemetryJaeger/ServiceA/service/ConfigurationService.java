package com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.service;

import com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.domain.Configuration;
import com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.repository.ConfigurationRepository;
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
