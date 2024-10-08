package com.example.japanesenamegenerator.nameGenerator.responses;

import lombok.Getter;

@Getter
public class NameResponse {

    private String surName;
    private String pronounceSurName;
    private String firstName;
    private String pronounceFirstName;
    private int households;
    private String eg;

    public NameResponse(String surName,
                        String pronounceSurName,
                        String firstName,
                        String pronounceFirstName,
                        int households,
                        String eg) {
        this.surName = surName;
        this.pronounceSurName = pronounceSurName;
        this.firstName = firstName;
        this.pronounceFirstName = pronounceFirstName;
        this.households = households;
        this.eg = eg;
    }

    public NameResponse(){}

    @Override
    public String toString() {
        return "{" +
                "성씨='" + surName + '\'' +
                ", 성씨 발음='" + pronounceSurName + '\'' +
                ", 이름='" + firstName + '\'' +
                ", 이름 발음='" + pronounceFirstName + '\'' +
                ", 가구 수=" + households +
                ", 정보='" + eg + '\'' +
                '}';
    }
}
