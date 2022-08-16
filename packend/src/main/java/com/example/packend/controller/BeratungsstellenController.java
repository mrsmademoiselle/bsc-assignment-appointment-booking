package com.example.packend.controller;

import com.example.packend.entities.Beratungsstelle;
import com.example.packend.repositories.BeratungsstellenRepository;
import com.example.packend.services.BeratungsstellenService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@RestController
@RequestMapping
public class BeratungsstellenController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeratungsstellenController.class);

    @Autowired
    BeratungsstellenRepository beratungsstellenRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    BeratungsstellenService beratungsstellenService;

    @GetMapping("public/beratungsstellen/get/all")
    public ResponseEntity<List<JsonNode>> getAllBeratungsstellen() {
        List<Beratungsstelle> all = beratungsstellenRepository.findAllByIstArchiviertIsFalse();

        List<JsonNode> beratungsstellenAlsJsonList = new ArrayList<>();
        for (Beratungsstelle beratungsstelle : all) {
            beratungsstellenAlsJsonList.add(objectMapper.valueToTree(beratungsstelle));
        }
        return ResponseEntity.ok(beratungsstellenAlsJsonList);
    }

    @PostMapping("/beratungsstellen/add")
    public ResponseEntity<Beratungsstelle> addBeratungsstelle(@RequestBody Beratungsstelle beratungsstelle) {
        beratungsstelle = beratungsstellenRepository.save(beratungsstelle);

        LOGGER.info("Beratungsstelle wurde erfolgreich angelegt.");
        return ResponseEntity.ok(beratungsstelle);
    }

    @PostMapping("/beratungsstellen/archiviere/{stringId}")
    public ResponseEntity<String> archiviereBeratungsstelle(@PathVariable String stringId) {
        try {
            beratungsstellenService.archiviereBeratungsstelle(stringId);

            LOGGER.info("Beratungsstelle mit ID " + stringId + " wurde erfolgreich archiviert.");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
