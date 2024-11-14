package com.example.japanesenamegenerator.repair.application;

import com.example.japanesenamegenerator.repair.application.request.RepairRequestDto;
import com.example.japanesenamegenerator.repair.application.response.RepairResponseDto;
import com.example.japanesenamegenerator.repair.domain.Repair;
import com.example.japanesenamegenerator.repair.repository.RepairRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class RepairService {

    private final RepairRepository repairRepository;

    @Transactional
    public RepairResponseDto requestRepair(RepairRequestDto repairRequestDto) {
        try {
            Repair repair = repairRequestDto.to();
            repairRepository.save(repair);
            return new RepairResponseDto(true);
        }catch (Exception e){
            return new RepairResponseDto(false);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldRequest() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(48);
        int deletedCount = repairRepository.deleteByCreatedAtBefore(threshold);
        log.info("Deleted {} old repair requests", deletedCount);
    }



}
