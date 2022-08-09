package com.example.packend.mapper;

import com.example.packend.dto.TerminDto;
import com.example.packend.entities.Termin;
import com.example.packend.enums.Anrede;
import org.springframework.stereotype.Component;

@Component
public class TerminToDtoMapper {

    public TerminDto terminToDto(Termin termin) {
        TerminDto terminDto = new TerminDto();

        // Kundeninformationen
        terminDto.setId(termin.getId());
        terminDto.setVorname(termin.getKundeninformationen().getVorname());
        terminDto.setNachname(termin.getKundeninformationen().getNachname());
        terminDto.setEmail(termin.getKundeninformationen().getEmail());

        if (termin.getKundeninformationen().getAnrede() != null) {
            terminDto.setGeschlecht(termin.getKundeninformationen().getAnrede().getAnrede());
        } else {
            terminDto.setGeschlecht(Anrede.DIVERS.getAnrede());
        }
        terminDto.setBereitsMitglied(String.valueOf(termin.getKundeninformationen().isBereitsMitglied()));
        terminDto.setTelefon(termin.getKundeninformationen().getTelefon());

        terminDto.setBeratungsstelle(termin.getBeratungsstelle());

        terminDto.setBemerkung(termin.getBemerkung());
        terminDto.setBeratungsgrund(termin.getBeratungsgrund());
        terminDto.setAusgewaehlterTermin(termin.getAusgewaehlterTermin().toString());
        terminDto.setUhrzeit(termin.getUhrzeit().toString());

        return terminDto;
    }

}
