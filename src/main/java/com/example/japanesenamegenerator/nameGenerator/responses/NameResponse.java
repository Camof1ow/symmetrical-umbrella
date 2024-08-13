package com.example.japanesenamegenerator.nameGenerator.responses;

import lombok.Getter;

@Getter
public class NameResponse {

    private String surName;
    private String pronounceSurName;
    private String firstName;
    private String pronounceFirstName;
    private int households;

    public NameResponse(String surName, String pronounceSurName, String firstName, String pronounceFirstName, int households) {
        this.surName = surName;
        this.pronounceSurName = pronounceSurName;
        this.firstName = firstName;
        this.pronounceFirstName = pronounceFirstName;
        this.households = households;
    }

    public NameResponse(){}
}
