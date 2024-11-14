package com.example.japanesenamegenerator.independence.application.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ActivistResponse {
    private int id;
    private String nameHanja;
    private String movementFamily;
    private int orderYear;
    private String name;
    private String addressBirth;
    private String aliases;
    private String bornDied;
    private String placeOfOrigin;
    private String referencesName;
    private String content;
    private String activities;
    private String engagedEvents;
    private String engagedOrganizations;
    private String imagePath;

    public String getContent() {
        content = content.replace("\n","\n\n");
        return content;
    }
}
