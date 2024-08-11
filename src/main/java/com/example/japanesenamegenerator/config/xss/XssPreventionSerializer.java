package com.example.japanesenamegenerator.config.xss;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class XssPreventionSerializer extends JsonSerializer<String> {
    private static final PolicyFactory POLICY = Sanitizers.BLOCKS
        .and(Sanitizers.FORMATTING)
        .and(Sanitizers.LINKS)
        .and(Sanitizers.STYLES)
        .and(Sanitizers.IMAGES);

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            String safe = POLICY.sanitize(value);
            gen.writeString(safe);
        }
    }
}