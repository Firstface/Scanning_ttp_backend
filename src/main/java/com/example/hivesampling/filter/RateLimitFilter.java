package com.example.hivesampling.filter;

import com.example.hivesampling.config.AppProperties;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements Filter {
    private final AppProperties appProperties;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public RateLimitFilter(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!appProperties.rateLimit.enabled) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String clientIp = getClientIp(httpRequest);

        Bucket bucket = buckets.computeIfAbsent(clientIp, ip -> {
            Bandwidth limit = Bandwidth.classic(appProperties.rateLimit.requestsPerMinute, Refill.greedy(appProperties.rateLimit.requestsPerMinute, Duration.ofMinutes(1)));
            return Bucket.builder().addLimit(limit).build();
        });

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (bucket.tryConsume(1)) {
            long remaining = bucket.getAvailableTokens();
            httpResponse.setHeader("X-RateLimit-Remaining", String.valueOf(remaining));
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(429);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\":\"Rate limit exceeded\"}");
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
