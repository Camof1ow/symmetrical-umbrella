package com.example.japanesenamegenerator.common.advisor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RestControllerAdvice
public class DefaultResponseHandler implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    public DefaultResponseHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
        MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
        ServerHttpRequest request, ServerHttpResponse response) {

        if (body instanceof String) {
            return handleStringResponse(body);
        }

        if (body instanceof ResponseEntity<?>) {
            return body; // ResponseEntity는 이미 처리된 것으로 간주
        }

        return createResponseData(body);
    }

    private String handleStringResponse(Object body) {
        try {
            return objectMapper.writeValueAsString(createResponseData(body));
        } catch (JsonProcessingException e) {
            log.error("Error converting response to JSON", e);
            throw new RuntimeException("Error converting response to JSON", e);
        }
    }

    private Map<String, Object> createResponseData(Object body) {
        return Map.of(
            "code", 1,
            "message", "success",
            "contents", body
        );
    }
}