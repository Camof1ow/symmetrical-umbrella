package com.example.japanesenamegenerator.common.ratelimit;

import com.example.japanesenamegenerator.config.ThrottlingConfig;
import io.github.bucket4j.Bucket;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IpBasedRateLimiter {
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final ThrottlingConfig throttlingConfig;

    public IpBasedRateLimiter(ThrottlingConfig throttlingConfig) {
        this.throttlingConfig = throttlingConfig;
    }

    public Bucket resolveBucket(String ip) {
        log.debug("Resolving bucket for IP: {}", ip);
        return buckets.computeIfAbsent(ip, k -> throttlingConfig.createNewBucket());
    }
}