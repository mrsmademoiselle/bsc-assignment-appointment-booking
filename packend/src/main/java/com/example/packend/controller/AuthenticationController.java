package com.example.packend.controller;

import com.example.packend.dto.LoginRequest;
import com.example.packend.dto.MitarbeiterDto;
import com.example.packend.entities.Mitarbeiter;
import com.example.packend.repositories.MitarbeiterRepository;
import com.example.packend.services.AuthenticationService;
import com.example.packend.services.MitarbeiterService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@RestController
@RequestMapping
public class AuthenticationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);
    @Autowired
    AuthenticationService userAuthService;
    @Autowired
    MitarbeiterService mitarbeiterService;
    @Autowired
    MitarbeiterRepository mitarbeiterRepository;
    @Autowired
    ObjectMapper objectMapper;

    /**
     * Loggt einen Benutzer ein.
     */
    @PostMapping("/public/auth/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        LOGGER.warn("Login-Versuch von User " + loginRequest.getUsername());

        try {
            final String token = userAuthService.authentifiziereUndErstelleTokenFuerUser(loginRequest.getUsername(), loginRequest.getPassword());
            LOGGER.info("Login erfolgreich.");
            return token;
        } catch (Exception e) {
            LOGGER.error("Login fehlgeschlagen.");
            return "";
        }
    }

    /**
     * Registriert einen Benutzer.
     */
    @PostMapping("/public/auth/register")
    public String registriere(@RequestBody LoginRequest userAuthDto) {

        Mitarbeiter mitarbeiter = new Mitarbeiter(userAuthDto.getUsername(), userAuthDto.getPassword());
        if (mitarbeiterService.saveUser(mitarbeiter)) {
            LOGGER.info("Registrierung war erfolgreich.");
            return login(userAuthDto);
        } else {
            LOGGER.warn("Benutzername ist bereits vergeben.");
            return "Der Benutzername ist bereits vergeben";
        }
    }

    /**
     * Gibt alle Mitarbeiter des Systems zurück.
     */
    @GetMapping("/mitarbeiter/get/all")
    public ResponseEntity<List<JsonNode>> getAlleMitarbeiter() {
        List<Mitarbeiter> all = mitarbeiterRepository.findAll();
        List<JsonNode> jsonListe = new ArrayList<>();

        for (Mitarbeiter mitarbeiter : all) {
            MitarbeiterDto mitarbeiterDto = MitarbeiterDto.builder()
                    .vorname(mitarbeiter.getVorname())
                    .nachname(mitarbeiter.getNachname())
                    .username(mitarbeiter.getUsername())
                    .build();
            jsonListe.add(objectMapper.valueToTree(mitarbeiterDto));
        }
        return ResponseEntity.ok(jsonListe);
    }
    
    @GetMapping("/auth/check")
    public void checkValidityOfToken() {
        LOGGER.info("Called auth/check successfully.");
    }
}
