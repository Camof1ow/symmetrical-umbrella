package com.example.japanesenamegenerator.likes;

import com.example.japanesenamegenerator.likes.application.LikeResponseDto;
import com.example.japanesenamegenerator.likes.application.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like")
public class LikeApiController {

    private final LikeService likeService;


    @GetMapping("/count")
    public LikeResponseDto getCount(){
        return likeService.getLikeCount();
    }

    @GetMapping("/up")
    public LikeResponseDto upVote(){
        return likeService.likeUp();
    }

}
