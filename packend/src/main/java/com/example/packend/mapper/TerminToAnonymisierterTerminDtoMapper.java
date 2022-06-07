package com.example.packend.mapper;

import com.example.packend.dto.AnonymisierterTerminDto;
import com.example.packend.entities.Termin;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TerminToAnonymisierterTerminDtoMapper {

    public List<AnonymisierterTerminDto> mapToAnonymisierterTerminDto(List<Termin> alleTermine) {
        return alleTermine.stream()
                .map(e -> new AnonymisierterTerminDto(e.getAusgewaehlterTermin().getDayOfMonth(),
                        e.getAusgewaehlterTermin().getYear(),
                        e.getAusgewaehlterTermin().getMonthValue(),
                        e.getUhrzeit().toString()))
                .collect(Collectors.toList());
    }
}
