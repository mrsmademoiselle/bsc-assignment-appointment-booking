package com.example.packend.controller;

import com.example.packend.entities.Beratungsstelle;
import com.example.packend.enums.Anrede;
import com.example.packend.enums.Beratungsgrund;
import com.example.packend.repositories.BeratungsstellenRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RestController
@RequestMapping("/public")
public class BeratungsstellenController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeratungsstellenController.class);

    @Autowired
    BeratungsstellenRepository beratungsstellenRepository;
    ObjectMapper objectMapper = new ObjectMapper();


    @GetMapping("/beratungsstellen/get/all")
    public ResponseEntity<List<JsonNode>> getAllBeratungsstellen() {
        LOGGER.info("Calling getAllBeratungsstellen");
        List<Beratungsstelle> all = beratungsstellenRepository.findAll();

        List<JsonNode> alleBeratungsstellen = new ArrayList<>();
        for (Beratungsstelle beratungsstelle : all) {
            alleBeratungsstellen.add(objectMapper.valueToTree(beratungsstelle));
        }
        return ResponseEntity.ok(alleBeratungsstellen);
    }

    @GetMapping("/termingrund/get/all")
    public ResponseEntity<List<String>> getAllTermingruende() {
        LOGGER.info("Calling getAllTermingruende");

        Beratungsgrund[] values = Beratungsgrund.values();
        return ResponseEntity.ok(Arrays.stream(values)
                .map(Beratungsgrund::getGrund)
                .collect(Collectors.toList()));
    }

    @GetMapping("/anrede/get/all")
    public ResponseEntity<List<String>> getAllAnreden() {
        LOGGER.info("Calling getAllAnreden");
        Anrede[] values = Anrede.values();
        return ResponseEntity.ok(Arrays.stream(values)
                .map(Anrede::getAnrede)
                .collect(Collectors.toList()));
    }
}
