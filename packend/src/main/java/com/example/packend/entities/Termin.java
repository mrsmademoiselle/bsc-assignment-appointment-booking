package com.example.packend.entities;

import com.example.packend.enums.Beratungsgrund;
import com.example.packend.enums.Terminerinnerung;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Termin {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @NotNull
    private Long id;
    @Embedded
    private Kundeninformationen kundeninformationen;
    private String bemerkung;
    private Terminerinnerung terminerinnerungPerMail;
    @NotNull
    private Beratungsgrund beratungsgrund;
    @NotNull
    private LocalDate ausgewaehlterTermin;
    @NotNull
    private LocalTime uhrzeit;

    @NotNull
    @OneToOne
    private Beratungsstelle beratungsstelle;

    public Long getId() {
        return id;
    }
}
