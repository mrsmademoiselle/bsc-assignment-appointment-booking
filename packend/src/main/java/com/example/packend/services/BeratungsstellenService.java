package com.example.packend.services;

import com.example.packend.entities.Beratungsstelle;
import com.example.packend.repositories.BeratungsstellenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeratungsstellenService {

    @Autowired
    BeratungsstellenRepository beratungsstellenRepository;

    public void archiviereBeratungsstelle(String stringId) {
        Long id = Long.valueOf(stringId);
        Beratungsstelle beratungsstelle = beratungsstellenRepository.findById(id).orElseThrow(RuntimeException::new);
        if (beratungsstelle.isIstArchiviert()) {
            throw new RuntimeException("Beratungsstelle ist bereits archiviert!");
        } else {
            beratungsstelle.setIstArchiviert(true);
            beratungsstellenRepository.save(beratungsstelle);
        }
    }
}
