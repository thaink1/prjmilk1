package com.example.demo.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class SignatureFilter implements Filter {

    @Value("${app.security.secret}")
    private String secretKey;

    @Value("${app.security.allowedSkewSeconds:300}")
    private long allowedSkewSeconds;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();
        // Có thể whitelist nếu cần (vd: health check)
        if (uri.startsWith("/actuator") || uri.equals("/")) {
            chain.doFilter(req, res);
            return;
        }

        String signature = trimOrNull(request.getHeader("X-Signature"));
        String timestamp = trimOrNull(request.getHeader("X-Timestamp"));

        if (signature == null || timestamp == null) {
            unauthorized(response, "Missing X-Signature or X-Timestamp");
            return;
        }

        long now = Instant.now().toEpochMilli();
        long ts;
        try {
            ts = Long.parseLong(timestamp);
        } catch (NumberFormatException e) {
            unauthorized(response, "Invalid timestamp");
            return;
        }

        if (Math.abs(now - ts) > allowedSkewSeconds * 1000) {
            unauthorized(response, "Timestamp too far from current time");
            return;
        }

        // Chuỗi để ký: METHOD + PATH + TIMESTAMPx
        String raw = request.getMethod().toUpperCase() + request.getRequestURI() + timestamp;
        String expected = SignatureUtil.hmacSha256Hex(raw, secretKey);

        if (!expected.equalsIgnoreCase(signature)) {
            unauthorized(response, "Invalid signature");
            return;
        }

        // ✅ Pass check → cho request đi tiếp
        chain.doFilter(req, res);
    }

    private static String trimOrNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private void unauthorized(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + msg + "\"}");
    }
}
