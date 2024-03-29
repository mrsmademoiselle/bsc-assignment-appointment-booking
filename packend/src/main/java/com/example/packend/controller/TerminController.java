package com.example.packend.controller;

import com.example.packend.dto.TerminDto;
import com.example.packend.entities.Termin;
import com.example.packend.enums.Anrede;
import com.example.packend.enums.Beratungsgrund;
import com.example.packend.mapper.TerminToDtoMapper;
import com.example.packend.repositories.TerminRepository;
import com.example.packend.services.TerminService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@RestController
@RequestMapping
public class TerminController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TerminController.class);

    @Autowired
    TerminRepository terminRepository;
    @Autowired
    TerminToDtoMapper terminToDtoMapper;
    @Autowired
    TerminService terminService;
    @Autowired
    ObjectMapper objectMapper;

    /**
     * Berechnet und gibt die verfügbaren Uhrzeiten für einen spezifischen Termin zurück.
     * Diese Uhrzeiten werden nach Auswahl eines Datums angezeigt.
     */
    @GetMapping("/public/{mitarbeiterId}/termin/uhrzeiten/get/{jahr}/{monat}/{tag}")
    public ResponseEntity<List<String>> getVerfuegbareUhrzeitenFuerTermin(@PathVariable String jahr, @PathVariable String monat, @PathVariable String tag, @PathVariable String mitarbeiterId) {
        List<String> uhrzeiten = terminService.getVerfuegbareUhrzeitenFuerTerminUndMitarbeiter(jahr, monat, tag, mitarbeiterId);

        return ResponseEntity.ok(uhrzeiten);
    }

    /**
     * Gibt alle Datümer zurück, in denen schon Termine existieren.
     * Wird bei der Terminbuchung verwendet, um diese Datümer auszugrauen.
     */
    @GetMapping("/public/termin/{mitarbeiterId}/komplett-belegt/all")
    public ResponseEntity<List<LocalDate>> getAlleKomplettBelegtenDatuemer(@PathVariable String mitarbeiterId) {
        List<LocalDate> komplettBelegteTage = terminService.berechneKomplettBelegteTage(mitarbeiterId);

        LOGGER.info("getAlleKomplettBelegtenDatuemer()-Berechnung abgeschlossen. Anzahl aller komplett belegten Tage: " + komplettBelegteTage.size());
        return ResponseEntity.ok(komplettBelegteTage);
    }

    /**
     * Gibt eine Übersicht aller bestehenden Termine zurück.
     */
    @GetMapping("/termin/get/all")
    public ResponseEntity<List<JsonNode>> getAll() {
        LOGGER.info("Calling /termin/get/all");
        List<Termin> all = terminService.findAll();

        List<JsonNode> alleTerminDtos = new ArrayList<>();
        for (Termin termin : all) {
            JsonNode node = objectMapper.valueToTree(terminToDtoMapper.terminToDto(termin));
            alleTerminDtos.add(node);
        }
        return ResponseEntity.ok(alleTerminDtos);
    }

    /**
     * Gibt die Termindaten zu dem Termin mit der ID zurück.
     */
    @GetMapping("/public/termin/get/{terminId}")
    public ResponseEntity<TerminDto> getById(@PathVariable Long terminId) {
        LOGGER.info("getById() mit ID " + terminId);

        Optional<Termin> terminOptional = terminRepository.findById(terminId);
        try {
            if (terminOptional.isPresent()) {
                Termin termin = terminOptional.get();
                TerminDto terminDto = terminToDtoMapper.terminToDto(termin);

                return ResponseEntity.ok(terminDto);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Speichert einen Termin
     */
    @PostMapping("/public/termin/post")
    public ResponseEntity<TerminDto> saveTermin(@RequestBody @Validated Termin data) {
        try {
            Termin gespeicherterTermin = terminService.saveTermin(data);
            TerminDto terminDto = terminToDtoMapper.terminToDto(gespeicherterTermin);

            LOGGER.info("Termin wurde erfolgreich gespeichert.");
            return ResponseEntity.ok(terminDto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Sucht nach dem Termin für das Cancellation Token und gibt diesen zurück.
     */
    @GetMapping("/public/termin/cancel/{token}")
    public ResponseEntity<TerminDto> getByCancellationToken(@PathVariable String token) {
        LOGGER.info("getByCancellationToken() mit Absage-URL-Token " + token);

        Optional<Termin> terminOptional = terminService.getTerminMitAbsageToken(token);
        if (terminOptional.isPresent()) {
            TerminDto terminDto = terminToDtoMapper.terminToDto(terminOptional.get());
            return ResponseEntity.ok(terminDto);
        } else {
            LOGGER.warn("Für das Absage-URL-Token " + token + " wurde kein Termin gefunden. ");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Über diesen Endpunkt kann ein Benutzer seinen eigenen Termin stornieren.
     */
    @PostMapping("/public/termin/cancel/{id}")
    public ResponseEntity<String> terminAbsagenUser(@PathVariable Long id) {
        boolean wurdeErfolgreichAbgesagt = terminService.sageTerminAb(id);

        if (wurdeErfolgreichAbgesagt) {
            LOGGER.info("Termin mit ID " + id + " wurde erfolgreich abgesagt.");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            LOGGER.info("Der abzusagende Termin mit ID " + id + " konnte nicht gefunden werden.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("public/termingrund/get/all")
    public ResponseEntity<List<String>> getAllTermingruende() {
        Beratungsgrund[] values = Beratungsgrund.values();
        return ResponseEntity.ok(Arrays.stream(values)
                .map(Beratungsgrund::getGrund)
                .collect(Collectors.toList()));
    }

    @GetMapping("public/anrede/get/all")
    public ResponseEntity<List<String>> getAllAnreden() {
        Anrede[] values = Anrede.values();
        return ResponseEntity.ok(Arrays.stream(values)
                .map(Anrede::getAnrede)
                .collect(Collectors.toList()));
    }
}
