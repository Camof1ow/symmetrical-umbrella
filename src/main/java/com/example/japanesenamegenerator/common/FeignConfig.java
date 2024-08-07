package com.example.japanesenamegenerator.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter() {
        ObjectMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new MappingJackson2XmlHttpMessageConverter(xmlMapper);
    }

}
