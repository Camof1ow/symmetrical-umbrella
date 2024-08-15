package com.example.japanesenamegenerator.independence.repository;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.japanesenamegenerator.independence.domain.Activist;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ActivistRepositoryPerformanceTest {

    @Autowired
    private ActivistRepository activistRepository;

    private static String name = "";

    @BeforeEach
    void setup() {
        name = "강용호";
    }

    @Test
    void testFindBySimilarNamePerformance() {
        long startTime = System.currentTimeMillis();

        List<Activist> results = activistRepository.findBySimilarName(name);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("findBySimilarName 실행 시간: " + duration + "ms");
        System.out.println("결과 수: " + results.size());
        System.out.println("검색이름: " + name);
        System.out.println("결과: " + results.stream().map(Activist::getName).toList());

        assertNotNull(results);
        assertTrue(results.size() <= 10);
    }

    @Test
    void testFindTop10ByNameContainingPerformance() {
        long startTime = System.currentTimeMillis();

        List<Activist> results = activistRepository.findTop10ByNameContaining(name);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("findTop10ByNameContaining 실행 시간: " + duration + "ms");
        System.out.println("결과 수: " + results.size());
        System.out.println("검색이름: " + name);
        System.out.println("결과: " + results.stream().map(Activist::getName).toList());

        assertNotNull(results);
        assertTrue(results.size() <= 10);
    }

    @Test
    void testFindByFullTextSearchPerformance() {
        long startTime = System.currentTimeMillis();

        List<Activist> results = activistRepository.findByFullTextSearch(name);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("findByFullTextSearch 실행 시간: " + duration + "ms");
        System.out.println("결과 수: " + results.size());
        System.out.println("검색이름: " + name);
        System.out.println("결과: " + results.stream().map(Activist::getName).toList());

        assertNotNull(results);
        assertTrue(results.size() <= 10);
    }
//
//    @Test
//    void testFindByNameHanjaPerformance() {
//        long startTime = System.currentTimeMillis();
//
//        Optional<List<Activist>> results = activistRepository.findByNameHanja("洪吉童");
//
//        long endTime = System.currentTimeMillis();
//        long duration = endTime - startTime;
//
//        System.out.println("findByNameHanja 실행 시간: " + duration + "ms");
//        System.out.println("결과 존재 여부: " + results.isPresent());
//
//        assertTrue(results.isPresent());
//    }
}