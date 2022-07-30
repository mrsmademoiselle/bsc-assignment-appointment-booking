package com.example.packend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MitarbeiterDto {
    private String username;
    private String vorname;
    private String nachname;
}
