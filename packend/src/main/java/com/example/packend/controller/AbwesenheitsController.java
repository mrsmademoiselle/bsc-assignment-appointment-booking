package com.example.packend.controller;

import com.example.packend.entities.Abwesenheit;
import com.example.packend.repositories.AbwesenheitRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

        List<Abwesenheit> all = abwesenheitRepository.findAll(Sort.by("startDatum").ascending());

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
        boolean existiert = abwesenheitRepository.existsById(id);
        if (existiert) {
            abwesenheitRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Abwesenheit> addAbwesenheit(@RequestBody Abwesenheit abwesenheit) {
        LOGGER.info("Abwesenheit wird angelegt: " + abwesenheit);
        LocalDate startDatum = abwesenheit.getStartDatum();
        LocalDate endDatum = abwesenheit.getEndDatum();
        boolean nochKeineEintraegeInZeitraum = abwesenheitRepository.findAllByStartDatumBetween(startDatum, endDatum).isEmpty()
                && abwesenheitRepository.findAllByEndDatumBetween(startDatum, endDatum).isEmpty();

        if (nochKeineEintraegeInZeitraum) {
            abwesenheit = abwesenheitRepository.save(abwesenheit);
            return ResponseEntity.ok(abwesenheit);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
