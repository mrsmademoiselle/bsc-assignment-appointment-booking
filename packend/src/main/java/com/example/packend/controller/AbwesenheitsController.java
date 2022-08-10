package com.example.packend.controller;

import com.example.packend.dto.AbwesenheitDto;
import com.example.packend.entities.Abwesenheit;
import com.example.packend.repositories.AbwesenheitRepository;
import com.example.packend.services.AbwesenheitsService;
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
    AbwesenheitsService abwesenheitsService;

    /**
     * Gibt alle Abwesenheitseinträge im Zeitraum zurück.
     */
    @GetMapping("/get/all/{mitarbeiterId}")
    public ResponseEntity<List<JsonNode>> getAlleAbwesenheiten(@PathVariable String mitarbeiterId) {
        LOGGER.info("Calling getAlleAbwesenheiten");

        List<Abwesenheit> all = abwesenheitRepository.findAllByMitarbeiter_Username(mitarbeiterId, Sort.by("startDatum").ascending());

        System.out.println("alle: " + all.size() + ", mitarbeiterid: " + mitarbeiterId);
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
        try {
            Abwesenheit abwesenheit = abwesenheitsService.save(abwesenheitDto).orElseThrow(RuntimeException::new);
            AbwesenheitDto abwesenheitDto1 = AbwesenheitDto.builder()
                    .id(abwesenheit.getId())
                    .mitarbeiterId(abwesenheit.getMitarbeiter().getUsername())
                    .endDatum(abwesenheit.getEndDatum())
                    .startDatum(abwesenheit.getStartDatum())
                    .build();
            return ResponseEntity.ok(abwesenheitDto1);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
