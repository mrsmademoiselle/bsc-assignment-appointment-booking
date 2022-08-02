package com.example.packend.repositories;

import com.example.packend.entities.Abwesenheit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AbwesenheitRepository extends JpaRepository<Abwesenheit, Long> {
    List<Abwesenheit> findAll();

    List<Abwesenheit> findAllByStartDatumBetween(LocalDate startDate, LocalDate endDate);

    List<Abwesenheit> findAllByEndDatumBetween(LocalDate startDate, LocalDate endDate);
}
