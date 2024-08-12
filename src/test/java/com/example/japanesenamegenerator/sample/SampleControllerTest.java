package com.example.japanesenamegenerator.sample;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class SampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRateLimiting() throws Exception {
        // 허용된 요청 수만큼 반복
        for (int i = 0; i < 20; i++) {
            ResultActions result = mockMvc.perform(get("/api/test"));
            result.andExpect(status().isOk())
                .andExpect(content().string("Hello, this is a rate-limited API!"))
                .andExpect(header().exists("X-Rate-Limit-Remaining"));
        }

        // 제한을 초과하는 요청
        ResultActions result = mockMvc.perform(get("/api/test"));
        result.andExpect(status().isTooManyRequests())
            .andExpect(header().exists("X-Rate-Limit-Retry-After-Seconds"));
    }

    @Test
    void testSingleRequest() throws Exception {
        mockMvc.perform(get("/api/test"))
            .andExpect(status().isOk())
            .andExpect(content().string("Hello, this is a rate-limited API!"))
            .andExpect(header().exists("X-Rate-Limit-Remaining"));
    }
}