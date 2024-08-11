package com.example.japanesenamegenerator.config.xss;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class XssPreventionModule extends SimpleModule {
    public XssPreventionModule() {
        addSerializer(String.class, new XssPreventionSerializer());
    }
}