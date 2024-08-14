package com.example.japanesenamegenerator.config;

import com.example.japanesenamegenerator.common.ratelimit.IpBasedRateLimiter;
import com.example.japanesenamegenerator.common.ratelimit.RateLimitInterceptor;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final Bucket bucket;
    private final IpBasedRateLimiter limiter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor(limiter))
            .addPathPatterns("/api/**");
    }
}