package com.example.japanesenamegenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class JapaneseNameGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(JapaneseNameGeneratorApplication.class, args);
    }

}
