package com.example.japanesenamegenerator.nameGenerator.service;

import com.example.japanesenamegenerator.nameGenerator.responses.NameResponse;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class NameService {

    private static final String NAME_CONVERTER_URL = "http://www.hipenpal.com/tool/japanese_old_kanji_characters_to_new_converter_in_korean.php";
    private static final String LAST_NAME_URL_TEMPLATE = "https://japanese-names.info/last-names/search-result/freeword-%s/";
    private static final String FIRST_NAME_URL_TEMPLATE = "https://japanese-names.info/first-names/search-result/%sfreeword-%s/";

    public NameResponse getNameInfo(String surName, String firstName, String gender) {
        String convertedName = nameConvert(String.format("%s_%s", surName, firstName));
        String[] nameParts = convertedName.split("_");
        surName = nameParts[0];
        firstName = nameParts[1];

        if (gender.equals("male")) gender = "boy";
        else gender = "girl";

        //이름
        List<String> firstNameList = Arrays.asList(firstName.split(""));
        List<String> firstNameUrls = createFirstNameUrls(firstNameList, gender);

        List<Element> firstNames = fetchElementsFromUrls(firstNameUrls);
        Element randomFirstName = getRandomElement(firstNames);

        String japaneseFirstName = getElementText(randomFirstName, "strong");
        String fnPronouceChunk = randomFirstName.getElementsByAttributeStarting("href").getFirst().toString()
                .replace("<a href=\"https://japanese-names.info/first-name/", "");

        String surNameUrl = String.format(LAST_NAME_URL_TEMPLATE, surName);
        Element lastNameElement = fetchElementFromUrl(surNameUrl);

        int households = parseHouseholds(lastNameElement);
        String japaneseLastName = getElementText(lastNameElement, "strong");
        String lnPronouceChunk = lastNameElement.getElementsByAttributeStarting("href").getFirst().toString()
                .replace("<a href=\"/last-name/", "");

        String firstNamePronouce = fnPronouceChunk.substring(0,fnPronouceChunk.indexOf("/"));
        String lastNamePronounce = lnPronouceChunk.substring(0,lnPronouceChunk.indexOf("/"));

        return new NameResponse(japaneseFirstName, firstNamePronouce,japaneseLastName, lastNamePronounce, households);
    }

    private List<String> createFirstNameUrls(List<String> firstNameList, String gender) {
        String genderParam = (gender == null) ? "" : String.format("gender-%s_", gender);
        List<String> urls = new ArrayList<>();
        firstNameList.forEach(
                firstName -> urls.add(String.format(FIRST_NAME_URL_TEMPLATE, genderParam, firstName))
        );
        return urls;
    }

    private List<Element> fetchElementsFromUrls(List<String> urls) {
        List<Element> elements = new ArrayList<>();
        urls.forEach(
                url -> {
                    try {
                        Document document = Jsoup.connect(url).get();
                        Elements nameElements = document.select(".name_summary");
                        elements.addAll(nameElements);
                    } catch (IOException e) {
                        throw new RuntimeException("Error fetching data from URL: " + url, e);
                    }
                });

        return elements;
    }

    private Element getRandomElement(List<Element> elements) {
        if (elements.isEmpty()) throw new IllegalStateException("No elements found");
        Random random = new Random();
        return elements.get(random.nextInt(elements.size()));
    }

    private Element fetchElementFromUrl(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            return document.selectFirst(".name_summary");
        } catch (IOException e) {
            throw new RuntimeException("Error fetching data from URL: " + url, e);
        }
    }

    private int parseHouseholds(Element element) {
        if (element == null) throw new IllegalStateException("No data found for households");
        String householdsText = element.text();
        String extractedNumber = householdsText.split("aprx\\.")[1].trim().replace(",", "");
        return Integer.parseInt(extractedNumber);
    }

    private String getElementText(Element element, String tag) {
        return Objects.requireNonNull(element.selectFirst(tag)).text();
    }

    private String nameConvert(String koreanName) {
        try {
            Connection.Response response = Jsoup.connect(NAME_CONVERTER_URL)
                    .method(Connection.Method.POST)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .data("contents", koreanName)
                    .data("firstinput", "OK")
                    .execute();

            Document document = response.parse();
            return document.selectFirst(".finalresult").text();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error converting name", e);
        }
    }
}
