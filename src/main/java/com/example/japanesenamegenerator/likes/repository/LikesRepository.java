package com.example.japanesenamegenerator.likes.repository;


import com.example.japanesenamegenerator.likes.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
}
