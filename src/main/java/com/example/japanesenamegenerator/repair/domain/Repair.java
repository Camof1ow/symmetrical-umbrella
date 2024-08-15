package com.example.japanesenamegenerator.repair.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "repair")
public class Repair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "page")
    @Enumerated(EnumType.STRING)
    PageEnum page;
    @Column(name = "request_data")
    String requestData;
    @Column(name = "request_message")
    String requestMessage;
    @Column(name = "created_at")
    LocalDateTime createdAt;
}
