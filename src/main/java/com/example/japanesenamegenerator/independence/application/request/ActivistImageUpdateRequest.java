package com.example.japanesenamegenerator.independence.application.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ActivistImageUpdateRequest {

    private String name;
    private String imageUrl;

    public ActivistImageUpdateRequest(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public ActivistImageUpdateRequest of(String name, String imageUrl) {
        return new ActivistImageUpdateRequest(name, imageUrl);
    }
}
