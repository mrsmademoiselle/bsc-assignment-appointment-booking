package com.example.packend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeratungsstelleDto {
    private String id;
    private MitarbeiterDto ansprechpartner;
    private String hausnummer;
    private String ort;
    private String plz;
    private String strasse;
    private List<MitarbeiterDto> mitarbeiterListe;

    public BeratungsstelleDto(String id) {
        this.id = id;
    }
}
