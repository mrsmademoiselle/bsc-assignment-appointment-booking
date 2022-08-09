package com.example.packend.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Beratungsgrund {
    TELEFONTERMIN("Telefontermin"), ERSTBERATUNG("Erstberatung"),
    UNTERLAGEN("Unterlagen"), BERATUNG("Beratung");

    String grund;

    Beratungsgrund(String grund) {
        this.grund = grund;
    }

    @JsonValue
    public String getGrund() {
        return grund;
    }
}
