package com.example.packend.services;

import com.example.packend.dto.VerfuegbareUhrzeitenDto;
import com.example.packend.entities.Termin;
import com.example.packend.repositories.TerminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TerminService {
    TerminRepository terminRepository;
    AdminConfigurationService adminConfigurationService;

    @Autowired
    public TerminService(TerminRepository terminRepository, AdminConfigurationService adminConfigurationService) {
        this.terminRepository = terminRepository;
        this.adminConfigurationService = adminConfigurationService;
    }

    /**
     * Berechnet für den angegebenen Tag alle zur Terminbuchung freien Uhrzeiten.
     *
     * @param tag
     * @return
     */
    public List<String> berechneVerfuegbareUhrzeitenFuerTag(LocalDate tag) {
        List<Termin> allByAusgewaehlterTermin = terminRepository.findAllByAusgewaehlterTermin(tag);
        List<String> alleBelegtenUhrzeitenDesTages = allByAusgewaehlterTermin.stream()
                .map(e -> String.valueOf(e.getUhrzeit().getHour()))
                .toList();

        List<String> verfuegbareUhrzeitenFuerTag = adminConfigurationService.getAlleVerfuegbarenUhrzeiten().getListeFuerTag(tag);
        // Weil die Liste verfuegbareUhrzeitenFuerTag nicht modifiable ist, wir aber die Elemente manipulieren wollen
        List<String> modifiableList = new ArrayList<>(verfuegbareUhrzeitenFuerTag);
        modifiableList.removeAll(alleBelegtenUhrzeitenDesTages);

        return modifiableList;

    }

    /*
    - Über jedes Datum der Terminliste iterieren.
    - für jedes Datum alle weiteren Termine dieses Datums zusammensammeln
    - alle Uhrzeiten in einer Liste sammeln und gegen verfuegbareUhrzeiten vergleichen
     */

    /**
     * Berechnet alle Tage, die komplett belegt wurden und somit im Kalender ausgegraut werden.
     */
    public List<LocalDate> berechneKomplettBelegteTage() {
        List<LocalDate> alleVollBelegtenTage = new ArrayList<>();
        List<Termin> alleTermineSortiert = terminRepository.findAllByOrderByAusgewaehlterTerminAsc();

        VerfuegbareUhrzeitenDto alleVerfuegbarenUhrzeiten = adminConfigurationService.getAlleVerfuegbarenUhrzeiten();

        LocalDate iterierDatum = null;
        //     - Über jedes Datum der Terminliste iterieren.
        for (Termin termin : alleTermineSortiert) {

            // wenn die Datümer identisch sind, skippen wir sie, da wir sie nur einmal prüfen müssen
            if (iterierDatum != null && iterierDatum.equals(termin.getAusgewaehlterTermin())) {
                continue;
            }
            // - für jedes Datum alle weiteren Termine dieses Datums zusammensammeln
            List<Termin> termineDiesenDatums = alleTermineSortiert.stream()
                    .filter(t -> t.getAusgewaehlterTermin().equals(termin.getAusgewaehlterTermin())).toList();

            // - alle Uhrzeiten in einer Liste sammeln und gegen verfuegbareUhrzeiten vergleichen
            List<LocalTime> alleUhrzeitenFuerDiesesDatum = termineDiesenDatums.stream().map(Termin::getUhrzeit).toList();
            List<String> verfuegbareUhrzeitenFuerTag = alleVerfuegbarenUhrzeiten.getListeFuerTag(termin.getAusgewaehlterTermin());

            boolean istVollBelegterTag = alleUhrzeitenFuerDiesesDatum.stream()
                    .map(e -> String.valueOf(e.getHour()))
                    .toList()
                    .containsAll(verfuegbareUhrzeitenFuerTag);

            if (istVollBelegterTag) alleVollBelegtenTage.add(termin.getAusgewaehlterTermin());

            iterierDatum = termin.getAusgewaehlterTermin();

        }
        return alleVollBelegtenTage;
    }
}
