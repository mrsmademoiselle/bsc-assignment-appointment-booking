package com.example.packend.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Anrede {
    HERR("Herr"), FRAU("Frau"), DIVERS("");

    private String anrede = "";

    Anrede(String message) {
        this.anrede = message;
    }

    @JsonValue
    public String getAnrede() {
        return anrede;
    }
}
