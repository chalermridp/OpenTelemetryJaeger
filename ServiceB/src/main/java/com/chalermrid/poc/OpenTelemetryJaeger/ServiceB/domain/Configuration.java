package com.chalermrid.poc.OpenTelemetryJaeger.ServiceB.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "w_configuration")
public class Configuration {
    @Id
    private String id;
    private String val;
}
