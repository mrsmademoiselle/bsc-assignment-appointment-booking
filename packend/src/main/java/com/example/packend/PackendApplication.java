package com.example.packend;

import com.example.packend.entities.*;
import com.example.packend.enums.Anrede;
import com.example.packend.enums.Beratungsgrund;
import com.example.packend.repositories.BeratungsstellenRepository;
import com.example.packend.repositories.TerminRepository;
import com.example.packend.services.MitarbeiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

@Configuration
@SpringBootApplication
public class PackendApplication implements CommandLineRunner {

    @Autowired
    MitarbeiterService mitarbeiterService;
    @Autowired
    BeratungsstellenRepository beratungsstellenRepository;
    @Autowired
    TerminRepository terminRepository;

    public static void main(String[] args) {
        SpringApplication.run(PackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Default AdminUser
        Mitarbeiter adminMitarbeiter = Mitarbeiter.builder().password("adminPass").username("admin").nachname("admin nachname").vorname("admin vorname").build();
        mitarbeiterService.saveUser(adminMitarbeiter);

        Mitarbeiter janni = Mitarbeiter.builder().vorname("Jannika").nachname("Loof").username("janni").password("janni").build();
        mitarbeiterService.saveUser(janni);

        Beratungsstelle preetz = Beratungsstelle.builder()
                .ansprechpartner(adminMitarbeiter)
                .adresse(Adresse.builder().hausnummer("5")
                        .strasse("Mühlenstraße")
                        .ort("Preetz")
                        .plz("24211").build()).build();
        Mitarbeiter claudia = Mitarbeiter.builder().vorname("Claudia").nachname("Loof").username("claudi").password("claudi").build();
        mitarbeiterService.saveUser(claudia);

        Beratungsstelle eutin = Beratungsstelle.builder()
                .ansprechpartner(adminMitarbeiter)
                .adresse(Adresse.builder().hausnummer("36")
                        .strasse("Lübecker Straße")
                        .ort("Eutin")
                        .plz("23701").build()).build();

        beratungsstellenRepository.save(eutin);
        beratungsstellenRepository.save(preetz);

        boolean istEutin = false;
        for (int i = 1; i <= 11; i++) {
            Kundeninformationen kundeninformationen = Kundeninformationen.builder()
                    .bereitsMitglied(istEutin)
                    .email("franziska.loof@web.de")
                    .nachname("Loof")
                    .vorname("Claudia" + i)
                    .telefon("1234567" + i)
                    .anrede(Anrede.DIVERS).build();
            Termin termin = Termin.builder()
                    .ausgewaehlterTermin(LocalDate.of(2022, i, i))
                    .bemerkung(generateString())
                    .beratungsgrund(Beratungsgrund.BERATUNG)
                    .uhrzeit(LocalTime.of(i, 0))
                    .beratungsstelle(istEutin ? eutin : preetz)
                    .kundeninformationen(kundeninformationen).build();

            terminRepository.save(termin);
            istEutin = !istEutin;
        }
        Kundeninformationen kundeninformationen = Kundeninformationen.builder()
                .bereitsMitglied(istEutin)
                .email("franziska.loof@web.de")
                .nachname("Loof")
                .vorname("Claudia")
                .telefon("1234567")
                .anrede(Anrede.DIVERS).build();
        for (int i = 9; i <= 18; i++) {
            Termin termin = Termin.builder()
                    .ausgewaehlterTermin(LocalDate.of(2022, 10, 10))
                    .bemerkung(generateString())
                    .beratungsgrund(Beratungsgrund.BERATUNG)
                    .uhrzeit(LocalTime.of(i, 0))
                    .beratungsstelle(istEutin ? eutin : preetz)
                    .kundeninformationen(kundeninformationen).build();
            terminRepository.save(termin);
        }
        Termin termin = Termin.builder()
                .ausgewaehlterTermin(LocalDate.now())
                .bemerkung(generateString())
                .beratungsgrund(Beratungsgrund.BERATUNG)
                .uhrzeit(LocalTime.now())
                .beratungsstelle(istEutin ? eutin : preetz)
                .kundeninformationen(kundeninformationen).build();
        terminRepository.save(termin);


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

}
