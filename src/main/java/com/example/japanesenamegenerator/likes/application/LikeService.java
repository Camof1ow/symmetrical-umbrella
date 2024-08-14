package com.example.japanesenamegenerator.likes.application;

import com.example.japanesenamegenerator.likes.domain.Likes;
import com.example.japanesenamegenerator.likes.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikesRepository likesRepository;


    public LikeResponseDto likeUp(){
        Likes like = likesRepository.findById(1L).get();
        like.setLikeCount(like.getLikeCount() + 1);
        likesRepository.save(like);
        return new LikeResponseDto(like.getLikeCount());


    }

    public LikeResponseDto getLikeCount(){

        Likes like = likesRepository.findById(1L).get();
        return new LikeResponseDto(like.getLikeCount());
    }


}
