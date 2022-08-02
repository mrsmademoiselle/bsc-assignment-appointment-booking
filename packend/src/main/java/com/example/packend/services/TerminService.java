package com.example.packend.services;

import com.example.packend.entities.Abwesenheit;
import com.example.packend.entities.Arbeitszeiten;
import com.example.packend.entities.Mitarbeiter;
import com.example.packend.entities.Termin;
import com.example.packend.repositories.AbwesenheitRepository;
import com.example.packend.repositories.ArbeitszeitenRepository;
import com.example.packend.repositories.TerminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TerminService {
    TerminRepository terminRepository;
    AbwesenheitRepository abwesenheitRepository;

    @Autowired
    ArbeitszeitenRepository arbeitszeitenRepository;

    @Autowired
    public TerminService(TerminRepository terminRepository, AbwesenheitRepository abwesenheitRepository) {
        this.terminRepository = terminRepository;
        this.abwesenheitRepository = abwesenheitRepository;
    }

    /**
     * Berechnet für den angegebenen Tag alle zur Terminbuchung verfügbaren Uhrzeiten.
     */
    public List<String> berechneVerfuegbareUhrzeitenFuerTag(LocalDate tag, Mitarbeiter ansprechpartner) {
        List<Termin> allByAusgewaehlterTermin = terminRepository.findAllByAusgewaehlterTermin(tag);
        List<String> alleBelegtenUhrzeitenDesTages = allByAusgewaehlterTermin.stream()
                .map(e -> String.valueOf(e.getUhrzeit().getHour()))
                .toList();

        Optional<Arbeitszeiten> byMitarbeiterId = arbeitszeitenRepository.findByMitarbeiterId(ansprechpartner.getUsername());
        Arbeitszeiten arbeitszeiten = null;
        if (byMitarbeiterId.isEmpty()) {
            arbeitszeiten = new Arbeitszeiten(ansprechpartner.getUsername());
            arbeitszeiten = arbeitszeitenRepository.save(arbeitszeiten);
        } else {
            arbeitszeiten = byMitarbeiterId.get();
        }

        List<String> verfuegbareUhrzeitenFuerTag = arbeitszeiten.getListeFuerTag(tag);
        // Weil die Liste verfuegbareUhrzeitenFuerTag nicht modifiable ist, wir aber die Elemente manipulieren wollen
        List<String> modifiableList = new ArrayList<>(verfuegbareUhrzeitenFuerTag);
        modifiableList.removeAll(alleBelegtenUhrzeitenDesTages);

        return modifiableList;

    }


    /**
     * Berechnet alle Tage, die komplett belegt wurden und somit im Kalender ausgegraut werden.
     */
    public List<LocalDate> berechneKomplettBelegteTage(Mitarbeiter mitarbeiter) {
        List<LocalDate> alleVollBelegtenTage = new ArrayList<>();
        Optional<Arbeitszeiten> byMitarbeiterId = arbeitszeitenRepository.findByMitarbeiterId(mitarbeiter.getUsername());
        Arbeitszeiten arbeitszeiten = null;
        if (byMitarbeiterId.isEmpty()) {
            arbeitszeiten = new Arbeitszeiten(mitarbeiter.getUsername());
            arbeitszeiten = arbeitszeitenRepository.save(arbeitszeiten);
        } else {
            arbeitszeiten = byMitarbeiterId.get();
        }

        List<Termin> alleTermineSortiert = terminRepository.findAllByOrderByAusgewaehlterTerminAsc();
        List<Abwesenheit> all = abwesenheitRepository.findAll();

        for (Abwesenheit abwesenheit : all) {
            LocalDate startDatum = abwesenheit.getStartDatum();
            LocalDate endDatum = abwesenheit.getEndDatum();

            for (LocalDate currentDate = startDatum; currentDate.isBefore(endDatum.plusDays(1)); currentDate = currentDate.plusDays(1)) {
                alleVollBelegtenTage.add(currentDate);
            }
        }

        LocalDate iterierDatum = null;
        // Über jedes Datum der Terminliste iterieren.
        for (Termin termin : alleTermineSortiert) {

            // wenn das Datum dasselbe ist wie das letzte, skippen wir dieses Datum, da wir es bereits geprüft haben
            if (iterierDatum != null && iterierDatum.equals(termin.getAusgewaehlterTermin())) {
                continue;
            }

            // alle weiteren Termine dieses Datums zusammensammeln
            List<Termin> termineDiesenDatums = alleTermineSortiert.stream()
                    .filter(t -> t.getAusgewaehlterTermin().equals(termin.getAusgewaehlterTermin())).toList();

            // alle Uhrzeiten in einer Liste sammeln und gegen verfuegbareUhrzeiten vergleichen
            List<LocalTime> alleUhrzeitenFuerDiesesDatum = termineDiesenDatums.stream().map(Termin::getUhrzeit).toList();
            List<String> verfuegbareUhrzeitenFuerTag = arbeitszeiten.getListeFuerTag(termin.getAusgewaehlterTermin());

            boolean istVollBelegterTag = alleUhrzeitenFuerDiesesDatum.stream()
                    .map(e -> String.valueOf(e.getHour()))
                    .toList()
                    .containsAll(verfuegbareUhrzeitenFuerTag);

            if (istVollBelegterTag)
                alleVollBelegtenTage.add(termin.getAusgewaehlterTermin());

            iterierDatum = termin.getAusgewaehlterTermin();

        }
        return alleVollBelegtenTage;
    }

}
