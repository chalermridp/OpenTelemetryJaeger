package com.chalermrid.poc.OpenTelemetryJaeger.ServiceB.configuration;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.exporter.jaeger.JaegerGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracerConfig {
    @Bean("tracer")
    public Tracer tracer(@Qualifier("openTelemetry") OpenTelemetry openTelemetry) {
        return openTelemetry.getTracer("com.chalermrid.poc.OpenTelemetryJaeger.ServiceB");
    }

    @Bean("textMapPropagator")
    public TextMapPropagator textMapPropagator(@Qualifier("openTelemetry") OpenTelemetry openTelemetry) {
        return openTelemetry.getPropagators().getTextMapPropagator();
    }

    @Bean("openTelemetry")
    public OpenTelemetry openTelemetry() {
        JaegerGrpcSpanExporter jaegerExporter =
                JaegerGrpcSpanExporter.builder()
                        .build();

        Attributes attributes = Attributes.builder()
                .put("service.name", this.getServiceName())
                .put("env", this.getEnvironment())
                .build();

        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(jaegerExporter).build())
                .setResource(Resource.getDefault().merge(Resource.create(attributes)))
                .setSampler(Sampler.traceIdRatioBased(0.5))
                .build();

        OpenTelemetrySdk openTelemetrySdk = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .buildAndRegisterGlobal();

        Runtime.getRuntime().addShutdownHook(new Thread(sdkTracerProvider::close));
        return openTelemetrySdk;
    }

    private String getEnvironment() {
        return "local";
    }

    private String getServiceName() {
        return "ServiceB";
    }
}
