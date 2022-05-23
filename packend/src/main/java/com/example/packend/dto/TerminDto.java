package com.example.packend.dto;

import com.example.packend.entities.Termin;
import com.example.packend.enums.Beratungsgrund;
import com.example.packend.enums.Terminerinnerung;
import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class TerminDto {
    @NotNull
    private String id;
    @NotNull
    private String vorname;
    @NotNull
    private String nachname;
    @NotNull
    private String email;
    @NotNull
    private String telefon;
    @NotNull
    private String ort;
    @NotNull
    private String bemerkung;
    @NotNull
    private Terminerinnerung terminerinnerungPerMail;
    @NotNull
    private BeratungsstelleDto beratungsstelle;
    @NotNull
    private String bereitsMitglied;
    @NotNull
    private Beratungsgrund beratungsgrund;
    @NotNull
    private String ausgewaehlterTermin;
    @NotNull
    private String uhrzeit;

    public TerminDto(Termin termin) {
        this.id = termin.getId();
        this.vorname = termin.getKundeninformationen().getVorname();
        this.nachname = termin.getKundeninformationen().getNachname();
        this.email = termin.getKundeninformationen().getEmail();
        this.telefon = termin.getKundeninformationen().getTelefon();
        this.ort = termin.getOrt();
        this.bemerkung = termin.getBemerkung();
        this.terminerinnerungPerMail = termin.getTerminerinnerungPerMail();
        this.beratungsstelle = new BeratungsstelleDto(termin.getBeratungsstelle() != null ? termin.getBeratungsstelle().getId() : "");
        this.bereitsMitglied = String.valueOf(termin.getKundeninformationen().isBereitsMitglied());
        this.beratungsgrund = termin.getBeratungsgrund();
        this.ausgewaehlterTermin = termin.getAusgewaehlterTermin().toString();
        this.uhrzeit = termin.getUhrzeit().toString();
    }
}
