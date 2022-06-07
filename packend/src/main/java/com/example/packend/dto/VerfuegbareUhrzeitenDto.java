package com.example.packend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class VerfuegbareUhrzeitenDto {
    private List<String> montag;
    private List<String> dienstag;
    private List<String> mittwoch;
    private List<String> donnerstag;
    private List<String> freitag;

    public List<String> getListeFuerTag(LocalDate tag) {
        switch (tag.getDayOfWeek()) {
            case MONDAY:
                return montag;
            case TUESDAY:
                return dienstag;
            case WEDNESDAY:
                return mittwoch;

            case THURSDAY:
                return donnerstag;
            default:
                return freitag;
        }
    }
}
