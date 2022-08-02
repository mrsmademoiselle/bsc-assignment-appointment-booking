package com.example.packend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Abwesenheit {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    Long id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDatum;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDatum;
}
