package com.example.packend.services;

import com.example.packend.config.VerfuegbareUhrzeitenConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminConfigurationService {
    @Autowired
    VerfuegbareUhrzeitenConfiguration verfuegbareUhrzeitenConfiguration;

    public List<String> getAlleVerfuegbarenUhrzeiten() {
        System.out.println(verfuegbareUhrzeitenConfiguration.getMontag());
        return verfuegbareUhrzeitenConfiguration.getMontag();
    }
}
