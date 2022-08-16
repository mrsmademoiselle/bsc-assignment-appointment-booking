package com.example.packend.repositories;

import com.example.packend.entities.Mitarbeiter;
import com.example.packend.entities.Termin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TerminRepository extends JpaRepository<Termin, Long> {
    List<Termin> findAll();

    Optional<Termin> findById(Long id);

    List<Termin> findAllByOrderByAusgewaehlterTerminAsc();

    List<Termin> findAllByAusgewaehlterTerminBefore(LocalDate now);

    List<Termin> findAllByAusgewaehlterTerminAndBeratungsstelle_Ansprechpartner(LocalDate ausgewaehlterTermin, Mitarbeiter ansprechpartner);

    List<Termin> findAllByBeratungsstelle_Ansprechpartner_UsernameAndAusgewaehlterTerminBetween(String username, LocalDate startDate, LocalDate endDate);

    List<Termin> findAllByAusgewaehlterTerminAndUhrzeit(LocalDate ausgewaehlterTermin, LocalTime uhrzeit);
}
