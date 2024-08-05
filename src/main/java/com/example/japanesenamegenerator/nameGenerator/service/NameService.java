package com.example.japanesenamegenerator.nameGenerator.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.Duration;

@Service
public class NameService {

    private final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .followRedirects(false)
            .followSslRedirects(false)
            .retryOnConnectionFailure(true)
            .connectTimeout(Duration.ofSeconds(120L))
            .readTimeout(Duration.ofSeconds(120L))
            .build();

    public String generateName(String surName, String lastName) {

        if(lastName.length() > 1){
            lastName = lastName.substring(0, 1);
        }

        String lastNameUrl = String.format("https://japanese-names.info/last-names/search-result/freeword-%s/", lastName);
        String responseBody ;
        try (Response response = request("GET", lastNameUrl, null)) {

            responseBody = responseToString(response);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


        System.out.println();
        return "";
    }

    private Response request(String method, String url, RequestBody body)
            throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(Duration.ofSeconds(15L))
                .readTimeout(Duration.ofSeconds(15L)).build();
        Request request = new Request.Builder()
                .url(url)
                .method(method, body)
                .build();
        return client.newCall(request).execute();
    }

    private String responseToString(Response response) throws IOException {
        try (BufferedReader br = new BufferedReader(response.body().charStream())) {
            StringBuilder responseBuffer = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                responseBuffer.append(inputLine);
            }
            return responseBuffer.toString();
        }
    }


}
