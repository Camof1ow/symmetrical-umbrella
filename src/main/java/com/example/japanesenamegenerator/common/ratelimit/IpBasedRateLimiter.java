package com.example.japanesenamegenerator.common.ratelimit;

import com.example.japanesenamegenerator.config.ThrottlingConfig;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
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
        return buckets.computeIfAbsent(ip, k -> {
            log.debug("Creating new bucket for IP: {}", ip);
            return throttlingConfig.createNewBucket();
        });
    }

    public boolean tryConsume(String ip) {
        Bucket bucket = resolveBucket(ip);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            log.debug("Request allowed for IP: {}. Remaining tokens: {}", ip, probe.getRemainingTokens());
            return true;
        } else {
            log.debug("Request denied for IP: {}. Need to wait {} seconds", ip, probe.getNanosToWaitForRefill() / 1_000_000_000);
            return false;
        }
    }
}