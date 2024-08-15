package com.example.japanesenamegenerator.repair.repository;


import com.example.japanesenamegenerator.repair.domain.Repair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface RepairRepository extends JpaRepository<Repair, Long> {
    int deleteByCreatedAtBefore(LocalDateTime threshold);
}
