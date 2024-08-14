package com.example.japanesenamegenerator.common.ratelimit;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

public class RateLimitInterceptor implements HandlerInterceptor {

    private final IpBasedRateLimiter rateLimiter;

    public RateLimitInterceptor(IpBasedRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response, Object handler) throws Exception {
        String ip = getClientIp(request);
        Bucket bucket = rateLimiter.resolveBucket(ip);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        }

        long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
        response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
        response.sendError(
            HttpStatus.TOO_MANY_REQUESTS.value(),
            "You have exhausted your API Request Quota"
        );
        return false;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}