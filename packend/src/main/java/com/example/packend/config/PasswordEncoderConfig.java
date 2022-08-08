package com.example.packend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Diese Bean wird für die Erstellung des MitarbeiterService-Objekts benötigt (PasswordEncoder)
 */
@Configuration
public class PasswordEncoderConfig {
    /**
     * Encoder fuer Passwörter. Wird fuer das JWT-handling verwendet.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
