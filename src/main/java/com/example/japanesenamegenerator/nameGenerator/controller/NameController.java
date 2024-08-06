package com.example.japanesenamegenerator.nameGenerator.controller;

import com.example.japanesenamegenerator.nameGenerator.responses.LastNameResponse;
import com.example.japanesenamegenerator.nameGenerator.service.NameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NameController {

    private final NameService nameService;

    @GetMapping("api/name/{surName}-{firstName}-{gender}")
    public ResponseEntity<LastNameResponse> generateName(@PathVariable("surName") String surName,
                                                         @PathVariable("firstName") String firstName,
                                                         @PathVariable String gender) {

        return ResponseEntity.ok(nameService.generateName(surName, firstName, gender));
    }



}
