package com.example.packend.services;

import com.example.packend.config.JwtTokenUtil;
import com.example.packend.entities.Mitarbeiter;
import com.example.packend.repositories.MitarbeiterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final MitarbeiterRepository mitarbeiterRepository;
    private final MitarbeiterService mitarbeiterService;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager, MitarbeiterRepository mitarbeiterRepository, MitarbeiterService mitarbeiterService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.mitarbeiterRepository = mitarbeiterRepository;
        this.mitarbeiterService = mitarbeiterService;
    }

    /**
     * Versucht, den Benutzer zu authentifizieren. Gibt ein neues Token zurück, wenn erfolgreich.
     */
    public String authenticateAndGetTokenForUserCredentials(String username, String password) {
        // Pruefen ob Token existiert
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
        } catch (DisabledException e) {
            logger.error("Account ist gesperrt. Darf in unserer Anwendung nicht auftreten.");
        } catch (BadCredentialsException e) {
            logger.error("Falsche Zugangsdaten");
        }
    }

    /**
     * Gibt den User zu einem jwt zurück. Wenn dieser nicht existiert, wird eine NotFoundException geworfen.
     */
    public Mitarbeiter getUserFromJwt(String jwtToken) {
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        Optional<Mitarbeiter> userOptional = mitarbeiterRepository.findByUsername(username);
        return userOptional.orElseThrow(RuntimeException::new);
    }
}
