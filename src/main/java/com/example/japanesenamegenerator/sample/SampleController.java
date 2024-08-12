package com.example.japanesenamegenerator.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("/api/test")
    public String test() {
        return "Hello, this is a rate-limited API!";
    }
}