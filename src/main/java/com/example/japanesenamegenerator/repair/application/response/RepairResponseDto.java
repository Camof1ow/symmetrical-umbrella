package com.example.japanesenamegenerator.repair.application.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepairResponseDto {

    boolean success;

    public RepairResponseDto(boolean success) {
        this.success = success;
    }
}
