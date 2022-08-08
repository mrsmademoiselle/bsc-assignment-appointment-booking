package com.example.packend.controller;

import com.example.packend.entities.Beratungsstelle;
import com.example.packend.repositories.BeratungsstellenRepository;
import com.example.packend.repositories.MitarbeiterRepository;
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
import java.util.Optional;

@Transactional
@RestController
@RequestMapping
public class BeratungsstellenController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeratungsstellenController.class);

    @Autowired
    BeratungsstellenRepository beratungsstellenRepository;
    @Autowired
    MitarbeiterRepository mitarbeiterRepository;
    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("public/beratungsstellen/get/all")
    public ResponseEntity<List<JsonNode>> getAllBeratungsstellen() {
        LOGGER.info("Calling getAllBeratungsstellen");
        List<Beratungsstelle> all = beratungsstellenRepository.findAllByIstArchiviertIsFalse();

        List<JsonNode> beratungsstellenAlsJsonList = new ArrayList<>();
        for (Beratungsstelle beratungsstelle : all) {
            beratungsstellenAlsJsonList.add(objectMapper.valueToTree(beratungsstelle));
        }
        return ResponseEntity.ok(beratungsstellenAlsJsonList);
    }

    @PostMapping("/beratungsstellen/add")
    public ResponseEntity<Beratungsstelle> addBeratungsstelle(@RequestBody Beratungsstelle beratungsstelle) {
        LOGGER.warn("calling addBeratungsstelle");

        beratungsstelle = beratungsstellenRepository.save(beratungsstelle);

        return ResponseEntity.ok(beratungsstelle);
    }

    @PostMapping("/beratungsstellen/archiviere/{stringId}")
    public ResponseEntity<String> archiviere(@PathVariable String stringId) {
        LOGGER.warn("archiviere Beratungsstelle mit ID " + stringId);

        Long id = Long.valueOf(stringId);
        Optional<Beratungsstelle> optional = beratungsstellenRepository.findById(id);
        if (optional.isPresent()) {
            Beratungsstelle beratungsstelle = optional.get();
            beratungsstelle.setIstArchiviert(true);
            beratungsstellenRepository.save(beratungsstelle);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
