package com.example.japanesenamegenerator.common.ratelimit;

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

        if (rateLimiter.tryConsume(ip)) {
            return true;
        }

        response.sendError(
            HttpStatus.TOO_MANY_REQUESTS.value(),
            "You have exhausted your API Request Quota"
        );
        return false;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}