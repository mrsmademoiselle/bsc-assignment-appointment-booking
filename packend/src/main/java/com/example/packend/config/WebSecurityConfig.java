package com.example.packend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Konfigurationsklasse für die Security der Anwendung
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationExceptionHandler jwtAuthenticationExceptionHandler;
    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public WebSecurityConfig(JwtAuthenticationExceptionHandler jwtAuthenticationExceptionHandler, JwtRequestFilter jwtRequestFilter) {
        this.jwtAuthenticationExceptionHandler = jwtAuthenticationExceptionHandler;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // csrf aus machen, da nicht benoetigt
        httpSecurity.csrf().disable()
                .cors().and()
                // auth-header check fuer folgende Endpunkte deaktivieren
                .authorizeRequests().antMatchers("/public/**").permitAll().
                // Alle Anfragen an anderen Endpunkten werden ueberprueft
                        anyRequest().authenticated().and().
                // Entrypoint fuer authmanagement angeben und Sessionmanagement deaktivieren
                        exceptionHandling().authenticationEntryPoint(jwtAuthenticationExceptionHandler).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // Filter Richtlinie - jeder Header eines Requests wird einmalig nach dem Token validiert
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
