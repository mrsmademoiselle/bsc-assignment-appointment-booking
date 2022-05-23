package com.example.packend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CancellationUrl {
    // TODO to be adjusted if live
    private static final String PREFIX = "http://localhost:8080/appointment/cancel/";
    private String terminId;
    @Id
    private String token;

    public String entireUrl() {
        return PREFIX + token;
    }
}
