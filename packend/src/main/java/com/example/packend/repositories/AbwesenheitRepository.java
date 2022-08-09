package com.example.packend.repositories;

import com.example.packend.entities.Abwesenheit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AbwesenheitRepository extends JpaRepository<Abwesenheit, Long> {
    List<Abwesenheit> findAll();

    List<Abwesenheit> findAllByMitarbeiter_UsernameAndStartDatumBetween(String username, LocalDate startDate, LocalDate endDate);

    List<Abwesenheit> findAllByMitarbeiter_UsernameAndEndDatumBetween(String username, LocalDate startDate, LocalDate endDate);

    List<Abwesenheit> findAllByMitarbeiter_Username(String username, Sort sort);

    @Override
    List<Abwesenheit> findAll(Sort sort);

}
