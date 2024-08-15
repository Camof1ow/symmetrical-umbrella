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
    private PageEnum page;
    private String requestData;

    public Repair to(){
        Repair repair = new Repair();
        repair.setPage(page);
        repair.setRequestData(requestData);
        repair.setRequestMessage(requestMessage);
        return repair;
    }

    String requestMessage;
}
