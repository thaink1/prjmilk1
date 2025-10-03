package com.example.demo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.UUID;

import java.io.IOException;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String requestId = request.getHeader("X-Request-ID");
        String signature = request.getHeader("X-Signature");

        if (requestId == null || requestId.isBlank()){
            requestId = UUID.randomUUID().toString();
        }

        MDC.put("requestId", requestId);
        MDC.put("signature", signature != null ? signature : "");

        long start =  System.currentTimeMillis();

        try {
            log.info("Incoming request: method={}, uri={}, X-REQUEST-ID={}, signature={}",
                    request.getMethod(), request.getRequestURI(), requestId, signature);

            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            log.info("Completed request: status={}, duration={}ms",
                    response.getStatus(), duration);

            MDC.clear();
        }

    }
}
