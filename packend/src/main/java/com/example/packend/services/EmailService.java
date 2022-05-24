package com.example.packend.services;

import com.example.packend.entities.Beratungsstelle;
import com.example.packend.entities.CancellationUrl;
import com.example.packend.entities.Termin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("EmailService")
public class EmailService {
    @Autowired
    private SimpleMailMessage template;
    @Autowired
    private JavaMailSender emailSender;

    public void sendeTerminbestaetigung(Termin termin, Beratungsstelle beratungsstelle, CancellationUrl cancellationUrl) {
        String beratungsstelleAlsText = beratungsstelle.getAnsprechpartner() + "\n"
                + beratungsstelle.getStrasse() + " " + beratungsstelle.getHausnummer() + "\n"
                + beratungsstelle.getPlz() + " " + beratungsstelle.getOrt();
        String text = String.format(template.getText(),
                termin.getKundeninformationen().getGeschlecht().getAnrede(),
                termin.getKundeninformationen().buildFullName(),
                termin.getAusgewaehlterTermin(),
                termin.getUhrzeit(),
                beratungsstelleAlsText,
                cancellationUrl.entireUrl());
        sendSimpleMessage(termin.getKundeninformationen().getEmail(), "Terminbestätigung", text);
    }

    public void sendeTerminabsage(Termin termin) {

    }

    public void sendSimpleMessage(String to, String subject, String text) {
        template.setFrom("usimcdusi@web.de");
        template.setTo(to);
        template.setSubject(subject);
        template.setText(text);
        // TODO replace with vlh
        template.setCc("franziska.loof@stud.th-luebeck.de");
        emailSender.send(template);
        System.out.println("Message sent!" + template.toString());
    }


}