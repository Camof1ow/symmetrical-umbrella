package com.example.japanesenamegenerator.nameGenerator.controller;

import com.example.japanesenamegenerator.nameGenerator.responses.LastNameResponse;
import com.example.japanesenamegenerator.nameGenerator.service.NameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NameController {

    private final NameService nameService;

    @GetMapping("api/name")
    public ResponseEntity<LastNameResponse> generateName(@RequestParam String surName,
                                                         @RequestParam String firstName,
                                                         @RequestParam String gender) {

        return ResponseEntity.ok(nameService.getNameInfo(surName, firstName, gender));
    }



}
