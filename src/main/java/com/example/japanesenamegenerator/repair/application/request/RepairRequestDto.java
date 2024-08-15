package com.example.japanesenamegenerator.repair.application.request;
import com.example.japanesenamegenerator.repair.domain.PageEnum;
import com.example.japanesenamegenerator.repair.domain.Repair;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RepairRequestDto
{
    PageEnum page;
    String requestData;
    String requestMessage;
    LocalDateTime createdAt;

    public Repair to(){
        Repair repair = new Repair();
        repair.setCreatedAt(createdAt);
        repair.setPage(page);
        repair.setRequestData(requestData);
        repair.setRequestMessage(requestMessage);
        return repair;
    }
}
