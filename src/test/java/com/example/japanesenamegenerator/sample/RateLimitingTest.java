package com.example.japanesenamegenerator.sample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.japanesenamegenerator.config.ThrottlingConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;


@SpringBootTest
@AutoConfigureMockMvc
public class RateLimitingTest {
    private static final Logger logger = LoggerFactory.getLogger(RateLimitingTest.class);

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private ThrottlingConfig throttlingConfig;

    @BeforeEach
    public void setup() {
        logger.info("Setting up test environment");
        // Add any necessary setup code here
    }

    @Test
    @DisplayName("다중 IP 속도 제한 테스트")
    public void testMultipleIpRateLimit() throws Exception {
        logger.info("다중 IP 속도 제한 테스트 시작");

        // IP1 테스트
        logger.info("IP1 (192.168.1.1)에 대한 속도 제한 테스트");
        for (int i = 0; i < 100; i++) {
            MvcResult result = performRequest("192.168.1.1")
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Rate-Limit-Remaining"))
                .andReturn();

            String remainingHeader = result.getResponse().getHeader("X-Rate-Limit-Remaining");
            logger.info("IP 192.168.1.1의 요청 {} - 남은 요청 수: {}", i + 1, remainingHeader);
            assertEquals(String.valueOf(99 - i), remainingHeader, "IP1의 예상치 못한 남은 요청 수");
        }

        logger.info("IP1 (192.168.1.1)의 속도 제한 초과 테스트");
        MvcResult result = performRequest("192.168.1.1")
            .andExpect(status().isTooManyRequests())
            .andExpect(header().exists("X-Rate-Limit-Retry-After-Seconds"))
            .andReturn();

        String retryAfterHeader = result.getResponse().getHeader("X-Rate-Limit-Retry-After-Seconds");
        logger.info("IP 192.168.1.1의 속도 제한 초과 - {}초 후 재시도", retryAfterHeader);
        assertNotNull(retryAfterHeader, "Retry-After 헤더가 존재해야 함");

        // IP2 테스트
        logger.info("IP2 (192.168.1.2)에 대한 속도 제한 테스트");
        for (int i = 0; i < 100; i++) {
            result = performRequest("192.168.1.2")
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Rate-Limit-Remaining"))
                .andReturn();

            String remainingHeader = result.getResponse().getHeader("X-Rate-Limit-Remaining");
            logger.info("IP 192.168.1.2의 요청 {} - 남은 요청 수: {}", i + 1, remainingHeader);
            assertEquals(String.valueOf(99 - i), remainingHeader, "IP2의 예상치 못한 남은 요청 수");
        }

        logger.info("다중 IP 속도 제한 테스트 종료");
    }

    @Test
    @DisplayName("피크 시간 속도 제한 테스트")
    public void testPeakHourRateLimit() throws Exception {
        logger.info("피크 시간 속도 제한 테스트 시작");

        // 피크 시간 모의 설정
        when(throttlingConfig.isPeakHour()).thenReturn(true);
        logger.info("피크 시간 조건 모의 설정 완료");

        // IP1 테스트
        logger.info("IP1 (192.168.1.1)에 대한 피크 시간 속도 제한 테스트");
        for (int i = 0; i < 50; i++) {
            MvcResult result = performRequest("192.168.1.1")
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Rate-Limit-Remaining"))
                .andReturn();

            String remainingHeader = result.getResponse().getHeader("X-Rate-Limit-Remaining");
            logger.info("IP 192.168.1.1의 요청 {} - 남은 요청 수: {}", i + 1, remainingHeader);
            assertEquals(String.valueOf(49 - i), remainingHeader, "피크 시간 동안 IP1의 예상치 못한 남은 요청 수");
        }

        logger.info("IP1 (192.168.1.1)의 피크 시간 속도 제한 초과 테스트");
        performRequest("192.168.1.1")
            .andExpect(status().isTooManyRequests())
            .andExpect(header().exists("X-Rate-Limit-Retry-After-Seconds"));

        // IP2 테스트
        logger.info("IP2 (192.168.1.2)에 대한 피크 시간 속도 제한 테스트");
        for (int i = 0; i < 50; i++) {
            MvcResult result = performRequest("192.168.1.2")
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Rate-Limit-Remaining"))
                .andReturn();

            String remainingHeader = result.getResponse().getHeader("X-Rate-Limit-Remaining");
            logger.info("IP 192.168.1.2의 요청 {} - 남은 요청 수: {}", i + 1, remainingHeader);
            assertEquals(String.valueOf(49 - i), remainingHeader, "피크 시간 동안 IP2의 예상치 못한 남은 요청 수");
        }

        logger.info("피크 시간 속도 제한 테스트 종료");
    }

    @Test
    @DisplayName("속도 제한 토큰 리필 테스트")
    public void testRateLimitRefill() throws Exception {
        logger.info("속도 제한 토큰 리필 테스트 시작");

        // 비 피크 시간 모의 설정
        when(throttlingConfig.isPeakHour()).thenReturn(false);
        logger.info("비 피크 시간 조건 모의 설정 완료");

        // IP1의 모든 토큰 소비
        logger.info("IP1 (192.168.1.1)의 모든 토큰 소비");
        for (int i = 0; i < 100; i++) {
            MvcResult result = performRequest("192.168.1.1")
                .andExpect(status().isOk())
                .andReturn();

            String remainingHeader = result.getResponse().getHeader("X-Rate-Limit-Remaining");
            logger.info("IP 192.168.1.1의 요청 {} - 남은 요청 수: {}", i + 1, remainingHeader);
        }

        logger.info("토큰 리필 대기 중 (1분)");
        Thread.sleep(60000);

        // IP1의 토큰 리필 확인
        logger.info("IP1 (192.168.1.1)의 토큰 리필 확인");
        MvcResult result = performRequest("192.168.1.1")
            .andExpect(status().isOk())
            .andExpect(header().exists("X-Rate-Limit-Remaining"))
            .andReturn();

        String remainingHeader = result.getResponse().getHeader("X-Rate-Limit-Remaining");
        logger.info("리필 후 IP1의 남은 토큰 수: {}", remainingHeader);
        assertEquals("99", remainingHeader, "IP1의 리필 후 예상치 못한 남은 요청 수");

        // IP2는 영향 받지 않음 확인
        logger.info("영향 받지 않은 IP2 (192.168.1.2) 테스트");
        result = performRequest("192.168.1.2")
            .andExpect(status().isOk())
            .andExpect(header().exists("X-Rate-Limit-Remaining"))
            .andReturn();

        remainingHeader = result.getResponse().getHeader("X-Rate-Limit-Remaining");
        logger.info("IP2의 남은 토큰 수: {}", remainingHeader);
        assertEquals("99", remainingHeader, "IP2의 예상치 못한 남은 요청 수");

        logger.info("속도 제한 토큰 리필 테스트 종료");
    }

    private ResultActions performRequest(String ip) throws Exception {
        return mockMvc.perform(get("/api/test").header("X-Forwarded-For", ip));
    }
}