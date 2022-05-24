package com.example.packend.services.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class MailTemplate {

    @Bean
    public SimpleMailMessage terminabsage() {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setText("%1$s %2$s,\n\nIhr Termin am %3$s um %4$s Uhr in der Beratungsstelle %5$s \nwurde abgesagt.\n\n" +
                "Neue Termine können Sie jederzeit unter folgendem Link buchen: %6$s \n\nMit freundlichen Grüßen %7$s");

        return message;
    }

    @Bean
    public SimpleMailMessage terminbestaetigung() {
        SimpleMailMessage message = new SimpleMailMessage();
        String footer = "Wir freuen uns auf Sie!";

        message.setText("%1$s %2$s,\n\nVielen Dank für Ihre Terminbuchung.\nHiermit erhalten Sie die Terminbestätigung für Ihren Termin am %3$s um %4$s Uhr in der Beratungsstelle %5$s \n\n" +
                "Sollten Sie diesen Termin nicht wahrnehmen können, bitten wir um Absage über folgenden Link: %6$s \n\n"
                + footer + "\n\nMit freundlichen Grüßen %7$s");

        return message;
    }
}
