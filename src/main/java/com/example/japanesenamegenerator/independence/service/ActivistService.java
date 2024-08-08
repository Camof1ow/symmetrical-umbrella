package com.example.japanesenamegenerator.independence.service;

import com.example.japanesenamegenerator.independence.client.ActivistClient;
import com.example.japanesenamegenerator.independence.domain.response.ActivistResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivistService {

    private final ActivistClient activistClient;

    public ActivistResponse fetchApiData() {
        return activistClient.getApiResponse("4", "AA", 1);
    }
}
