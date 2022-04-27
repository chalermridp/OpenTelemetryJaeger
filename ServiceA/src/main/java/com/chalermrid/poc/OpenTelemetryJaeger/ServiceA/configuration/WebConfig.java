package com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.configuration;

import com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.web.interceptor.TracingInterceptor;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.propagation.TextMapPropagator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    @Qualifier("tracer")
    private Tracer tracer;

    @Autowired
    @Qualifier("textMapPropagator")
    private TextMapPropagator textMapPropagator;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TracingInterceptor(tracer, textMapPropagator));
    }
}
