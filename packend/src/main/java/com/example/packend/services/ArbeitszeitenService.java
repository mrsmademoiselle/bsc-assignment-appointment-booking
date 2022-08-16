package com.example.packend.services;

import com.example.packend.entities.Arbeitszeiten;
import com.example.packend.repositories.ArbeitszeitenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArbeitszeitenService {
    @Autowired
    ArbeitszeitenRepository arbeitszeitenRepository;

    public Arbeitszeiten aktualisiereArbeitszeiten(Arbeitszeiten arbeitszeiten) {
        return arbeitszeitenRepository.save(arbeitszeiten);
    }

    public Arbeitszeiten getOrCreateArbeitszeiten(String mitarbeiterId) {
        Optional<Arbeitszeiten> arbeitszeitenFuerMitarbeiter = arbeitszeitenRepository.findByMitarbeiterId(mitarbeiterId);

        // Es kann sein, dass er noch nie welche eingetragen hat. In dem Fall werden ihm default-Werte  zugeordnet
        if (arbeitszeitenFuerMitarbeiter.isEmpty()) {
            Arbeitszeiten arbeitszeiten = new Arbeitszeiten(mitarbeiterId);
            return arbeitszeitenRepository.save(arbeitszeiten);
        } else {
            return arbeitszeitenFuerMitarbeiter.get();
        }
    }
}
