package com.example.japanesenamegenerator.nameGenerator.repository;

import com.example.japanesenamegenerator.nameGenerator.model.HanjaName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HanjaNameRepository extends JpaRepository<HanjaName, Long> {

    List<HanjaName> findByKoreanHanja(String koreanHanja);
}
