package com.example.packend.entities;

import com.example.packend.enums.Beratungsgrund;
import com.example.packend.enums.Terminerinnerung;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Termin {

    @Id
    @NotNull
    private String id;

    @Embedded
    private Kundeninformationen kundeninformationen;
    @NotNull
    private String ort;
    private String bemerkung;
    private Terminerinnerung terminerinnerungPerMail;
    @Embedded
    private Beratungsstelle beratungsstelle;

    @NotNull
    private Beratungsgrund beratungsgrund;
    @NotNull
    private LocalDate ausgewaehlterTermin;
    @NotNull
    private LocalTime uhrzeit;

    public String getId() {
        return id;
    }
}
