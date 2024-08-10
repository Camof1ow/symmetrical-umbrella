package com.example.japanesenamegenerator.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public feign.Logger feignLogger() {
        return new feign.slf4j.Slf4jLogger();
    }
//
//    @Bean
//    public Decoder feignDecoder() {
//        return new ResponseEntityDecoder(new SpringDecoder(() -> new HttpMessageConverters(
//            new MappingJackson2XmlHttpMessageConverter(xmlMapper())
//        )));
//    }
//
//    @Bean
//    public XmlMapper xmlMapper() {
//        XmlMapper xmlMapper = new XmlMapper();
//        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        xmlMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
//        return xmlMapper;
//    }
}
