package com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.repository;

import com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.domain.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, String> {
}
