package com.example.packend.services.mail;

import com.example.packend.entities.Beratungsstelle;
import com.example.packend.entities.CancellationUrl;
import com.example.packend.entities.Termin;
import com.example.packend.enums.Anrede;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("EmailService")
public class EmailService {
    @Value("spring.mail.username")
    String from;
    @Value("spring.mail.cc")
    String cc;
    @Autowired
    private SimpleMailMessage terminbestaetigung;
    @Autowired
    private SimpleMailMessage terminabsage;
    @Autowired
    private JavaMailSender emailSender;

    public void sendeTerminbestaetigung(Termin termin, CancellationUrl cancellationUrl) {
        String text = String.format(terminbestaetigung.getText(),
                getOffizielleAnrede(termin),
                termin.getKundeninformationen().buildFullName(),
                termin.getAusgewaehlterTermin(),
                termin.getUhrzeit(),
                getBeratungsstelleFooter(termin),
                cancellationUrl.entireUrl(), getBeratungsstelleFooter(termin));
        sendSimpleMessage(termin.getKundeninformationen().getEmail(), "Terminbestätigung", text, terminbestaetigung);
    }


    public void sendeTerminabsage(Termin termin) {
        String text = String.format(terminabsage.getText(),
                getOffizielleAnrede(termin),
                termin.getKundeninformationen().buildFullName(),
                termin.getAusgewaehlterTermin(),
                termin.getUhrzeit(),
                getBeratungsstelleFooter(termin),
                "http://localhost:8080/book-appointment", getBeratungsstelleFooter(termin));
        sendSimpleMessage(termin.getKundeninformationen().getEmail(), "Terminabsage", text, terminabsage);
    }

    public void sendSimpleMessage(String to, String subject, String text, SimpleMailMessage template) {
        template.setFrom(from);
        template.setTo(to);
        template.setSubject(subject);
        template.setText(text);
        // TODO replace with vlh
        template.setCc(cc);
        emailSender.send(template);
    }

    private String getOffizielleAnrede(Termin termin) {
        Anrede geschlecht = termin.getKundeninformationen().getGeschlecht();
        String anrede = "";

        if (geschlecht.equals(Anrede.MALE)) {
            anrede = "Sehr geehrter " + geschlecht.getAnrede();
        } else if (geschlecht.equals(Anrede.FEMALE)) {
            anrede = "Sehr geehrte " + geschlecht.getAnrede();
        } else {
            anrede = "Sehr geehrte/r ";
        }
        return anrede;
    }

    private String getBeratungsstelleFooter(Termin termin) {
        final String LINEBREAK = "\n";
        final String SPACE = " ";

        Beratungsstelle beratungsstelle = termin.getBeratungsstelle();
        String ansprechpartner = beratungsstelle.getAnsprechpartner();
        String strasseHausnummer = beratungsstelle.getStrasse() + SPACE + beratungsstelle.getHausnummer();
        String plzOrt = beratungsstelle.getPlz() + SPACE + beratungsstelle.getOrt();

        return LINEBREAK + LINEBREAK + ansprechpartner +
                LINEBREAK + "Vereinigte Lohnsteuerhilfe e.V." +
                LINEBREAK + strasseHausnummer +
                LINEBREAK + plzOrt + LINEBREAK;
    }


}