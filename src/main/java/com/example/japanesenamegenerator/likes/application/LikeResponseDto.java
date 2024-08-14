package com.example.japanesenamegenerator.likes.application;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeResponseDto {
    private Long likeCount;

    public LikeResponseDto(Long likeCount) {
        this.likeCount = likeCount;
    }
}
