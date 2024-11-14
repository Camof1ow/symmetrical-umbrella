package com.example.japanesenamegenerator.sample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.japanesenamegenerator.config.ThrottlingConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
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

    private static final String TEST_IP = "192.168.1.1";
    private static final int TOTAL_REQUESTS = 10000;
    private static final int THREAD_COUNT = 100;
    private static final int TIMEOUT_MINUTES = 10;

    @BeforeEach
    public void setup() {
        logger.info("테스트 환경 설정");
        // 필요한 설정 코드 추가
    }

    @Test
    @DisplayName("비동기 대량 요청 처리 테스트")
    void testAsyncMassiveRequests() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        AtomicInteger successfulRequests = new AtomicInteger(0);
        AtomicInteger failedRequests = new AtomicInteger(0);
        AtomicInteger ignoredRequests = new AtomicInteger(0);

        List<CompletableFuture<RequestResult>> futures = new ArrayList<>();

        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            CompletableFuture<RequestResult> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        MvcResult result = mockMvc.perform(get("/api/test")
                                .header("X-Forwarded-For", TEST_IP))
                            .andReturn();

                        int status = result.getResponse().getStatus();
                        if (status == 200) {
                            successfulRequests.incrementAndGet();
                            return new RequestResult(RequestStatus.SUCCESS, status);
                        } else if (status == 429) {
                            failedRequests.incrementAndGet();
                            return new RequestResult(RequestStatus.FAILED, status);
                        } else {
                            ignoredRequests.incrementAndGet();
                            return new RequestResult(RequestStatus.IGNORED, status);
                        }
                    } catch (Exception e) {
                        logger.error("요청 처리 중 오류 발생", e);
                        ignoredRequests.incrementAndGet();
                        return new RequestResult(RequestStatus.ERROR, 0);
                    }
                }, executor).orTimeout(1, TimeUnit.SECONDS)
                .exceptionally(ex -> {
                    if (ex instanceof TimeoutException) {
                        logger.warn("요청 시간 초과");
                        ignoredRequests.incrementAndGet();
                        return new RequestResult(RequestStatus.TIMEOUT, 0);
                    } else {
                        logger.error("예상치 못한 오류", ex);
                        ignoredRequests.incrementAndGet();
                        return new RequestResult(RequestStatus.ERROR, 0);
                    }
                });

            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .orTimeout(TIMEOUT_MINUTES, TimeUnit.MINUTES)
            .exceptionally(ex -> {
                logger.error("테스트 시간 초과 또는 오류 발생", ex);
                return null;
            })
            .join();

        executor.shutdown();

        int expectedSuccessfulRequests = throttlingConfig.isPeakHour() ? 50 : 100;

        logger.info("총 요청 수: {}", TOTAL_REQUESTS);
        logger.info("성공한 요청 수: {}", successfulRequests.get());
        logger.info("실패한 요청 수: {}", failedRequests.get());
        logger.info("무시되거나 오류 발생한 요청 수: {}", ignoredRequests.get());

        for (CompletableFuture<RequestResult> future : futures) {
            RequestResult result = future.join();
            if (result.status == RequestStatus.IGNORED || result.status == RequestStatus.ERROR
                || result.status == RequestStatus.TIMEOUT) {
                logger.warn("요청이 무시되거나, 오류 발생, 또는 시간 초과. 상태: {}, HTTP 상태 코드: {}", result.status,
                    result.httpStatus);
            }
        }

        assertEquals(expectedSuccessfulRequests, successfulRequests.get(),
            "예상치 못한 성공 요청 수");
        assertEquals(TOTAL_REQUESTS - expectedSuccessfulRequests - ignoredRequests.get(),
            failedRequests.get(),
            "예상치 못한 실패 요청 수");
        assertTrue(ignoredRequests.get() >= 0, "무시된 요청 수는 음수가 될 수 없습니다");

        if (ignoredRequests.get() > 0) {
            logger.warn("일부 요청이 무시되거나 오류가 발생했습니다: {}", ignoredRequests.get());
        }
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
                .andReturn();

            logger.info("IP 192.168.1.1의 요청 {}", i + 1);
        }

        logger.info("IP1 (192.168.1.1)의 속도 제한 초과 테스트");
        performRequest("192.168.1.1")
            .andExpect(status().isTooManyRequests());

        // IP2 테스트
        logger.info("IP2 (192.168.1.2)에 대한 속도 제한 테스트");
        for (int i = 0; i < 100; i++) {
            MvcResult result = performRequest("192.168.1.2")
                .andExpect(status().isOk())
                .andReturn();

            logger.info("IP 192.168.1.2의 요청 {}", i + 1);
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
                .andReturn();

            logger.info("IP 192.168.1.1의 요청 {}", i + 1);
        }

        logger.info("IP1 (192.168.1.1)의 피크 시간 속도 제한 초과 테스트");
        performRequest("192.168.1.1")
            .andExpect(status().isTooManyRequests());

        // IP2 테스트
        logger.info("IP2 (192.168.1.2)에 대한 피크 시간 속도 제한 테스트");
        for (int i = 0; i < 50; i++) {
            MvcResult result = performRequest("192.168.1.2")
                .andExpect(status().isOk())
                .andReturn();

            logger.info("IP 192.168.1.2의 요청 {}", i + 1);
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

            logger.info("IP 192.168.1.1의 요청 {}", i + 1);
        }

        logger.info("토큰 리필 대기 중 (1분)");
        Thread.sleep(60000);

        // IP1의 토큰 리필 확인
        logger.info("IP1 (192.168.1.1)의 토큰 리필 확인");
        MvcResult result = performRequest("192.168.1.1")
            .andExpect(status().isOk())
            .andReturn();

        logger.info("리필 후 IP1의 요청 성공");

        // IP2는 영향 받지 않음 확인
        logger.info("영향 받지 않은 IP2 (192.168.1.2) 테스트");
        result = performRequest("192.168.1.2")
            .andExpect(status().isOk())
            .andReturn();

        logger.info("IP2의 요청 성공");

        logger.info("속도 제한 토큰 리필 테스트 종료");
    }

    private ResultActions performRequest(String ip) throws Exception {
        return mockMvc.perform(get("/api/test").header("X-Forwarded-For", ip));
    }

    private enum RequestStatus {
        SUCCESS, FAILED, IGNORED, ERROR, TIMEOUT
    }

    private static class RequestResult {

        RequestStatus status;
        int httpStatus;

        RequestResult(RequestStatus status, int httpStatus) {
            this.status = status;
            this.httpStatus = httpStatus;
        }
    }
}