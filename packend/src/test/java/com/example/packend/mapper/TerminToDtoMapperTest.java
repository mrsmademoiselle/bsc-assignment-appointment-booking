package com.example.packend.mapper;

import com.example.packend.dto.TerminDto;
import com.example.packend.entities.Beratungsstelle;
import com.example.packend.entities.Kundeninformationen;
import com.example.packend.entities.Termin;
import com.example.packend.enums.Anrede;
import com.example.packend.enums.Beratungsgrund;
import com.example.packend.enums.Terminerinnerung;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class TerminToDtoMapperTest {

    TerminToDtoMapper terminToDtoMapper = new TerminToDtoMapper();

    @Test
    public void terminToDto_happyPath() {
        Beratungsstelle beratungsstelle = createBeratungsstelle();
        Termin termin = createTermin(beratungsstelle);

        TerminDto terminDto = terminToDtoMapper.terminToDto(termin);

        TerminDto expected = TerminDto.builder()
                .ausgewaehlterTermin(LocalDate.of(1999, 1, 13).toString())
                .bemerkung("arghhh")
                .beratungsgrund(Beratungsgrund.ERSTBERATUNG)
                .beratungsstelle(beratungsstelle)
                .bereitsMitglied("true")
                .email("asd@asd.de")
                .geschlecht(Anrede.FEMALE.getAnrede())
                .nachname("nya")
                .vorname("ayn")
                .telefon("1234567890")
                .terminerinnerungPerMail(Terminerinnerung.KEINE)
                .uhrzeit(LocalTime.of(13, 30).toString())
                .build();

        assertThat(terminDto).isEqualTo(expected);
    }

    @Test
    public void terminToDto_missingField() {
        Beratungsstelle beratungsstelle = createBeratungsstelle();
        Termin termin = createTermin(beratungsstelle);

        TerminDto terminDto = terminToDtoMapper.terminToDto(termin);

        TerminDto expected = TerminDto.builder()
                .ausgewaehlterTermin(LocalDate.of(1999, 1, 13).toString())
                // bemerkung is missing
                .beratungsgrund(Beratungsgrund.ERSTBERATUNG)
                .beratungsstelle(beratungsstelle)
                .bereitsMitglied("true")
                .email("asd@asd.de")
                .geschlecht(Anrede.FEMALE.getAnrede())
                .nachname("nya")
                .vorname("ayn")
                .telefon("1234567890")
                .terminerinnerungPerMail(Terminerinnerung.KEINE)
                .uhrzeit(LocalTime.of(13, 30).toString())
                .build();

        assertThat(terminDto).isNotEqualTo(expected);
    }

    private Termin createTermin(Beratungsstelle beratungsstelle) {
        return Termin.builder()
                .ausgewaehlterTermin(LocalDate.of(1999, 1, 13))
                .bemerkung("arghhh")
                .beratungsgrund(Beratungsgrund.ERSTBERATUNG)
                .beratungsstelle(beratungsstelle)
                .kundeninformationen(
                        Kundeninformationen.builder()
                                .bereitsMitglied(true)
                                .email("asd@asd.de")
                                .geschlecht(Anrede.FEMALE)
                                .nachname("nya")
                                .vorname("ayn")
                                .telefon("1234567890")
                                .build())
                .terminerinnerungPerMail(Terminerinnerung.KEINE)
                .uhrzeit(LocalTime.of(13, 30))
                .build();
    }

    private Beratungsstelle createBeratungsstelle() {
        Beratungsstelle beratungsstelle = Beratungsstelle.builder()
                .strasse("Lübecker Straße")
                .ansprechpartner("Ich")
                .hausnummer("11")
                .ort("HH")
                .plz("12345")
                .build();
        return beratungsstelle;
    }

}