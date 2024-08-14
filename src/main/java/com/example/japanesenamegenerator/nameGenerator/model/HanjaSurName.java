package com.example.japanesenamegenerator.nameGenerator.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class HanjaSurName {

    private String koreanHanja;
    private String pronounce;
    private String japaneseNameHanja;
    private String example;
}
