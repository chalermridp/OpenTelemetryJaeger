package com.chalermrid.poc.OpenTelemetryJaeger.ServiceA.web.interceptor;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

@Component
@Slf4j
public class TracingInterceptor implements HandlerInterceptor {
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

    public TracingInterceptor(Tracer tracer, TextMapPropagator textMapPropagator) {
        this.tracer = tracer;
        this.textMapPropagator = textMapPropagator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("preHandle");
        Context context = textMapPropagator.extract(Context.current(), request, getter);
        log.info(context.toString());

        String spanName = request.getMethod() + " " + request.getRequestURI();
        span = tracer.spanBuilder(spanName)
                .setParent(context)
                .setSpanKind(SpanKind.SERVER)
                .startSpan();

        try (Scope scope = span.makeCurrent()) {
            URI uri = new URI(request.getRequestURI());

            span.setAttribute("component", "http");
            span.setAttribute("http.method", request.getMethod());

            span.setAttribute("http.scheme", "http");
            span.setAttribute("http.host", uri.getHost());
            span.setAttribute("http.target", uri.getPath());
        } catch (URISyntaxException e) {
            log.error("preHandle", e);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("afterCompletion");

        if (ex != null) {
            span.setStatus(StatusCode.ERROR, "Something bad happened!");
            span.recordException(ex);
        }
        span.end();
    }
}
