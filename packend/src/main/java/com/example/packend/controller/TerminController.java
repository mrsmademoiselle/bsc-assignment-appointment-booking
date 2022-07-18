package com.example.packend.controller;

import com.example.packend.dto.TerminDto;
import com.example.packend.entities.CancellationUrl;
import com.example.packend.entities.Termin;
import com.example.packend.mapper.TerminToDtoMapper;
import com.example.packend.repositories.BeratungsstellenRepository;
import com.example.packend.repositories.CancellationLinkRepository;
import com.example.packend.repositories.TerminRepository;
import com.example.packend.services.TerminService;
import com.example.packend.services.mail.EmailService;
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
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Transactional
@RestController
@RequestMapping("/public/termin")
public class TerminController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TerminController.class);
    @Autowired
    public EmailService emailService;
    @Autowired
    TerminRepository terminRepository;
    @Autowired
    CancellationLinkRepository cancellationLinkRepository;
    @Autowired
    BeratungsstellenRepository beratungsstellenRepository;
    @Autowired
    TerminToDtoMapper terminToDtoMapper;

    @Autowired
    TerminService terminService;

    ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/uhrzeiten/get/{jahr}/{monat}/{tag}")
    public ResponseEntity<List<String>> get(@PathVariable String jahr, @PathVariable String monat, @PathVariable String tag) {

        List<String> verfuegbareUhrzeitenFuerTag = terminService.berechneVerfuegbareUhrzeitenFuerTag(
                LocalDate.of(Integer.parseInt(jahr), Integer.parseInt(monat), Integer.parseInt(tag)));
        LOGGER.info("verfuegbareUhrzeitenFuerTag: " + verfuegbareUhrzeitenFuerTag.size());

        return ResponseEntity.ok(verfuegbareUhrzeitenFuerTag);
    }

    @GetMapping("/komplett-belegt/all")
    public ResponseEntity<List<LocalDate>> getAlleKomplettBelegtenDatümer() {
        List<LocalDate> komplettBelegteTage = terminService.berechneKomplettBelegteTage();
        LOGGER.info("Komplett belegte Tage: " + komplettBelegteTage.size());
        return ResponseEntity.ok(komplettBelegteTage);
    }


    @GetMapping("/get/all")
    public ResponseEntity<List<JsonNode>> getAll() {
        LOGGER.info("Calling getAll");
        List<Termin> all = terminRepository.findAll();

        List<JsonNode> alleTerminDtos = new ArrayList<>();
        for (Termin termin : all) {
            JsonNode node = objectMapper.valueToTree(terminToDtoMapper.terminToDto(termin));
            alleTerminDtos.add(node);
        }
        return ResponseEntity.ok(alleTerminDtos);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TerminDto> getById(@PathVariable Long id) {
        LOGGER.info("Calling getById");

        Optional<Termin> terminOptional = terminRepository.findById(id);
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

    @PostMapping("/post")
    public String saveTermin(@RequestBody @Validated Termin data) {
        LOGGER.info("Calling saveTermin with data " + data.toString());
        CancellationUrl cancellationUrl = generateOneTimeUrl(data);

        emailService.sendeTerminbestaetigung(data, cancellationUrl);
        terminRepository.save(data);

        return "This works!";
    }

    @GetMapping("/cancel/{token}")
    public ResponseEntity<TerminDto> getByCancellationToken(@PathVariable String token) {
        LOGGER.info("Calling getByCancellationToken with token " + token);

        Optional<CancellationUrl> cancellationUrlOptional = cancellationLinkRepository.findByToken(token);

        if (cancellationUrlOptional.isPresent()) {
            Optional<Termin> terminOptional = terminRepository.findById(cancellationUrlOptional.get().getTerminId());
            if (terminOptional.isPresent()) {
                Termin termin = terminOptional.get();

                TerminDto terminDto = terminToDtoMapper.terminToDto(termin);
                return ResponseEntity.ok(terminDto);
            } else {
                LOGGER.warn("No Appointment has been found for token " + token);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            LOGGER.warn("The CancellationToken does not exist.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id) {
        LOGGER.info("Calling cancelAppointment for Appointment with id " + id);

        Optional<Termin> terminOptional = terminRepository.findById(id);
        if (terminOptional.isPresent()) {
            // remove cancellationUrl
            cancellationLinkRepository.deleteByTerminId(id);
            // remove appointment
            terminRepository.delete(terminOptional.get());
            // send cancellation-Email
            emailService.sendeTerminabsage(terminOptional.get());

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            LOGGER.info("No appointment has been found for id " + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    private CancellationUrl generateOneTimeUrl(Termin termin) {
        String randomString = generateString();
        CancellationUrl cancellationUrl = new CancellationUrl(termin.getId(), randomString);
        cancellationLinkRepository.save(cancellationUrl);
        return cancellationUrl;
    }

    private String generateString() {
        Random rng = new Random();
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        int length = 40;
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

}
