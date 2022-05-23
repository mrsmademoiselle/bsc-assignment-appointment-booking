package com.example.packend.controller;

import com.example.packend.dto.TerminDto;
import com.example.packend.entities.CancellationUrl;
import com.example.packend.entities.Termin;
import com.example.packend.repositories.CancellationLinkRepository;
import com.example.packend.repositories.TerminRepository;
import com.example.packend.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/example")
public class ExampleController {
    @Autowired
    public EmailService emailService;

    @Autowired
    TerminRepository terminRepository;
    @Autowired
    CancellationLinkRepository cancellationLinkRepository;
    ObjectMapper objectMapper = new ObjectMapper();


    @GetMapping("/get/all")
    public List<Object> getAll() {
        System.out.println("Get all works!");
        List<Termin> all = terminRepository.findAll();

        all.forEach(System.out::println);
        return all.stream().map(e -> objectMapper.valueToTree(new TerminDto(e)))
                .collect(Collectors.toList());
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

        if (terminOptional.isPresent()) {
            TerminDto terminDto = new TerminDto(terminOptional.get());
            return ResponseEntity.ok(terminDto);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/post")
    public String postMe(@RequestBody Termin data) {
        System.out.println("Post works!!");
        System.out.println(data);
        CancellationUrl cancellationUrl = generateOneTimeUrl(data);

        emailService.sendeTerminbestaetigung(data, cancellationUrl);
        terminRepository.save(data);
        return "This works!";
    }

    @GetMapping("/cancel/{token}")
    public ResponseEntity<TerminDto> getByCancellationToken(@PathVariable String token) {
        Optional<CancellationUrl> cancellationUrlOptional = cancellationLinkRepository.findByToken(token);
        if (cancellationUrlOptional.isPresent()) {
            Optional<Termin> terminOptional = terminRepository.findById(cancellationUrlOptional.get().getTerminId());
            System.out.println(cancellationUrlOptional.get().getTerminId());
            if (terminOptional.isPresent()) {
                TerminDto terminDto = new TerminDto(terminOptional.get());
                return ResponseEntity.ok(terminDto);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
        String randomString = generateString(new Random(), "abcdefghijklmnopqrstuvwxyz0123456789", 40);
        CancellationUrl cancellationUrl = new CancellationUrl(termin.getId(), randomString);
        cancellationLinkRepository.save(cancellationUrl);
        return cancellationUrl;
    }

    private String generateString(Random rng, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

}
