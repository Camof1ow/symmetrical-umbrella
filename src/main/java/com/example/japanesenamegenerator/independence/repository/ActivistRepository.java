package com.example.japanesenamegenerator.independence.repository;

import com.example.japanesenamegenerator.independence.domain.Activist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivistRepository extends JpaRepository<Activist, Long> {

//    SELECT * FROM activists
//    WHERE name_normalized LIKE CONCAT('%', REGEXP_REPLACE(LOWER(TRIM(?)), '[^가-힣]', ''), '%')
//    OR levenshtein_distance(name, ?) <= 2
//    LIMIT 10;
}
