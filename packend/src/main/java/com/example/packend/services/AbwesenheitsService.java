package com.example.packend.services;

import com.example.packend.dto.AbwesenheitDto;
import com.example.packend.entities.Abwesenheit;
import com.example.packend.entities.Termin;
import com.example.packend.repositories.AbwesenheitRepository;
import com.example.packend.repositories.MitarbeiterRepository;
import com.example.packend.repositories.TerminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AbwesenheitsService {
    @Autowired
    AbwesenheitRepository abwesenheitRepository;
    @Autowired
    MitarbeiterRepository mitarbeiterRepository;
    @Autowired
    TerminRepository terminRepository;

    public Optional<Abwesenheit> save(AbwesenheitDto abwesenheitDto) {
        LocalDate startDatum = abwesenheitDto.getStartDatum();
        LocalDate endDatum = abwesenheitDto.getEndDatum();

        boolean nochKeineEintraegeInZeitraum = abwesenheitRepository.findAllByMitarbeiter_UsernameAndStartDatumBetween(abwesenheitDto.getMitarbeiterId(), startDatum, endDatum).isEmpty()
                && abwesenheitRepository.findAllByMitarbeiter_UsernameAndEndDatumBetween(abwesenheitDto.getMitarbeiterId(), startDatum, endDatum).isEmpty();

        System.out.println("Einträge in Zeitraum ? " + nochKeineEintraegeInZeitraum);
        if (nochKeineEintraegeInZeitraum) {
            List<Termin> termineWaehrendAbwesenheit = terminRepository.findAllByBeratungsstelle_Ansprechpartner_UsernameAndAusgewaehlterTerminBetween(abwesenheitDto.getMitarbeiterId(), startDatum, endDatum);
            System.out.println("Termine während Abwesenheit ? " + termineWaehrendAbwesenheit.size());
            if (!termineWaehrendAbwesenheit.isEmpty()) {
                return Optional.empty();
            }
            Abwesenheit abwesenheit = Abwesenheit.builder()
                    .mitarbeiter(mitarbeiterRepository.findByUsername(abwesenheitDto.getMitarbeiterId()).orElseThrow(RuntimeException::new))
                    .startDatum(abwesenheitDto.getStartDatum())
                    .endDatum(abwesenheitDto.getEndDatum()).build();

            System.out.println("abwesenheit: " + abwesenheit);
            return Optional.of(abwesenheitRepository.save(abwesenheit));
        } else {
            return Optional.empty();
        }
    }
}
