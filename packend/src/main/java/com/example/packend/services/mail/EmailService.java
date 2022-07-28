package com.example.packend.services.mail;

import com.example.packend.entities.Beratungsstelle;
import com.example.packend.entities.CancellationUrl;
import com.example.packend.entities.Termin;
import com.example.packend.enums.Anrede;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("EmailService")
public class EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    @Value("${spring.mail.username}")
    String from;
    @Value("${spring.mail.cc}")
    String cc;
    private SimpleMailMessage terminbestaetigung;
    private SimpleMailMessage terminabsage;
    private JavaMailSender emailSender;

    @Autowired
    public EmailService(SimpleMailMessage terminbestaetigung, SimpleMailMessage terminabsage, JavaMailSender emailSender) {
        this.terminbestaetigung = terminbestaetigung;
        this.terminabsage = terminabsage;
        this.emailSender = emailSender;
    }

    public void sendeTerminbestaetigung(Termin termin, CancellationUrl cancellationUrl) {

        LOGGER.info("Calling sendeTerminbestaetigung");
        String text = String.format(terminbestaetigung.getText(),
                getOffizielleAnrede(termin),
                termin.getKundeninformationen().buildFullName(),
                termin.getAusgewaehlterTermin(),
                termin.getUhrzeit(),
                getBeratungsstelleFooter(termin),
                cancellationUrl.entireUrl(),
                getBeratungsstelleFooter(termin));
        sendSimpleMessage(termin.getKundeninformationen().getEmail(), "Terminbestätigung", text, terminbestaetigung);
    }


    public void sendeTerminabsage(Termin termin) {
        LOGGER.info("Calling sendeTerminabsage");

        String text = String.format(terminabsage.getText(),
                getOffizielleAnrede(termin),
                termin.getKundeninformationen().buildFullName(),
                termin.getAusgewaehlterTermin(),
                termin.getUhrzeit(),
                getBeratungsstelleFooter(termin),
                "http://localhost:8080/book-appointment",
                getBeratungsstelleFooter(termin));
        sendSimpleMessage(termin.getKundeninformationen().getEmail(), "Terminabsage", text, terminabsage);
    }

    public void sendSimpleMessage(String to, String subject, String text, SimpleMailMessage template) {
        template.setFrom(from);
        template.setTo(to);
        template.setSubject(subject);
        template.setText(text);
        // TODO replace with vlh
        template.setCc(cc);

        try {
            emailSender.send(template);
            LOGGER.info("Sending email with subject " + subject + " to " + to + ", " + cc);
        } catch (Exception e) {
            LOGGER.error("Mail sending failed! Mail with subject " + subject + " to email addresses " + to + ", " + cc);
        }
    }

    private String getOffizielleAnrede(Termin termin) {
        Anrede geschlecht = termin.getKundeninformationen().getGeschlecht() != null
                ? termin.getKundeninformationen().getGeschlecht()
                : Anrede.DIVERSE;
        String anrede = "";

        if (geschlecht == null || (!geschlecht.equals(Anrede.MALE) && !geschlecht.equals(Anrede.FEMALE))) {
            anrede = "Sehr geehrte/r ";
        } else if (geschlecht.equals(Anrede.MALE)) {
            anrede = "Sehr geehrter " + geschlecht.getAnrede();
        } else {
            anrede = "Sehr geehrte " + geschlecht.getAnrede();
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