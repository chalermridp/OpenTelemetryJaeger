package com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.web.interceptor;

import io.opentelemetry.api.trace.*;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.context.propagation.TextMapSetter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Slf4j
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private Tracer tracer;
    private TextMapPropagator textMapPropagator;

    public RestTemplateInterceptor(Tracer tracer, TextMapPropagator textMapPropagator) {
        this.tracer = tracer;
        this.textMapPropagator = textMapPropagator;
    }

    // Tell OpenTelemetry to inject the context in the HTTP headers
    TextMapSetter<HttpRequest> setter =
            new TextMapSetter<HttpRequest>() {
                @Override
                public void set(HttpRequest carrier, String key, String value) {
                    // Insert the context as Header
                    carrier.getHeaders().set(key, value);
                }
            };

    private final TextMapGetter<HttpRequest> getter =
            new TextMapGetter<>() {
                @Override
                public Iterable<String> keys(HttpRequest carrier) {
                    return carrier.getHeaders().keySet();
                }

                @Override
                public String get(HttpRequest carrier, String key) {
                    if (carrier.getHeaders().get(key) != null) {
                        return carrier.getHeaders().get(key).get(0);
                    }
                    return "";
                }
            };

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        log.info("intercept");

        String spanName = request.getMethod() + " " + request.getURI().getPath();
        SpanBuilder spanBuilder = tracer.spanBuilder(spanName)
                .setSpanKind(SpanKind.CLIENT);

        ClientHttpResponse response;
        Span span = spanBuilder.startSpan();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("http.method", String.valueOf(request.getMethod()));
            span.setAttribute("component", "http");
            span.setAttribute("http.url", String.valueOf(request.getURI()));
            textMapPropagator.inject(Context.current(), request, setter);

            log.info(span.toString());
            response = execution.execute(request, body);
        } catch (Throwable throwable) {
            span.setStatus(StatusCode.ERROR, "Something bad happened!");
            span.recordException(throwable);
            throw throwable;
        } finally {
            span.end();
        }
        return response;
    }
}
