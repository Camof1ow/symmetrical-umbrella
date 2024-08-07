package com.example.japanesenamegenerator.independence.web;

import com.example.japanesenamegenerator.independence.domain.response.ActivistResponse;
import com.example.japanesenamegenerator.independence.service.ActivistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/activists")
public class ActivistApiController {

    private final ActivistService activistService;

    @PostMapping
    public void create() {
        ActivistResponse activistResponse = activistService.fetchApiData();
        System.out.println(activistResponse);
    }
}
