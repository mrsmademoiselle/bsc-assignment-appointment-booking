package com.example.packend.enums;

public enum Beratungsgrund {
    TELEFONTERMIN("Telefontermin"), ERSTBERATUNG("Erstberatung"),
    UNTERLAGEN("Unterlagen"), BERATUNG("Beratung");

    String grund;

    Beratungsgrund(String grund) {
        this.grund = grund;
    }

    public String getGrund() {
        return grund;
    }
}
