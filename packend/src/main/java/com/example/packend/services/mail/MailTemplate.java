package com.example.packend.services.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class MailTemplate {

    @Bean
    public SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        String footer = "Wir freuen uns auf Sie!\nClaudia Loof\nLohnsteuerhilfe Eutin\nLübecker Straße 36\n23701 Eutin";

        message.setText("Sehr geehrte/r %1$s %2$s,\n\nVielen Dank für Ihre Terminbuchung.\nHiermit erhalten Sie die Terminbestätigung für Ihren Termin am %3$s um %4$s Uhr in der Beratungsstelle %5$s .\n\n" +
                "Sollten Sie diesen Termin nicht wahrnehmen können, bitten wir um Absage über folgenden Link: %6$s \n\n"
                + footer);

        return message;
    }
}
