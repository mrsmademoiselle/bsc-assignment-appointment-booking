package com.example.packend.services;

import com.example.packend.entities.Mitarbeiter;
import com.example.packend.repositories.MitarbeiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MitarbeiterService implements UserDetailsService {
    private final MitarbeiterRepository mitarbeiterRepository;
    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    public MitarbeiterService(MitarbeiterRepository mitarbeiterRepository) {
        this.mitarbeiterRepository = mitarbeiterRepository;
    }

    /**
     * Speichert einen Mitarbeiter in der Datenbank und gibt den Erfolgsstatus zurück.
     */
    public boolean saveUser(Mitarbeiter mitarbeiter) {
        if (this.mitarbeiterRepository.existsByUsername(mitarbeiter.getUsername())) {
            return false;
        }
        try {
            Mitarbeiter newMitarbeiter = new Mitarbeiter(mitarbeiter.getUsername(), bcryptEncoder.encode(mitarbeiter.getPassword()), mitarbeiter.getVorname(), mitarbeiter.getNachname());
            mitarbeiterRepository.save(newMitarbeiter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Laden von Benutzerdetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Mitarbeiter> optional = mitarbeiterRepository.findByUsername(username);

        if (optional.isEmpty())
            throw new UsernameNotFoundException("Es konnte kein Mitarbeiter mit dem Namen " + username + " gefunden werden");

        Mitarbeiter mitarbeiter = optional.get();
        return new org.springframework.security.core.userdetails.User(mitarbeiter.getUsername(), mitarbeiter.getPassword(),
                new ArrayList<>());
    }

    public Mitarbeiter findMitarbeiter(String username) {
        return mitarbeiterRepository.findByUsername(username).orElseThrow(RuntimeException::new);
    }
}
