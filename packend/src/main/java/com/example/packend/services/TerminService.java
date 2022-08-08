package com.example.packend.services;

import com.example.packend.entities.*;
import com.example.packend.repositories.AbsageLinkRepository;
import com.example.packend.repositories.AbwesenheitRepository;
import com.example.packend.repositories.ArbeitszeitenRepository;
import com.example.packend.repositories.TerminRepository;
import com.example.packend.services.mail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class TerminService {
    EmailService emailService;
    TerminRepository terminRepository;
    AbwesenheitRepository abwesenheitRepository;
    ArbeitszeitenRepository arbeitszeitenRepository;
    AbsageLinkRepository absageLinkRepository;
    MitarbeiterService mitarbeiterService;

    @Autowired
    public TerminService(TerminRepository terminRepository, AbwesenheitRepository abwesenheitRepository, EmailService emailService, ArbeitszeitenRepository arbeitszeitenRepository, AbsageLinkRepository absageLinkRepository, MitarbeiterService mitarbeiterService) {
        this.terminRepository = terminRepository;
        this.abwesenheitRepository = abwesenheitRepository;
        this.emailService = emailService;
        this.arbeitszeitenRepository = arbeitszeitenRepository;
        this.absageLinkRepository = absageLinkRepository;
        this.mitarbeiterService = mitarbeiterService;
    }

    public List<String> getVerfuegbareUhrzeitenFuerTerminUndMitarbeiter(String jahr, String monat, String tag, String mitarbeiterId) {
        Mitarbeiter mitarbeiter = mitarbeiterService.findMitarbeiter(mitarbeiterId);

        List<String> verfuegbareUhrzeitenFuerTag = berechneVerfuegbareUhrzeitenFuerTag(
                LocalDate.of(Integer.parseInt(jahr), Integer.parseInt(monat), Integer.parseInt(tag)), mitarbeiter);
        return verfuegbareUhrzeitenFuerTag;
    }

    /**
     * Berechnet für den angegebenen Tag alle zur Terminbuchung verfügbaren Uhrzeiten.
     */
    public List<String> berechneVerfuegbareUhrzeitenFuerTag(LocalDate tag, Mitarbeiter ansprechpartner) {
        List<Termin> allByAusgewaehlterTermin = terminRepository.findAllByAusgewaehlterTermin(tag);
        List<String> alleBelegtenUhrzeitenDesTages = allByAusgewaehlterTermin.stream()
                .map(e -> String.valueOf(e.getUhrzeit().getHour()))
                .toList();

        Arbeitszeiten arbeitszeiten = getArbeitszeitenFuerMitarbeiter(ansprechpartner);

        List<String> verfuegbareUhrzeitenFuerTag = arbeitszeiten.getListeFuerTag(tag);
        // Weil die Liste verfuegbareUhrzeitenFuerTag nicht modifiable ist, wir aber die Elemente manipulieren wollen
        List<String> modifiableList = new ArrayList<>(verfuegbareUhrzeitenFuerTag);
        modifiableList.removeAll(alleBelegtenUhrzeitenDesTages);

        return modifiableList;

    }


    /**
     * Berechnet alle Tage, die komplett belegt wurden und somit im Kalender ausgegraut werden.
     */
    public List<LocalDate> berechneKomplettBelegteTage(String mitarbeiterId) {
        Mitarbeiter mitarbeiter = mitarbeiterService.findMitarbeiter(mitarbeiterId);
        List<LocalDate> alleVollBelegtenTage = new ArrayList<>();
        Arbeitszeiten arbeitszeiten = getArbeitszeitenFuerMitarbeiter(mitarbeiter);

        List<Termin> alleTermineSortiert = terminRepository.findAllByOrderByAusgewaehlterTerminAsc();
        List<Abwesenheit> alleAbwesenheiten = abwesenheitRepository.findAll();

        // Alle Tage mit Abwesenheiten sind "komplett belegte Tage"
        for (Abwesenheit abwesenheit : alleAbwesenheiten) {
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

    public Termin saveTermin(Termin data) {
        data = terminRepository.save(data); // muss zuerst gespeichert werden, damit es eine ID erhält, über die wir die CancellationURL generieren können
        AbsageLink absageLink = generateOneTimeUrl(data);
        emailService.sendeTerminbestaetigung(data, absageLink);
        return data;
    }

    private AbsageLink generateOneTimeUrl(Termin termin) {
        String randomString = generateString();
        AbsageLink absageLink = new AbsageLink(termin.getId(), randomString);
        absageLink = absageLinkRepository.save(absageLink);
        return absageLink;
    }

    public boolean cancelAppointment(Long id) {
        Optional<Termin> terminOptional = terminRepository.findById(id);
        if (terminOptional.isPresent()) {
            // remove cancellationUrl
            absageLinkRepository.deleteByTerminId(id);
            // remove appointment
            terminRepository.delete(terminOptional.get());
            // send cancellation-Email
            emailService.sendeTerminabsage(terminOptional.get());
            return true;
        } else {
            return false;
        }
    }

    public Optional<Termin> getTerminByCancellationToken(String token) {
        Optional<AbsageLink> cancellationUrlOptional = absageLinkRepository.findByToken(token);

        if (cancellationUrlOptional.isPresent()) {
            return terminRepository.findById(cancellationUrlOptional.get().getTerminId());
        } else {
            return Optional.empty();
        }
    }

    private String generateString() {
        Random rng = new Random();
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        int length = 40;
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    private Arbeitszeiten getArbeitszeitenFuerMitarbeiter(Mitarbeiter mitarbeiter) {
        Optional<Arbeitszeiten> optionalArbeitszeiten = arbeitszeitenRepository.findByMitarbeiterId(mitarbeiter.getUsername());
        Arbeitszeiten arbeitszeiten = null;

        if (optionalArbeitszeiten.isEmpty()) {
            arbeitszeiten = new Arbeitszeiten(mitarbeiter.getUsername());
            return arbeitszeitenRepository.save(arbeitszeiten);
        } else {
            return optionalArbeitszeiten.get();
        }
    }

    public List<Termin> findAll() {
        deleteTermineInVergangenheit();
        return terminRepository.findAll();
    }

    private void deleteTermineInVergangenheit() {
        List<Termin> termineInVergangenheit = terminRepository.findAllByAusgewaehlterTerminBefore(LocalDate.now().minusDays(1));
        terminRepository.deleteAll(termineInVergangenheit);
    }
}