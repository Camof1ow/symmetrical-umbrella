package com.example.japanesenamegenerator.repair.web;

import com.example.japanesenamegenerator.repair.application.RepairService;
import com.example.japanesenamegenerator.repair.application.request.RepairRequestDto;
import com.example.japanesenamegenerator.repair.application.response.RepairResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/repair")
public class RepairController {
    private final RepairService repairService;

    @PostMapping("")
    public RepairResponseDto repairRequest(@RequestBody RepairRequestDto repairRequestDto) {
        return repairService.requestRepair(repairRequestDto);
    }

}
