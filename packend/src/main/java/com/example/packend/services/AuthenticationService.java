package com.example.packend.services;

import com.example.packend.config.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final MitarbeiterService mitarbeiterService;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager, MitarbeiterService mitarbeiterService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.mitarbeiterService = mitarbeiterService;
    }

    /**
     * Versucht, den Benutzer zu authentifizieren. Gibt ein neues Token zurück, wenn erfolgreich.
     */
    public String authentifiziereUndErstelleTokenFuerUser(String username, String password) {

        UserDetails userDetails = mitarbeiterService.loadUserByUsername(username);
        authenticate(username, password);

        return jwtTokenUtil.generateToken(userDetails);
    }

    /**
     * Versucht den Benutzer zu authentifizieren. Wirft eine Exception, wenn das nicht möglich ist.
     */
    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            logger.error("Falsche Zugangsdaten");
        }
    }
}
