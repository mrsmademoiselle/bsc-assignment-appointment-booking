package com.example.packend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Abwesenheit {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    Long id;
    String startDatum;
    String endDatum;
}
