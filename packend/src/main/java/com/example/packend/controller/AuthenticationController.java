package com.example.packend.controller;

import com.example.packend.dto.LoginRequest;
import com.example.packend.entities.User;
import com.example.packend.services.UserAuthenticationService;
import com.example.packend.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@Transactional
@RestController
@RequestMapping
public class AuthenticationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);
    @Autowired
    UserAuthenticationService userAuthService;
    @Autowired
    UserService userService;

    @PostMapping("/public/auth/login")
    public String login(@RequestBody LoginRequest userData) {
        LOGGER.warn("Login try from User " + userData.getUsername());

        final String token = userAuthService.authenticateAndGetTokenForUserCredentials(userData.getUsername(), userData.getPassword());
        LOGGER.info("Login erfolgreich. Token: " + token);

        return token;
    }

    /**
     * Registrierung
     * Wenn Username bereits vorhanden gebe 400 wenn User noch nicht vorhanden 200
     */
    @PostMapping("/public/auth/register")
    public String register(@RequestBody LoginRequest userAuthDto) {

        // return new ResponseEntity<>("Benutzername oder Passwort sind nicht valide.", HttpStatus.BAD_REQUEST);

        User user = new User(userAuthDto.getUsername(), userAuthDto.getPassword());
        if (userService.saveUser(user)) {
            LOGGER.info("Registrierung war erfolgreich.");
            return login(userAuthDto);
        } else {
            LOGGER.warn("Benutzername ist bereits vergeben.");
            return "Der Benutzername ist bereits vergeben";
        }
    }

    @GetMapping("/auth/check")
    public void checkValidityOfToken() {
        LOGGER.info("Called auth/check successfully.");
    }
}
