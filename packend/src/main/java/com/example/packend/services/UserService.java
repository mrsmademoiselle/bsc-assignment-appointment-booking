package com.example.packend.services;

import com.example.packend.entities.Mitarbeiter;
import com.example.packend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    /*
    Dieses Feld muss field injection haben, damit kein Zyklus entsteht. Bei uns besteht der Zyklus bei PasswordEncoder,
    weil Spring nicht entscheiden kann, welche Bean zuerst erstellt werden soll.

    Begründung:
    Spring versucht zu Beginn die WebSecurityConfig zu erstellen.
    Dafür braucht er eine UserService-Instanz (Konstruktor). Um die zu erstellen, braucht er aber eine Bean von
    PasswordEncoder, die erst später in der WebSecurityConfig erstellt wird.
    Somit kann Spring nicht entscheiden, in welcher Reihenfolge die Dependencies aufgelöst werden sollen, weil ein
    Zyklus besteht: WebSecurity braucht UserService, UserService braucht PasswordEncoder, PasswordEncoder
    braucht zu seiner eigenen Erstellung die fertige WebSecurity.

    Warum Field Injection hier funktioniert:
    Field Injections finden (anders als Constructor Injections) erst dann statt, wenn sie gebraucht werden, sodass
    zu diesem Zeitpunkt die PasswordEncoder-Bean bereits existiert, weil der Konstruktor von UserService bereits erfolgreich
    aufgerufen wurde.

    Weitere Workarounds wären z.B. das Auslagern der PasswordEncoder-Bean in eine andere Klasse oder das field
    injecten des UserServices in der WebsecurityConfig.
    */
    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Persistiert User
     *
     * @param mitarbeiter Zu speichernden User
     * @return true wenn Objekt persistiert, false wenn Exception eintritt
     */
    public boolean saveUser(Mitarbeiter mitarbeiter) {
        if (this.userRepository.existsByUsername(mitarbeiter.getUsername())) {
            return false;
        }
        try {
            Mitarbeiter newMitarbeiter = new Mitarbeiter(mitarbeiter.getUsername(), bcryptEncoder.encode(mitarbeiter.getPassword()));
            userRepository.save(newMitarbeiter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Laden von Benutzerdetails
     *
     * @param username Username des users
     * @return Userdetails
     * @throws UsernameNotFoundException Exceptionhandling
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Mitarbeiter> optional = userRepository.findByUsername(username);

        if (optional.isEmpty())
            throw new UsernameNotFoundException("Kein Benutzer mit dem Namen " + username + " gefunden");

        Mitarbeiter mitarbeiter = optional.get();
        return new org.springframework.security.core.userdetails.User(mitarbeiter.getUsername(), mitarbeiter.getPassword(),
                new ArrayList<>());
    }
}
