package com.chalermrid.poc.OpenTelemetryJaeger.ServiceB.repository;

import com.chalermrid.poc.OpenTelemetryJaeger.ServiceB.domain.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, String> {
}
