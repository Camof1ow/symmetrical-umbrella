package com.example.japanesenamegenerator.independence.application;

import com.example.japanesenamegenerator.independence.application.request.ActivistImageUpdateRequest;
import com.example.japanesenamegenerator.independence.application.response.ActivistOpenApiResponse;
import com.example.japanesenamegenerator.independence.application.response.ActivistResponse;
import com.example.japanesenamegenerator.independence.application.response.FamilyKeysAndPageCount;
import com.example.japanesenamegenerator.independence.client.ActivistClient;
import com.example.japanesenamegenerator.independence.domain.Activist;
import com.example.japanesenamegenerator.independence.repository.ActivistRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.transaction.Transactional;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
    private static final String IMAGE_DIRECTORY = "src/main/resources/static/images/activists/";

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

    @Transactional
    public List<ActivistResponse> findSameOrSimilarName(String name) {
        List<List<Activist>> searchResults = Stream.of(
                activistRepository.findTop10ByNameContaining(name),
                activistRepository.findBySimilarName(name),
                activistRepository.findByFullTextSearch(name)
            )
            .filter(results -> !ObjectUtils.isEmpty(results))
            .toList();

        if (searchResults.isEmpty()) {
            log.error("No activists found with name: {}", name);
            return Collections.emptyList();
        }

        List<Activist> results = searchResults.getFirst();
        log.info("[search] search name : '{}', response names: {}",
            name, results.stream().map(Activist::getName).toList());

        if(results.size() > 10){
            results = results.subList(0, 9);
        }

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

    @Transactional
    public boolean updateImages() {
        try {
            Path path = Files.createDirectories(Paths.get(IMAGE_DIRECTORY));
            log.info("Directory created: {}", path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<ActivistImageUpdateRequest> requests = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\((.*?)\\)");

        //513
        IntStream.range(1, 513).forEach(i -> {
            log.info("Processing page: '{}'", i);
            String url = String.format("https://search.i815.or.kr/dictionary/moreSearchResult.do?index=%d"
                + "&pageNum=20&searchWord=&reSearchWord=&inMyeongOrder=&birthPlace=&movementFamily=&isForeigner=", i);

            try {
                Document doc = Jsoup.connect(url).get();

                doc.select("li.thumb_txt_case01")
                    .stream()
                    .filter(li -> !li.select("div.thumb > img").attr("src").equals("/media/"))
                    .forEach(li -> {
                        String src = li.select("div.thumb > img").attr("src");
                        String fullName = li.select(".dict_txt_title").text();
                        Matcher matcher = pattern.matcher(fullName);

                        if (matcher.find()) {
                            String chineseName = matcher.group(1);

                            requests.add(new ActivistImageUpdateRequest(chineseName, src));
                        }
                    });
            } catch (IOException e) {
                log.error("Error processing page {}: {}", i, e.getMessage());
            }
        });

        try{
            for (ActivistImageUpdateRequest request : requests) {
                if(request == null){
                    log.error("Request is null");
                    continue;
                }
                String imagePath = downloadImage(request.getImageUrl());
                log.info("Processing request: '{}' :: '{}'", request.getName(), imagePath);
                updateDatabase(request.getName(), imagePath);
            }
        } catch (Exception e){
            log.error("Error processing request: {}", e.getMessage());
            e.printStackTrace();
        }

        return true;
    }
    private String downloadImage(String imageUrl) {
        String fileName = UUID.randomUUID() + ".jpg";
        Path path = Paths.get(IMAGE_DIRECTORY + fileName);

        try {
            String targetUrl = "https://search.i815.or.kr" + imageUrl;
            URL url = new URL(targetUrl);

            try (InputStream in = url.openStream();
                ReadableByteChannel rbc = Channels.newChannel(in);
                FileOutputStream fos = new FileOutputStream(path.toFile())) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }
            return fileName;
        } catch (IOException e) {
            log.error("Error downloading image: {}", e.getMessage());
            return null;
        }
    }

    private void updateDatabase(String chineseName, String imagePath) {
        if (imagePath != null) {
            activistRepository.findByNameHanja(chineseName)
                .ifPresent(activist -> {
                    //유니크한 값이 아니라서 여러명이 나올 수 있음. 누군지 모르지 일단 업데이트 하지 않음
                    if(activist.size() > 1){
                        return;
                    }
                    Activist first = activist.getFirst();
                    first.setImagePath(imagePath);
                    activistRepository.save(first);
                });
        }
    }
}

