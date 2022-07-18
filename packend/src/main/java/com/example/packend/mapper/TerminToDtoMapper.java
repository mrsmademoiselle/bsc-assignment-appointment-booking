package com.example.packend.mapper;

import com.example.packend.dto.TerminDto;
import com.example.packend.entities.Termin;
import org.springframework.stereotype.Component;

@Component
public class TerminToDtoMapper {

    public TerminDto terminToDto(Termin termin) {
        TerminDto terminDto = new TerminDto();

        // Kundeninformationen
        terminDto.setVorname(termin.getKundeninformationen().getVorname());
        terminDto.setNachname(termin.getKundeninformationen().getNachname());
        terminDto.setEmail(termin.getKundeninformationen().getEmail());
        terminDto.setGeschlecht(termin.getKundeninformationen().getGeschlecht().getAnrede());
        terminDto.setBereitsMitglied(String.valueOf(termin.getKundeninformationen().isBereitsMitglied()));
        terminDto.setTelefon(termin.getKundeninformationen().getTelefon());

        terminDto.setBeratungsstelle(termin.getBeratungsstelle());

        terminDto.setTerminerinnerungPerMail(termin.getTerminerinnerungPerMail());
        terminDto.setBemerkung(termin.getBemerkung());
        terminDto.setBeratungsgrund(termin.getBeratungsgrund());
        terminDto.setAusgewaehlterTermin(termin.getAusgewaehlterTermin().toString());
        terminDto.setUhrzeit(termin.getUhrzeit().toString());

        return terminDto;
    }

}
