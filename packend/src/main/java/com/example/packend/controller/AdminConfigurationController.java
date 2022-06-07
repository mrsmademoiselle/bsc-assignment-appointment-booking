package com.example.packend.controller;

import com.example.packend.config.VerfuegbareUhrzeitenConfiguration;
import com.example.packend.dto.VerfuegbareUhrzeitenDto;
import com.example.packend.services.AdminConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminConfigurationController {
    @Autowired
    AdminConfigurationService adminConfigurationService;

    @GetMapping("public/admin/uhrzeiten/get/all")
    public ResponseEntity<VerfuegbareUhrzeitenDto> getAlleVerfuegbarenTagesUhrzeiten() {
        return ResponseEntity.ok(adminConfigurationService.getAlleVerfuegbarenUhrzeiten());
    }

    @PostMapping("admin/uhrzeiten/post")
    public ResponseEntity<String> postSetzeVerfuegbareTagesUhrzeiten(@RequestBody VerfuegbareUhrzeitenConfiguration verfuegbareUhrzeitenConfiguration) {
        return ResponseEntity.ok("");
    }
}
