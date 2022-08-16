package com.example.packend.services;

import com.example.packend.entities.*;
import com.example.packend.repositories.AbsageLinkRepository;
import com.example.packend.repositories.AbwesenheitRepository;
import com.example.packend.repositories.ArbeitszeitenRepository;
import com.example.packend.repositories.TerminRepository;
import com.example.packend.services.mail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    /**
     * Gibt alle Uhrzeiten an dem Tag zurück, an denen der Mitarbeiter noch keine Termine hat,
     * sofern diese Uhrzeiten als Terminbuchungszeiten zugelassen sind.
     * <p>
     * Wird ausgeführt, wenn ein Kunde ein Datum bei der Terminbuchung auswählt, um die verfügbaren Uhrzeiten für das Datum zu berechnen.
     */
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
        List<Termin> alleTermineAnTagFuerMitarbeiter = terminRepository.findAllByAusgewaehlterTerminAndBeratungsstelle_Ansprechpartner(tag, ansprechpartner);
        List<String> alleBereitsBelegtenUhrzeitenAnTag = alleTermineAnTagFuerMitarbeiter.stream()
                .map(e -> String.valueOf(e.getUhrzeit().getHour()))
                .toList();
        Arbeitszeiten arbeitszeiten = getArbeitszeitenFuerMitarbeiter(ansprechpartner);

        List<String> verfuegbareUhrzeitenFuerTag = arbeitszeiten.getListeFuerTag(tag);

        // Weil die Liste verfuegbareUhrzeitenFuerTag nicht modifiable ist, wir aber die Elemente manipulieren wollen
        List<String> modifiableList = new ArrayList<>(verfuegbareUhrzeitenFuerTag);
        // Entferne alle belegten Uhrzeiten aus der Liste der verfügbaren Uhrzeiten
        modifiableList.removeAll(alleBereitsBelegtenUhrzeitenAnTag);
        // Alle noch nicht belegten Uhrzeiten für Tag zurückgeben
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
        List<Abwesenheit> alleAbwesenheitenDesMitarbeiters = abwesenheitRepository.findAllByMitarbeiter_Username(mitarbeiterId, Sort.by("startDatum").ascending());

        // Alle Tage, an denen eine Abwesenheit existiert, gelten als "komplett belegte Tage" und müssen als solche eingetragen werdens
        for (Abwesenheit abwesenheit : alleAbwesenheitenDesMitarbeiters) {
            LocalDate startDatum = abwesenheit.getStartDatum();
            LocalDate endDatum = abwesenheit.getEndDatum();

            // Für jeden Tag dieses Abwesenheitseintrags wird das jeweilige Datum in der Liste gespeichert
            for (LocalDate currentDate = startDatum; currentDate.isBefore(endDatum.plusDays(1)); currentDate = currentDate.plusDays(1)) {
                alleVollBelegtenTage.add(currentDate);
            }
        }

        LocalDate iterierDatum = null;
        // Wenn alle verfügbaren Uhrzeiten an einem Tag mit Terminen belegt sind, wird dieser Tag zur Liste hinzugefügt.
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

    /**
     * Speichert einen Termin in der Datenbank, wenn noch kein Termin an dem Tag zu derselben Uhrzeit existiert.
     */
    public Termin saveTermin(Termin data) {
        List<Termin> ueberschneidendeTermine = terminRepository.findAllByAusgewaehlterTerminAndUhrzeit(data.getAusgewaehlterTermin(), data.getUhrzeit());

        if (!ueberschneidendeTermine.isEmpty())
            throw new RuntimeException("Es gibt bereits einen Termin zu diesem Zeitpunkt.");

        data = terminRepository.save(data); // muss zuerst gespeichert werden, damit es eine ID erhält, über die wir die CancellationURL generieren können
        AbsageLink absageLink = generiereUndSpeichereAbsageLink(data);
        emailService.sendeTerminbestaetigung(data, absageLink);
        return data;
    }

    /**
     * Sagt einen Termin ab, indem
     * - der Termin gelöscgt wird
     * - eine Absage-Email rausgeschickt wird
     * - der AbsageLink gelöscht wird
     */
    public boolean sageTerminAb(Long id) {
        Optional<Termin> terminOptional = terminRepository.findById(id);

        if (terminOptional.isPresent()) {
            emailService.sendeTerminabsage(terminOptional.get());
            absageLinkRepository.deleteByTerminId(id);
            terminRepository.delete(terminOptional.get());
            return true;
        } else {
            return false;
        }
    }

    public List<Termin> findAll() {
        deleteTermineInVergangenheit();
        return terminRepository.findAll();
    }

    /**
     * Findet den Termin für das Absage-Token
     */
    public Optional<Termin> getTerminMitAbsageToken(String token) {
        Optional<AbsageLink> absageLinkOptional = absageLinkRepository.findByToken(token);

        if (absageLinkOptional.isPresent()) {
            return terminRepository.findById(absageLinkOptional.get().getTerminId());
        } else {
            return Optional.empty();
        }
    }

    private AbsageLink generiereUndSpeichereAbsageLink(Termin termin) {
        String randomString = generiereRandomString();
        AbsageLink absageLink = new AbsageLink(termin.getId(), randomString);
        absageLink = absageLinkRepository.save(absageLink);
        return absageLink;
    }

    private String generiereRandomString() {
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

    private void deleteTermineInVergangenheit() {
        List<Termin> termineInVergangenheit = terminRepository.findAllByAusgewaehlterTerminBefore(LocalDate.now().minusDays(1));
        terminRepository.deleteAll(termineInVergangenheit);
    }
}