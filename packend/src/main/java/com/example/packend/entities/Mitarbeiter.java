package com.example.packend.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@ToString
@Getter
@Setter
@NoArgsConstructor

@Entity(name = "mitarbeiter")
public class Mitarbeiter {
    @OneToOne
    Arbeitszeiten arbeitszeiten;

    @Column
    @Id
    private String username;
    @Column
    private String password;
    @Column
    private String vorname;
    @Column
    private String nachname;


    @Builder
    public Mitarbeiter(String username, String password, String vorname, String nachname) {
        this.username = username;
        this.password = password;
        this.nachname = nachname;
        this.vorname = vorname;
    }

    public Mitarbeiter(String username, String password) {
        this.username = username;
        this.password = password;
    }
}