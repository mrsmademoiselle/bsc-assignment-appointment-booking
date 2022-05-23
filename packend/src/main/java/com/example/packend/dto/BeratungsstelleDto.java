package com.example.packend.dto;

import lombok.Data;

@Data
public class BeratungsstelleDto {
    String id = "";

    public BeratungsstelleDto(String id) {
        this.id = id;
    }
}
