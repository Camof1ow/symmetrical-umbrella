package com.example.japanesenamegenerator.independence.application.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JacksonXmlRootElement(localName = "root")
@Getter
@NoArgsConstructor
public class ActivistOpenApiResponse {

    @JacksonXmlProperty(localName = "script")
    private String script;

    @JacksonXmlProperty(localName = "total_count")
    private int totalCount;

    @JacksonXmlProperty(localName = "page_count")
    @Setter
    private int pageCount;

    @JacksonXmlProperty(localName = "page")
    private int page;

    @JacksonXmlProperty(localName = "article_count")
    private int articleCount;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    private List<DataItem> items;

    @Getter
    @NoArgsConstructor
    public static class DataItem {

        @JacksonXmlProperty(localName = "nameHanja")
        private String nameHanja;

        @JacksonXmlProperty(localName = "movementFamily")
        private String movementFamily;

        @JacksonXmlProperty(localName = "orderYear")
        private int orderYear;

        @JacksonXmlProperty(localName = "name")
        private String name;

        @JacksonXmlProperty(localName = "addressBirth")
        private String addressBirth;

        @JacksonXmlProperty(localName = "aliases")
        private String aliases;

        @JacksonXmlProperty(localName = "bornDied")
        private String bornDied;

        @JacksonXmlProperty(localName = "placeOfOrigin")
        private String placeOfOrigin;

        @JacksonXmlProperty(localName = "references")
        private String references;

        @JacksonXmlProperty(localName = "content")
        private String content;

        @JacksonXmlProperty(localName = "activities")
        private String activities;

        @JacksonXmlProperty(localName = "engagedEvents")
        private String engagedEvents;

        @JacksonXmlProperty(localName = "engagedOrganizations")
        private String engagedOrganizations;
    }
}
