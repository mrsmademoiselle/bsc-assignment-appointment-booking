package com.example.packend.repositories;

import com.example.packend.entities.Termin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TerminRepository extends JpaRepository<Termin, String> {
    List<Termin> findAll();

    List<Termin> findAllByOrderByAusgewaehlterTerminAsc();

    List<Termin> findAllByAusgewaehlterTermin(LocalDate ausgewaehlterTermin);
}
