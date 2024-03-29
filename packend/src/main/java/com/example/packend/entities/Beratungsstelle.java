package com.example.packend.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Beratungsstelle {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @OneToOne
    private Mitarbeiter ansprechpartner;
    @Embedded
    private Adresse adresse;
    @OneToMany
    private List<Mitarbeiter> mitarbeiterListe;
    private boolean istArchiviert;
    
    public String getFormattedAnsprechpartner() {
        return ansprechpartner.getVorname() + " " + ansprechpartner.getNachname();
    }
}


