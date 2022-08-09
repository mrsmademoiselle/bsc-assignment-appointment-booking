package com.example.packend.controller;

import com.example.packend.dto.AbwesenheitDto;
import com.example.packend.entities.Abwesenheit;
import com.example.packend.repositories.AbwesenheitRepository;
import com.example.packend.repositories.MitarbeiterRepository;
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

    @Autowired
    MitarbeiterRepository mitarbeiterRepository;

    /**
     * Gibt alle Abwesenheitseinträge im Zeitraum zurück.
     */
    @GetMapping("/get/all/{mitarbeiterId}")
    public ResponseEntity<List<JsonNode>> getAlleAbwesenheiten(String mitarbeiterId) {
        LOGGER.info("Calling getAlleAbwesenheiten");

        List<Abwesenheit> all = abwesenheitRepository.findAllByMitarbeiter_Username(mitarbeiterId, Sort.by("startDatum").ascending());

        List<JsonNode> abwesenheitAlsJsonList = new ArrayList<>();
        for (Abwesenheit abwesenheit : all) {
            AbwesenheitDto abwesenheitDto1 = AbwesenheitDto.builder()
                    .id(abwesenheit.getId())
                    .mitarbeiterId(abwesenheit.getMitarbeiter().getUsername())
                    .endDatum(abwesenheit.getEndDatum())
                    .startDatum(abwesenheit.getStartDatum())
                    .build();
            JsonNode jsonNode = objectMapper.valueToTree(abwesenheitDto1);
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
    public ResponseEntity<AbwesenheitDto> addAbwesenheit(@RequestBody AbwesenheitDto abwesenheitDto) {
        LOGGER.info("Abwesenheit wird angelegt: " + abwesenheitDto);
        LocalDate startDatum = abwesenheitDto.getStartDatum();
        LocalDate endDatum = abwesenheitDto.getEndDatum();

        try {
            boolean nochKeineEintraegeInZeitraum = abwesenheitRepository.findAllByMitarbeiter_UsernameAndStartDatumBetween(abwesenheitDto.getMitarbeiterId(), startDatum, endDatum).isEmpty()
                    && abwesenheitRepository.findAllByMitarbeiter_UsernameAndEndDatumBetween(abwesenheitDto.getMitarbeiterId(), startDatum, endDatum).isEmpty();

            if (nochKeineEintraegeInZeitraum) {
                Abwesenheit abwesenheit = Abwesenheit.builder()
                        .mitarbeiter(mitarbeiterRepository.findByUsername(abwesenheitDto.getMitarbeiterId()).orElseThrow(RuntimeException::new))
                        .startDatum(abwesenheitDto.getStartDatum())
                        .endDatum(abwesenheitDto.getEndDatum()).build();
                abwesenheit = abwesenheitRepository.save(abwesenheit);

                AbwesenheitDto abwesenheitDto1 = AbwesenheitDto.builder()
                        .id(abwesenheit.getId())
                        .mitarbeiterId(abwesenheit.getMitarbeiter().getUsername())
                        .endDatum(abwesenheit.getEndDatum())
                        .startDatum(abwesenheit.getStartDatum())
                        .build();
                return ResponseEntity.ok(abwesenheitDto1);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
