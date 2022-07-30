package com.example.packend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Data
public class CancellationUrl {
    // TODO to be adjusted if live
    private static final String PREFIX = "http://localhost:8080/appointment/cancel/";
    @Id
    private Long terminId;
    private String token;

    public CancellationUrl(Long terminId, String token) {
        this.terminId = terminId;
        this.token = token;
    }

    public String entireUrl() {
        return PREFIX + token;
    }
}
