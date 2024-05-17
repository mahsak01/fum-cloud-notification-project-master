package ir.fum.cloud.notification.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ir.fum.cloud.notification.core.exception.runtime.JsonProcessException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class JsonUtils {

    private static ObjectMapper mapper;

    static {

        mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(
                        new SimpleModule().addSerializer(Double.class, new JsonSerializer<Double>() {
                            @Override
                            public void serialize(Double value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                                jsonGenerator.writeNumber(new BigDecimal(value.toString()).toPlainString());
                            }
                        }))
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }

    public static String getStringJson(Object obj) {
        try {
            mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.disable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("unsuccessful parsing json", e);
            throw new JsonProcessException(e);
        }
    }

    public static String getIntentStringJson(Object obj) {
        try {
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("unsuccessful parsing json", e);
            throw new JsonProcessException(e);
        }
    }

    public static Map<?, ?> getMap(Object o) {
        return mapper.convertValue(o, Map.class);
    }

    public static String getWithNoTimestampJson(Object obj) {
        try {
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.disable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("unsuccessful parsing json", e);
            throw new JsonProcessException(e);
        }
    }

    public static <T> T getObject(byte[] json, Class<T> classOfT) {
        try {
            String content = new String(json, StandardCharsets.UTF_8);
            log.debug("byte content: " + content);
            return mapper.readValue(content, classOfT);
        } catch (IOException e) {
            throw new JsonProcessException(e, classOfT.getSimpleName());
        }
    }

    public static <T> T getObject(String json, Class<T> classOfT) {
        try {
            return mapper.readValue(json, classOfT);
        } catch (IOException e) {
            throw new JsonProcessException(e, classOfT.getSimpleName());
        }
    }

    public static <T> T getObject(Map map, Class<T> classOfT) {
        try {
            return mapper.convertValue(map, classOfT);
        } catch (IllegalArgumentException e) {
            throw new JsonProcessException(e, classOfT.getSimpleName());
        }
    }

    public static <T> T getObject(String json, TypeReference<T> typeReference) {
        try {
            log.info(typeReference.toString());
            return mapper.readValue(json, typeReference);
        } catch (IOException e) {
            String typeName = typeReference.getType().getTypeName();
            throw new JsonProcessException(e, typeName.substring(typeName.lastIndexOf(".") + 1));
        }
    }

    public static JsonNode getJsonObject(String json) {
        try {
            return json == null ? null : mapper.readTree(json);
        } catch (IOException e) {
            throw new JsonProcessException(e);
        }
    }


}
