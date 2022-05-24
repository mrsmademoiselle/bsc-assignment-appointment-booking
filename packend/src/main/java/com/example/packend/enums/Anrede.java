package com.example.packend.enums;

public enum Anrede {
    MALE("Herr"), FEMALE("Frau"), DIVERSE("keine Angabe");

    private String anrede = "";

    Anrede(String message) {
        this.anrede = message;
    }

    public String getAnrede() {
        return anrede;
    }
}
