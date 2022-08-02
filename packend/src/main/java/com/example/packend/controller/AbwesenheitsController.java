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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Transactional
@RestController
@RequestMapping("/abwesenheit")
public class AbwesenheitsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbwesenheitsController.class);
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AbwesenheitRepository abwesenheitRepository;

    @GetMapping("/get/all")
    public ResponseEntity<List<JsonNode>> getAlleAbwesenheiten() {
        LOGGER.info("Calling getAlleAbwesenheiten");
        List<Abwesenheit> all = abwesenheitRepository.findAll();

        List<JsonNode> abwesenheitAlsJsonList = new ArrayList<>();
        for (Abwesenheit abwesenheit : all) {
            JsonNode jsonNode = objectMapper.valueToTree(abwesenheit);
            abwesenheitAlsJsonList.add(jsonNode);
        }
        System.out.println(abwesenheitAlsJsonList.size());
        return ResponseEntity.ok(abwesenheitAlsJsonList);
    }

    @PostMapping("/remove/{stringId}")
    public ResponseEntity<String> cancelAbwesenheit(@PathVariable String stringId) {
        Long id = Long.valueOf(stringId);
        abwesenheitRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/add")
    public ResponseEntity<String> addAbwesenheit(@RequestBody Abwesenheit abwesenheit) {
        LOGGER.info("Abwesenheit wird angelegt: " + abwesenheit);
        LocalDate startDatum = abwesenheit.getStartDatum();
        LocalDate endDatum = abwesenheit.getEndDatum();
        boolean nochKeineEintraegeInZeitraum = abwesenheitRepository.findAllByStartDatumBetween(startDatum, endDatum).isEmpty()
                && abwesenheitRepository.findAllByEndDatumBetween(startDatum, endDatum).isEmpty();

        if (nochKeineEintraegeInZeitraum) {
            abwesenheitRepository.save(abwesenheit);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
