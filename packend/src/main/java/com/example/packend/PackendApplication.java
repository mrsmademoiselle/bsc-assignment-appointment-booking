package com.example.packend;

import com.example.packend.entities.Beratungsstelle;
import com.example.packend.entities.Kundeninformationen;
import com.example.packend.entities.Termin;
import com.example.packend.enums.Anrede;
import com.example.packend.enums.Beratungsgrund;
import com.example.packend.enums.Terminerinnerung;
import com.example.packend.repositories.BeratungsstellenRepository;
import com.example.packend.repositories.TerminRepository;
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
    BeratungsstellenRepository beratungsstellenRepository;
    @Autowired
    TerminRepository terminRepository;

    public static void main(String[] args) {
        SpringApplication.run(PackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Beratungsstelle preetz = Beratungsstelle.builder()
                .ansprechpartner("Jannika Loof")
                .hausnummer("5")
                .strasse("Mühlenstraße")
                .ort("Preetz")
                .plz("24211").build();
        Beratungsstelle eutin = Beratungsstelle.builder()
                .ansprechpartner("Claudia Loof")
                .hausnummer("36")
                .strasse("Lübecker Straße")
                .ort("Eutin")
                .plz("23701").build();

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
                    .geschlecht(Anrede.DIVERSE).build();
            Termin termin = Termin.builder()
                    .ausgewaehlterTermin(LocalDate.of(2022, i, i))
                    .bemerkung(generateString())
                    .beratungsgrund(Beratungsgrund.BERATUNG)
                    .terminerinnerungPerMail(Terminerinnerung.EINE_STUNDE)
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
                .geschlecht(Anrede.DIVERSE).build();
        for (int i = 9; i <= 18; i++) {
            Termin termin = Termin.builder()
                    .ausgewaehlterTermin(LocalDate.of(2022, 10, 10))
                    .bemerkung(generateString())
                    .beratungsgrund(Beratungsgrund.BERATUNG)
                    .terminerinnerungPerMail(Terminerinnerung.EINE_STUNDE)
                    .uhrzeit(LocalTime.of(i, 0))
                    .beratungsstelle(istEutin ? eutin : preetz)
                    .kundeninformationen(kundeninformationen).build();
            terminRepository.save(termin);
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

}
