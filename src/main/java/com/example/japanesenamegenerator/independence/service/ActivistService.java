package com.example.japanesenamegenerator.independence.service;

import com.example.japanesenamegenerator.independence.client.ActivistClient;
import com.example.japanesenamegenerator.independence.domain.Activist;
import com.example.japanesenamegenerator.independence.domain.response.ActivistResponse;
import com.example.japanesenamegenerator.independence.domain.response.FamilyKeysAndPageCount;
import com.example.japanesenamegenerator.independence.repository.ActivistRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivistService {

    private final ActivistClient activistClient;
    private final ActivistRepository activistRepository;
    private final ModelMapper modelMapper;

    private static final List<String> movementFamilyKeys = List.of(
        "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK",
        "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU"
    );

    public List<ActivistResponse> fetchApiData() {
        return movementFamilyKeys.stream()
            .map(this::getFamilyKeyAndPageCount)
            .filter(Objects::nonNull)
            .flatMap(this::fetchAllPages)
            .filter(Objects::nonNull)
            .toList();
    }

    private FamilyKeysAndPageCount getFamilyKeyAndPageCount(String key) {
        String response = activistClient.getApiResponse("4", key, 1);
        if (Objects.isNull(response)) {
            log.error("Failed to get. key: '{}'", key);
            return null;
        }
        ActivistResponse activistResponse = parseXmlResponse(response);
        if (Objects.isNull(activistResponse)) {
            log.error("Failed to parse. key: '{}'", key);
            return null;
        }
        return new FamilyKeysAndPageCount(key, activistResponse.getPageCount());
    }

    private Stream<ActivistResponse> fetchAllPages(FamilyKeysAndPageCount keysAndPageCount) {
        return IntStream.rangeClosed(1, keysAndPageCount.getPageCount())
            .mapToObj(page -> fetchPage(keysAndPageCount.getFamilyKey(), page))
            .filter(Objects::nonNull);
    }

    private ActivistResponse fetchPage(String key, int page) {
        String response = activistClient.getApiResponse("4", key, page);
        if (Objects.isNull(response)) {
            log.error("Failed to get. key: '{}', page: '{}'", key, page);
            return null;
        }
        ActivistResponse activistResponse = parseXmlResponse(response);
        if (Objects.isNull(activistResponse)) {
            log.error("Failed to parse. key: '{}', page: '{}'", key, page);
            return null;
        }
        return activistResponse;
    }

    private ActivistResponse parseXmlResponse(String xmlString) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ActivistResponse response = xmlMapper.readValue(xmlString, ActivistResponse.class);
            log.debug("Parsed response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error parsing XML: {}", e.getMessage(), e);
            log.debug("error XML: {}", xmlString);
            return null;
        }
    }

    public List<Activist> convert(List<ActivistResponse> responses) {
        return responses.stream()
            .filter(x-> !ObjectUtils.isEmpty(x.getItems()))
            .flatMap(response -> response.getItems().stream())
            .map(this::convertToEntity)
            .toList();
    }
    private Activist convertToEntity(ActivistResponse.DataItem dataItem) {
        return modelMapper.map(dataItem, Activist.class);
    }

    public void saveAll(List<Activist> entities) {
        activistRepository.saveAll(entities);
    }
}