package com.example.japanesenamegenerator.independence.service;

import com.example.japanesenamegenerator.independence.client.ActivistClient;
import com.example.japanesenamegenerator.independence.domain.response.ActivistResponse;
import com.example.japanesenamegenerator.independence.domain.response.FamilyKeysAndPageCount;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivistService {

    private final ActivistClient activistClient;
    private static final List<String> movementFamilyKeys = List.of(
        "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK",
        "AL", "AM", "AN", "AO","AP", "AQ", "AR", "AS", "AT","AU"
    );

    public ActivistResponse fetchApiData() {
        List<FamilyKeysAndPageCount> familyKeysAndPageCounts = new ArrayList<>();
        for (String key : movementFamilyKeys) {
            String response = activistClient.getApiResponse("4", key, 1);
            log.info("key and response: '{}', '{}'", key, response);

            ActivistResponse activist = parseXmlResponse(response);
            if (activist != null) {
                familyKeysAndPageCounts.add(new FamilyKeysAndPageCount(key, activist.getPageCount()));
            }
        }

        System.out.println(familyKeysAndPageCounts);
        return null;
    }

    private ActivistResponse parseXmlResponse(String response) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            return xmlMapper.readValue(response, ActivistResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
