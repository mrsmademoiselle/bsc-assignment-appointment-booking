package com.example.packend.services;

import com.example.packend.config.VerfuegbareUhrzeitenConfiguration;
import com.example.packend.dto.VerfuegbareUhrzeitenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminConfigurationService {
    @Autowired
    VerfuegbareUhrzeitenConfiguration verfuegbareUhrzeitenConfiguration;

    public VerfuegbareUhrzeitenDto getAlleVerfuegbarenUhrzeiten() {
        VerfuegbareUhrzeitenDto verfuegbareUhrzeitenDto = new VerfuegbareUhrzeitenDto();
        verfuegbareUhrzeitenDto.setMontag(verfuegbareUhrzeitenConfiguration.getMontag());
        verfuegbareUhrzeitenDto.setDienstag(verfuegbareUhrzeitenConfiguration.getDienstag());
        verfuegbareUhrzeitenDto.setMittwoch(verfuegbareUhrzeitenConfiguration.getMittwoch());
        verfuegbareUhrzeitenDto.setDonnerstag(verfuegbareUhrzeitenConfiguration.getDonnerstag());
        verfuegbareUhrzeitenDto.setFreitag(verfuegbareUhrzeitenConfiguration.getFreitag());

        return verfuegbareUhrzeitenDto;
    }
}
