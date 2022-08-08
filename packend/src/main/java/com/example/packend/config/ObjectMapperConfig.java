package com.example.packend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Hilfsklasse für ausgelagerte object-Mapper-Bean, da standardmäßig
 * das JavaTimeModule nicht registriert wird und es dadurch beim automatischen
 * Mapping in den Controllern zu Fehlern kommt.
 */
@Configuration
public class ObjectMapperConfig {
    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
