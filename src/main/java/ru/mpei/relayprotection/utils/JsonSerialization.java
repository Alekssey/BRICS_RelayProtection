package ru.mpei.relayprotection.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JsonSerialization {
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> Optional<T> parseJson(String json, Class<T> clazz){
        try {
            T object = mapper.readValue(json, clazz);
            return Optional.of(object);
        } catch (JsonProcessingException e) {
            log.error("Can not parse json to {} class", clazz);
            return Optional.empty();
        }
    }

    public static <T> List<T> parseList(String json, Class<T> clazz) {
        try {
            TypeFactory typeFactory = mapper.getTypeFactory();
            JavaType listType = typeFactory.constructCollectionType(List.class, clazz);
            return mapper.readValue(json, listType);
        } catch (JsonProcessingException e) {
            log.error("Can not parse json to List<{}> ", clazz);
            return new ArrayList<>();
        }
    }

    public static <T> List<T> parseListFromFile(String filePath, Class<T> clazz) {
        try {
            String json = Files.readString(Paths.get(filePath));
            TypeFactory typeFactory = mapper.getTypeFactory();
            JavaType listType = typeFactory.constructCollectionType(List.class, clazz);
            return mapper.readValue(json, listType);
        } catch (JsonProcessingException e) {
            log.error("Can not parse json to List<{}> ", clazz);
            return new ArrayList<>();
        } catch (IOException e) {
            log.error("Can not read json from file {}", filePath);
            return new ArrayList<>();
        }
    }

    public static Optional<String> writeAsJson(Object obj) {
        try {
            return Optional.of(mapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            log.error("Can not write object {} to json", obj);
            return Optional.empty();
        }
    }

}
