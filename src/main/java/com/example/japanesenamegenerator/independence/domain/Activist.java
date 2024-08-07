package com.example.japanesenamegenerator.independence.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;


@Entity
@Table(name = "activists")
@Getter
public class Activist {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "name_hanja")
    private String nameHanja;

    @Column(name = "movement_family")
    private String movementFamily;

    @Column(name = "order_year")
    private int orderYear;

    @Column(name = "name")
    private String name;

    @Column(name = "address_birth")
    private String addressBirth;

    @Column(name = "aliases")
    private String aliases;

    @Column(name = "born_died")
    private String bornDied;

    @Column(name = "place_of_origin")
    private String placeOfOrigin;

    @Column(name = "references_name")
    private String references_name;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "activities")
    private String activities;

    @Column(name = "engaged_events")
    private String engagedEvents;

    @Column(name = "engaged_organizations")
    private String engagedOrganizations;
}
