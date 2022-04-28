package com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.web.interceptor;

import io.opentelemetry.api.trace.*;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Objects;

@Component
@Slf4j
public class MethodTracingInterceptor implements HandlerInterceptor {
    private Tracer tracer;
    private TextMapPropagator textMapPropagator;
    private Span span;

    private final TextMapGetter<HttpServletRequest> getter =
            new TextMapGetter<>() {
                @Override
                public Iterable<String> keys(HttpServletRequest carrier) {
                    return Collections.list(carrier.getHeaderNames());
                }

                @Override
                public String get(HttpServletRequest carrier, String key) {
                    if (carrier.getHeader(key) != null) {
                        return carrier.getHeader(key);
                    }
                    return "";
                }
            };

    public MethodTracingInterceptor(Tracer tracer, TextMapPropagator textMapPropagator) {
        this.tracer = tracer;
        this.textMapPropagator = textMapPropagator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws URISyntaxException {
        log.info("MethodTracingInterceptor.preHandle");
        if (handler instanceof HandlerMethod handlerMethod) {
            Method method = handlerMethod.getMethod();

            String spanName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
            SpanBuilder spanBuilder = tracer.spanBuilder(spanName)
                    .setSpanKind(SpanKind.SERVER);

            Context context = textMapPropagator.extract(Context.current(), request, getter);
            if (!Objects.equals(context.toString(), "{}")) {
                spanBuilder.setParent(context);
            }

            span = spanBuilder.startSpan();
            Scope scope = span.makeCurrent();
            log.info(span.toString());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("MethodTracingInterceptor.afterCompletion");

        if (ex != null) {
            span.setStatus(StatusCode.ERROR, "Something bad happened!");
            span.recordException(ex);
        }
        span.end();
    }
}
