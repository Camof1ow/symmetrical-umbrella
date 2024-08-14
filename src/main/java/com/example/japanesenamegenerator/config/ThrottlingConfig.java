package com.example.japanesenamegenerator.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import java.time.Duration;
import java.time.LocalTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThrottlingConfig {

    public Bucket createNewBucket() {
        return Bucket.builder()
            .addLimit(
                Bandwidth.classic(getCapacity(),
                    Refill.intervally(getRefillTokens(), Duration.ofMinutes(1)))
            ).build();
    }

    @Bean
    public Bucket dynamicBucket() {
        return Bucket.builder()
            .addLimit(
                Bandwidth.classic(getCapacity(),
                Refill.intervally(getRefillTokens(), Duration.ofMinutes(1)))
            ).build();
    }

    private long getCapacity() {
        return isPeakHour() ? 50 : 100;
    }

    private long getRefillTokens() {
        return isPeakHour() ? 50 : 100;
    }

    public boolean isPeakHour() {
        int hour = LocalTime.now().getHour();
        return hour >= 9 && hour < 17;  // 9AM to 5PM를 피크 시간으로 가정
    }
}