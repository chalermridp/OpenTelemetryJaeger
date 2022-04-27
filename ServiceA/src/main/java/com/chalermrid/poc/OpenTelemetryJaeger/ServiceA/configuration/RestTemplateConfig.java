package com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.configuration;

import com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.web.interceptor.RestTemplateInterceptor;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.propagation.TextMapPropagator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(@Qualifier("tracer") Tracer tracer,
                                     @Qualifier("textMapPropagator") TextMapPropagator textMapPropagator) {
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors
                = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(new RestTemplateInterceptor(tracer, textMapPropagator));
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
