package com.example.packend.controller;

import com.example.packend.entities.Beratungsstelle;
import com.example.packend.enums.Anrede;
import com.example.packend.enums.Beratungsgrund;
import com.example.packend.repositories.BeratungsstellenRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    BeratungsstellenRepository beratungsstellenRepository;
    ObjectMapper objectMapper = new ObjectMapper();


    @GetMapping("/beratungsstellen/get/all")
    public ResponseEntity<List<JsonNode>> getAllBeratungsstellen() {
        System.out.println("getAllBeratungsstellen works!");
        List<Beratungsstelle> all = beratungsstellenRepository.findAll();

        try {
            List<JsonNode> alleberatungsstellen = new ArrayList<>();
            for (Beratungsstelle beratungsstelle : all) {
                alleberatungsstellen.add(objectMapper.valueToTree(beratungsstelle));
            }
            return ResponseEntity.ok(alleberatungsstellen);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/termingrund/get/all")
    public ResponseEntity<List<String>> getAllTermingruende() {
        Beratungsgrund[] values = Beratungsgrund.values();
        return ResponseEntity.ok(Arrays.stream(values).map(Beratungsgrund::getGrund).collect(Collectors.toList()));
    }

    @GetMapping("/anrede/get/all")
    public ResponseEntity<List<String>> getAllAnreden() {
        Anrede[] values = Anrede.values();
        return ResponseEntity.ok(Arrays.stream(values).map(Anrede::getAnrede).collect(Collectors.toList()));
    }
}
