package com.example.japanesenamegenerator.independence.application;

import com.example.japanesenamegenerator.independence.application.response.ActivistResponse;
import com.example.japanesenamegenerator.independence.client.ActivistClient;
import com.example.japanesenamegenerator.independence.domain.Activist;
import com.example.japanesenamegenerator.independence.application.response.ActivistOpenApiResponse;
import com.example.japanesenamegenerator.independence.application.response.FamilyKeysAndPageCount;
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

    public List<ActivistOpenApiResponse> fetchApiData() {
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
        ActivistOpenApiResponse activistOpenApiResponse = parseXmlResponse(response);
        if (Objects.isNull(activistOpenApiResponse)) {
            log.error("Failed to parse. key: '{}'", key);
            return null;
        }
        return new FamilyKeysAndPageCount(key, activistOpenApiResponse.getPageCount());
    }

    private Stream<ActivistOpenApiResponse> fetchAllPages(FamilyKeysAndPageCount keysAndPageCount) {
        return IntStream.rangeClosed(1, keysAndPageCount.getPageCount())
            .mapToObj(page -> fetchPage(keysAndPageCount.getFamilyKey(), page))
            .filter(Objects::nonNull);
    }

    private ActivistOpenApiResponse fetchPage(String key, int page) {
        String response = activistClient.getApiResponse("4", key, page);
        if (Objects.isNull(response)) {
            log.error("Failed to get. key: '{}', page: '{}'", key, page);
            return null;
        }
        ActivistOpenApiResponse activistOpenApiResponse = parseXmlResponse(response);
        if (Objects.isNull(activistOpenApiResponse)) {
            log.error("Failed to parse. key: '{}', page: '{}'", key, page);
            return null;
        }
        return activistOpenApiResponse;
    }

    private ActivistOpenApiResponse parseXmlResponse(String xmlString) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ActivistOpenApiResponse response = xmlMapper.readValue(xmlString, ActivistOpenApiResponse.class);
            log.debug("Parsed response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error parsing XML: {}", e.getMessage(), e);
            log.debug("error XML: {}", xmlString);
            return null;
        }
    }

    public List<Activist> convert(List<ActivistOpenApiResponse> responses) {
        return responses.stream()
            .filter(x -> !ObjectUtils.isEmpty(x.getItems()))
            .flatMap(response -> response.getItems().stream())
            .map(this::convertToEntity)
            .toList();
    }

    private Activist convertToEntity(ActivistOpenApiResponse.DataItem dataItem) {
        return modelMapper.map(dataItem, Activist.class);
    }

    public void saveAll(List<Activist> entities) {
        activistRepository.saveAll(entities);
    }

    public List<ActivistResponse> findSameOrSimilarName(String name) {
        List<List<Activist>> searchResults = Stream.of(
                activistRepository.findByNameContaining(name),
                activistRepository.findBySimilarName(name),
                activistRepository.findByFullTextSearch(name)
            )
            .filter(results -> !ObjectUtils.isEmpty(results))
            .toList();

        if (searchResults.isEmpty()) {
            return null;
        }

        List<Activist> results = searchResults.getFirst();
        log.info("[search] search name : '{}', response names: {}",
            name, results.stream().map(Activist::getName).toList());

        return proceedActivists(results);
    }

    private List<ActivistResponse> proceedActivists(List<Activist> results) {
        return results.stream()
            .map(activist -> modelMapper.map(activist, ActivistResponse.class))
            .toList();
    }

    public void deleteAll() {
        activistRepository.deleteAll();
    }
}

