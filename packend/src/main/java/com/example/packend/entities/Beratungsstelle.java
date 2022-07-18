package com.example.packend.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Beratungsstelle {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String ansprechpartner;
    private String hausnummer;
    private String ort;
    private String plz;
    private String strasse;
}

