package com.example.packend.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@ToString
@Getter
@Setter
@Entity(name = "mitarbeiter")
@NoArgsConstructor
public class Mitarbeiter {
    @Column
    @Id
    private String username;
    // TODO: Adjust datatype for password
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