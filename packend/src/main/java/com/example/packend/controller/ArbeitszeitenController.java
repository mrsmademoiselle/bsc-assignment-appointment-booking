package com.example.packend.controller;

import com.example.packend.entities.Arbeitszeiten;
import com.example.packend.repositories.ArbeitszeitenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ArbeitszeitenController {

    @Autowired
    ArbeitszeitenRepository arbeitszeitenRepository;

    /**
     * Erlaubt es dem Admin, seine für Termine verfügbaren Uhrzeiten pro Tag abzurufen.
     */
    @GetMapping("public/{mitarbeiterId}/uhrzeiten/get/all")
    public ResponseEntity<Arbeitszeiten> getAlleVerfuegbarenTagesUhrzeiten(@PathVariable String mitarbeiterId) {

        Optional<Arbeitszeiten> byMitarbeiterId = arbeitszeitenRepository.findByMitarbeiterId(mitarbeiterId);
        Arbeitszeiten arbeitszeiten = null;
        if (byMitarbeiterId.isEmpty()) {
            arbeitszeiten = new Arbeitszeiten(mitarbeiterId);
            arbeitszeiten = arbeitszeitenRepository.save(arbeitszeiten);
        } else {
            arbeitszeiten = byMitarbeiterId.get();
        }
        return ResponseEntity.ok(arbeitszeiten);
    }

    /**
     * Erlaubt es dem Admin, seine für Termine verfügbaren Uhrzeiten pro Tag anzupassen.
     */
    @PostMapping("admin/uhrzeiten/post")
    public ResponseEntity<Arbeitszeiten> postSetzeVerfuegbareTagesUhrzeiten(@RequestBody Arbeitszeiten arbeitszeiten) {
        arbeitszeiten = arbeitszeitenRepository.save(arbeitszeiten);
        return ResponseEntity.ok(arbeitszeiten);
    }
}
