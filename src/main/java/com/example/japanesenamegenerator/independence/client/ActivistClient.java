package com.example.japanesenamegenerator.independence.client;

import com.example.japanesenamegenerator.common.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "activistClient", url = "https://search.i815.or.kr", configuration = FeignConfig.class)
public interface ActivistClient {

    @GetMapping(value = "/openApiData.do")
    String getApiResponse(
        @RequestParam("type") String type,
        @RequestParam("movementFamily") String movementFamily,
        @RequestParam("page") int page);
}
