package com.example.japanesenamegenerator.likes.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "likes")
public class Likes {

    @Id
    private Long id;
    @Column(name = "like_count")
    private Long likeCount;
}
