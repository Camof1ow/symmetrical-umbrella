package com.example.japanesenamegenerator.independence.repository;

import com.example.japanesenamegenerator.independence.domain.Activist;
import feign.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivistRepository extends JpaRepository<Activist, Long> {

    @Query(value = "SELECT * FROM (" +
        "  SELECT *, " +
        "    CASE " +
        "      WHEN CONVERT(name USING utf8mb4) = CONVERT(:name USING utf8mb4) THEN 100 " +
        "      WHEN extract_chosung_mjung_jongsung(CONVERT(name USING utf8mb4)) = extract_chosung_mjung_jongsung(CONVERT(:name USING utf8mb4)) THEN 90 " +
        "      WHEN CONVERT(name USING utf8mb4) LIKE CONCAT('%', CONVERT(:name USING utf8mb4), '%') THEN 80 " +
        "      WHEN name_normalized LIKE CONCAT('%', REGEXP_REPLACE(LOWER(TRIM(:name)), '[^가-힣]', ''), '%') THEN 70 " +
        "      ELSE 60 - korean_levenshtein_distance(CONVERT(name USING utf8mb4), CONVERT(:name USING utf8mb4)) * 10 " +
        "    END AS match_score " +
        "  FROM activists " +
        "  WHERE CONVERT(name USING utf8mb4) = CONVERT(:name USING utf8mb4) " +
        "    OR extract_chosung_mjung_jongsung(CONVERT(name USING utf8mb4)) = extract_chosung_mjung_jongsung(CONVERT(:name USING utf8mb4)) " +
        "    OR CONVERT(name USING utf8mb4) LIKE CONCAT('%', CONVERT(:name USING utf8mb4), '%') " +
        "    OR name_normalized LIKE CONCAT('%', REGEXP_REPLACE(LOWER(TRIM(:name)), '[^가-힣]', ''), '%') " +
        "    OR korean_levenshtein_distance(CONVERT(name USING utf8mb4), CONVERT(:name USING utf8mb4)) <= 2 " +
        ") AS search_results " +
        "ORDER BY match_score DESC " +
        "LIMIT 10",
        nativeQuery = true)
    List<Activist> findBySimilarName(@Param("name") String name);

    List<Activist> findByNameContaining(String name);

    @Query(value = "SELECT *, MATCH(name, content) AGAINST(:searchTerm IN BOOLEAN MODE) AS relevance " +
        "FROM activists " +
        "WHERE MATCH(name, content) AGAINST(:searchTerm IN BOOLEAN MODE) " +
        "ORDER BY relevance DESC " +
        "LIMIT 10", nativeQuery = true)
    List<Activist> findByFullTextSearch(@Param("searchTerm") String searchTerm);

    Optional<Activist> findByNameHanja(String nameHanja);
}
