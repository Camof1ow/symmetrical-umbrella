package com.example.japanesenamegenerator.independence.web;

import com.example.japanesenamegenerator.independence.domain.Activist;
import com.example.japanesenamegenerator.independence.domain.response.ActivistResponse;
import com.example.japanesenamegenerator.independence.service.ActivistService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/activists")
public class ActivistApiController {

    private final ActivistService activistService;

    @PostMapping
    public void create() {

        List<ActivistResponse> activistResponses = activistService.fetchApiData();
        log.info("Activist responses: {}", activistResponses);

        List<Activist> entities = activistService.convert(activistResponses);
        log.info("Activist entities: {}", entities);

        activistService.saveAll(entities);
    }
}
