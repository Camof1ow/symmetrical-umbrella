package com.example.japanesenamegenerator.independence.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import com.example.japanesenamegenerator.independence.application.ActivistService;
import com.example.japanesenamegenerator.independence.application.response.ActivistOpenApiResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ActivistServiceTest {

    @Autowired
    private ActivistService activistService;

    @Test
    void crawl_data() {
        try{

            // When
            List<ActivistOpenApiResponse> activistOpenApiRespons = activistService.fetchApiData();

            // Then
            assertNotNull(activistOpenApiRespons);

            // Add more specific assertions based on the expected structure of your ActivistResponse
            // For example:
            // assertNotNull(response.getItems());
            // assertFalse(response.getItems().isEmpty());
            // assertEquals(expectedTotalCount, response.getTotalCount());

            // Print the response for manual inspection
            System.out.println("Received response: " + activistOpenApiRespons);
        }catch (Exception e){
            fail("Exception occurred: " + e.getMessage());
        }
    }
}