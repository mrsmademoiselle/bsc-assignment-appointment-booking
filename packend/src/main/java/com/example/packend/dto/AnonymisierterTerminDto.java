package com.example.packend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AnonymisierterTerminDto {
    private int tag;
    private int jahr;
    private int monat;
    private String uhrzeit;
}
