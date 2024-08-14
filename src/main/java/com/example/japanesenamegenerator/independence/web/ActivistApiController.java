package com.example.japanesenamegenerator.independence.web;

import com.example.japanesenamegenerator.independence.application.response.ActivistResponse;
import com.example.japanesenamegenerator.independence.domain.Activist;
import com.example.japanesenamegenerator.independence.application.response.ActivistOpenApiResponse;
import com.example.japanesenamegenerator.independence.application.ActivistService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/activists")
public class ActivistApiController {

    private final ActivistService activistService;

    @DeleteMapping
    public void delete() {
        activistService.deleteAll();
    }

    @PostMapping
    public void create() {

        List<ActivistOpenApiResponse> activistOpenApiRespons = activistService.fetchApiData();
        log.info("Activist responses: {}", activistOpenApiRespons);

        List<Activist> entities = activistService.convert(activistOpenApiRespons);
        log.info("Activist entities: {}", entities);

        activistService.saveAll(entities);
    }

    @PostMapping("/images")
    public boolean updateImages(){
        return activistService.updateImages();
    }

    @GetMapping
    public List<ActivistResponse> get(@RequestParam String name){
        return activistService.findSameOrSimilarName(name);
    }


}
