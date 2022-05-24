package com.example.packend.controller;

import com.example.packend.dto.TerminDto;
import com.example.packend.entities.Beratungsstelle;
import com.example.packend.entities.CancellationUrl;
import com.example.packend.entities.Termin;
import com.example.packend.mapper.TerminToDtoMapper;
import com.example.packend.repositories.BeratungsstellenRepository;
import com.example.packend.repositories.CancellationLinkRepository;
import com.example.packend.repositories.TerminRepository;
import com.example.packend.services.mail.EmailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Transactional
@RestController
@RequestMapping("/public/termin")
public class TerminController {
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
    ObjectMapper objectMapper = new ObjectMapper();


    @GetMapping("/get/all")
    public ResponseEntity<List<JsonNode>> getAll() {
        System.out.println("Get all works!");
        List<Termin> all = terminRepository.findAll();

        try {
            List<JsonNode> alleTerminDtos = new ArrayList<>();
            for (Termin termin : all) {
                JsonNode node = objectMapper.valueToTree(terminToDtoMapper.terminToDto(termin));
                alleTerminDtos.add(node);
                System.out.println(node);
            }
            return ResponseEntity.ok(alleTerminDtos);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get")
    public String getMe() {
        System.out.println("getMe works!");
        return "It works!";
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TerminDto> getById(@PathVariable String id) {
        System.out.println("getById works! id:" + id);

        Optional<Termin> terminOptional = terminRepository.findById(id);

        System.out.println("is there: " + terminOptional.isPresent());
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
    public String postMe(@RequestBody Termin data) {
        System.out.println("Post works!!");
        System.out.println(data);
        CancellationUrl cancellationUrl = generateOneTimeUrl(data);

        Beratungsstelle beratungsstelle = data.getBeratungsstelle();

        emailService.sendeTerminbestaetigung(data, beratungsstelle, cancellationUrl);
        terminRepository.save(data);

        return "This works!";
    }

    @GetMapping("/cancel/{token}")
    public ResponseEntity<TerminDto> getByCancellationToken(@PathVariable String token) {
        Optional<CancellationUrl> cancellationUrlOptional = cancellationLinkRepository.findByToken(token);

        try {
            if (cancellationUrlOptional.isPresent()) {
                Optional<Termin> terminOptional = terminRepository.findById(cancellationUrlOptional.get().getTerminId());
                System.out.println(cancellationUrlOptional.get().getTerminId());
                if (terminOptional.isPresent()) {
                    Termin termin = terminOptional.get();

                    TerminDto terminDto = terminToDtoMapper.terminToDto(termin);
                    return ResponseEntity.ok(terminDto);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelAppointment(@PathVariable String id) {
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
