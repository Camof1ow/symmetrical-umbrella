package com.example.japanesenamegenerator.config;

import com.example.japanesenamegenerator.common.ratelimit.IpBasedRateLimiter;
import com.example.japanesenamegenerator.common.ratelimit.RateLimitInterceptor;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final Bucket bucket;
    private final IpBasedRateLimiter limiter;

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
        "classpath:/static/",
        "classpath:/public/",
        "classpath:/", "classpath:/resources/", "classpath:/META-INF/resources/",
        "classpath:/META-INF/resources/webjars/"};

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor(limiter))
            .addPathPatterns("/api/**");
    }
}