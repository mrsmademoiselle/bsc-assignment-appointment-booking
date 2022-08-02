package com.example.packend.controller;

import com.example.packend.entities.Abwesenheit;
import com.example.packend.repositories.AbwesenheitRepository;
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
@RequestMapping("/abwesenheit")
public class AbwesenheitsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbwesenheitsController.class);
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    AbwesenheitRepository abwesenheitRepository;

    @GetMapping("/get/all")
    public ResponseEntity<List<JsonNode>> getAlleAbwesenheiten() {
        LOGGER.info("Calling getAllBeratungsstellen");
        List<Abwesenheit> all = abwesenheitRepository.findAll();

        List<JsonNode> beratungsstellenAlsJsonList = new ArrayList<>();
        for (Abwesenheit beratungsstelle : all) {
            beratungsstellenAlsJsonList.add(objectMapper.valueToTree(beratungsstelle));
        }
        return ResponseEntity.ok(beratungsstellenAlsJsonList);
    }

    @PostMapping("/remove/{stringId}")
    public ResponseEntity<String> cancelAbwesenheit(@PathVariable String stringId) {
        Long id = Long.valueOf(stringId);
        abwesenheitRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/add")
    public ResponseEntity<String> addAbwesenheit(@RequestBody Abwesenheit abwesenheit) {
        LOGGER.debug("Abwesenheit wird angelegt: " + abwesenheit);
        abwesenheitRepository.save(abwesenheit);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
