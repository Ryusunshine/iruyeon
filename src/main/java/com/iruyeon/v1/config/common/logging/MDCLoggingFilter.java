package com.iruyeon.v1.config.common.logging;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class MDCLoggingFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(MDCLoggingFilter.class);
    private static final String REQUEST_ID_MDC_KEY = "request_id";
    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final String LAYER_MDC_KEY = "layer";
    private static final String API_MDC_KEY = "api";
    private static final String ERROR_SOURCE_MDC_KEY = "error_source";
    private static final String DEFAULT_MDC_VALUE = "-";

    @PostConstruct
    public void init() {
        setDefaultMDC();
    }

    private void setDefaultMDC() {
        MDC.clear();
        MDC.put(REQUEST_ID_MDC_KEY, DEFAULT_MDC_VALUE);
        MDC.put(LAYER_MDC_KEY, DEFAULT_MDC_VALUE);
        MDC.put(API_MDC_KEY, DEFAULT_MDC_VALUE);
        MDC.put(ERROR_SOURCE_MDC_KEY, DEFAULT_MDC_VALUE);
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String requestId = httpServletRequest.getHeader(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }

        try {
            setupMDC(httpServletRequest, requestId);

            httpServletRequest.setAttribute(REQUEST_ID_MDC_KEY, requestId);
            httpServletResponse.setHeader(REQUEST_ID_HEADER, requestId);

            filterChain.doFilter(servletRequest, servletResponse);

        } finally {
            setDefaultMDC();
        }
    }

    private void setupMDC(HttpServletRequest request, String requestId) {
        MDC.clear();
        MDC.put(REQUEST_ID_MDC_KEY, requestId != null ? requestId : DEFAULT_MDC_VALUE);
        MDC.put(LAYER_MDC_KEY, determineLayer());

        // 현재 실행 중인 클래스명 찾기
        try {
            Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
            if (throwable != null && throwable.getStackTrace().length > 0) {
                // 스택트레이스의 첫 번째 요소 가져오기 (실제 예외 발생 지점)
                StackTraceElement element = throwable.getStackTrace()[0];
                String fileName = element.getFileName() + ":" + element.getLineNumber();
                MDC.put(ERROR_SOURCE_MDC_KEY, fileName);
            } else {
                MDC.put(ERROR_SOURCE_MDC_KEY, DEFAULT_MDC_VALUE);
            }
        } catch (Exception e) {
            MDC.put(ERROR_SOURCE_MDC_KEY, DEFAULT_MDC_VALUE);
        }

        String uri = request.getRequestURI();
        String method = request.getMethod();
        if (uri != null && method != null) {
            MDC.put(API_MDC_KEY, String.format("%s %s", method, uri));
        } else {
            MDC.put(API_MDC_KEY, DEFAULT_MDC_VALUE);
        }
    }

    private String determineLayer() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            String className = element.getClassName();

            if (className.contains(".controller.")) {
                return "CONTROLLER";
            } else if (className.contains(".service.")) {
                return "SERVICE";
            } else if (className.contains(".repository.")) {
                return "REPOSITORY";
            } else if (className.contains(".filter.")) {
                return "FILTER";
            } else if (className.contains("org.springframework.")) {
                return "SPRING";
            } else if (className.contains("org.hibernate.")) {
                return "HIBERNATE";
            } else if (className.contains("org.apache.catalina.")) {
                return "TOMCAT";
            }
        }
        return "SYSTEM";
    }
}