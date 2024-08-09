package com.example.japanesenamegenerator.independence.domain.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FamilyKeysAndPageCount {

    private String familyKey;
    @JacksonXmlProperty(localName = "page_count")
    private int pageCount;

    public FamilyKeysAndPageCount(String familyKey, int pageCount) {
        this.familyKey = familyKey;
        this.pageCount = pageCount;
    }

    public FamilyKeysAndPageCount of(String familyKey, int pageCount) {
        return new FamilyKeysAndPageCount(familyKey, pageCount);
    }
}
