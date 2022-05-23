package com.example.packend.entities;

import com.example.packend.enums.Anrede;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Embeddable
public class Kundeninformationen {
    @NotNull
    private String vorname;
    @NotNull
    private String nachname;
    @NotNull
    private Anrede geschlecht;
    @NotNull
    private String email;
    @NotNull
    private String telefon;
    @NotNull
    private boolean bereitsMitglied;

    public String buildFullName() {
        return getVorname() + " " + getNachname();
    }
}
