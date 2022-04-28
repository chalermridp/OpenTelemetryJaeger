package com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "configuration")
public class Configuration {
    @Id
    private String id;
    private String val;
}
