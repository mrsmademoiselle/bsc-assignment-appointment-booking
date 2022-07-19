package com.example.packend.services;

import com.example.packend.config.JwtTokenUtil;
import com.example.packend.entities.User;
import com.example.packend.repositories.UserRepository;
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
public class UserAuthenticationService {
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserAuthenticationService.class);

    public UserAuthenticationService(JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager, UserRepository userRepository, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Versucht, den Benutzer zu authentifizieren. Gibt ein neues Token zurück, wenn erfolgreich.
     */
    public String authenticateAndGetTokenForUserCredentials(String username, String password) {
        // Pruefen ob Token existiert
        UserDetails userDetails = userService.loadUserByUsername(username);
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
    public User getUserFromJwt(String jwtToken) {
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElseThrow(RuntimeException::new);
    }
}