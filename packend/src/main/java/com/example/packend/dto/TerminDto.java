package com.example.packend.dto;

import com.example.packend.entities.Beratungsstelle;
import com.example.packend.enums.Beratungsgrund;
import com.example.packend.enums.Terminerinnerung;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class TerminDto {
    // Kundeninformationen
    private Long id;
    private String vorname;
    private String nachname;
    private String geschlecht;
    private String email;
    private String telefon;
    private String bereitsMitglied;

    //Termindetails
    private String bemerkung;
    private Terminerinnerung terminerinnerungPerMail;
    private Beratungsgrund beratungsgrund;
    private String ausgewaehlterTermin;
    private String uhrzeit;

    // Beratungsstelle
    private Beratungsstelle beratungsstelle;
}
