package com.example.japanesenamegenerator.nameGenerator.controller;

import com.example.japanesenamegenerator.nameGenerator.responses.NameResponse;
import com.example.japanesenamegenerator.nameGenerator.service.NameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/name")
public class NameController {

    private final NameService nameService;

    @GetMapping
    public NameResponse generateName(@RequestParam String firstName,
                                         @RequestParam String lastName,
                                         @RequestParam String gender) {

        return nameService.getNameInfo(lastName,firstName, gender);
    }

}
