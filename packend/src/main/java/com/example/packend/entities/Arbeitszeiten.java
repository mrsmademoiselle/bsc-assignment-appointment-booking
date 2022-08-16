package com.example.packend.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "arbeitszeiten")
public class Arbeitszeiten {
    private static final List<String> standardValues = Arrays.asList("8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19");
    @Id
    private String mitarbeiterId;
    @ElementCollection
    private List<String> montag;
    @ElementCollection
    private List<String> dienstag;
    @ElementCollection
    private List<String> mittwoch;
    @ElementCollection
    private List<String> donnerstag;
    @ElementCollection
    private List<String> freitag;


    public Arbeitszeiten(String mitarbeiterId) {
        this.mitarbeiterId = mitarbeiterId;
        montag = standardValues;
        dienstag = standardValues;
        mittwoch = standardValues;
        donnerstag = standardValues;
        freitag = standardValues;
    }

    public List<String> getListeFuerTag(LocalDate tag) {
        return switch (tag.getDayOfWeek()) {
            case MONDAY -> montag;
            case TUESDAY -> dienstag;
            case WEDNESDAY -> mittwoch;
            case THURSDAY -> donnerstag;
            default -> freitag;
        };
    }
}
