package com.chalermrid.poc.OpenTelemetryJaeger.ServiceB.web.interceptor;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class TracingInterceptor implements HandlerInterceptor {
    private Tracer tracer;

    public TracingInterceptor(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("preHandle");

        String spanName = request.getMethod() + " " + request.getRequestURI();
        Span span = tracer.spanBuilder(spanName)
                .setSpanKind(SpanKind.SERVER)
                .startSpan();
        Scope scope = span.makeCurrent();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.info("postHandle");

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("afterCompletion");

        Span span = Span.current();
        if (ex != null) {
            span.setStatus(StatusCode.ERROR, "Something bad happened!");
            span.recordException(ex);
        }
        span.end();
    }
}
