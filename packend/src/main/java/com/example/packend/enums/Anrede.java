package com.example.packend.enums;

public enum Anrede {
    MALE("Herr"), FEMALE("Frau"), OTHER("");

    private String anrede = "";

    Anrede(String message) {
        this.anrede = message;
    }

    public String getAnrede() {
        return anrede;
    }
}
