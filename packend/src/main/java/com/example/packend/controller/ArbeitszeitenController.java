package com.example.packend.controller;

import com.example.packend.entities.Arbeitszeiten;
import com.example.packend.services.ArbeitszeitenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArbeitszeitenController {

    @Autowired
    ArbeitszeitenService arbeitszeitenService;

    /**
     * Erlaubt es dem Admin, seine für Termine verfügbaren Uhrzeiten pro Tag abzurufen.
     */
    @GetMapping("public/{mitarbeiterId}/uhrzeiten/get/all")
    public ResponseEntity<Arbeitszeiten> getAlleVerfuegbarenTagesUhrzeiten(@PathVariable String mitarbeiterId) {
        Arbeitszeiten arbeitszeiten = arbeitszeitenService.getOrCreateArbeitszeiten(mitarbeiterId);
        return ResponseEntity.ok(arbeitszeiten);
    }

    /**
     * Erlaubt es dem Admin, seine für Termine verfügbaren Uhrzeiten pro Tag anzupassen.
     */
    @PostMapping("admin/uhrzeiten/post")
    public ResponseEntity<Arbeitszeiten> postSetzeVerfuegbareTagesUhrzeiten(@RequestBody Arbeitszeiten arbeitszeiten) {
        arbeitszeiten = arbeitszeitenService.aktualisiereArbeitszeiten(arbeitszeiten);
        return ResponseEntity.ok(arbeitszeiten);
    }
}
