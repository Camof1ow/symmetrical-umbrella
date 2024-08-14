package com.example.japanesenamegenerator.nameGenerator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "hanja_name")
public class HanjaName {

    @Id
    private Long id;
    @Column(name = "korean_hanja")
    private String koreanHanja;
    @Column(name = "pronounce")
    private String pronounce;
    @Column(name = "japanese_hanja")
    private String japaneseHanja;
    @Column(name = "example")
    private String example;

}
